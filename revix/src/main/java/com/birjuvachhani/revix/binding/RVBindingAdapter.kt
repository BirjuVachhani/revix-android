/*
 * Copyright 2019 BirjuVachhani (https://github.com/BirjuVachhani)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.birjuvachhani.revix.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.birjuvachhani.revix.common.BaseBindingVH
import com.birjuvachhani.revix.common.BaseModel
import com.birjuvachhani.revix.common.RecyclerAdapterState
import com.birjuvachhani.revix.common.classHash

/**
 * Created by Birju Vachhani on 30/11/18.
 */

class RVBindingAdapter(config: BindingRecyclerAdapterBuilder.() -> Unit) :
    RecyclerView.Adapter<BaseBindingVH>(), Filterable {

    protected var baseList: ArrayList<BaseModel> = ArrayList()
    protected var filteredList: ArrayList<BaseModel> = ArrayList()
    internal var holders: MutableMap<Int, BindingViewType<BaseModel>> = mutableMapOf()
    protected val builder: BindingRecyclerAdapterBuilder
    protected val state = MutableLiveData<RecyclerAdapterState>()
    protected var filter = AdapterFilter()

    protected val EMPTY = 1
    protected val NON_EMPTY = 2
    protected val LOADING = 3
    protected val ERROR = 4

    init {
        state.value = RecyclerAdapterState.Empty
        builder = BindingRecyclerAdapterBuilder().apply {
            config()
        }
        holders = builder.holderData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingVH =
        when (viewType) {
            EMPTY -> {
                getViewHolderFromType(builder.emptyViewType, parent, "empty")
            }
            ERROR -> {
                getViewHolderFromType(builder.errorViewType, parent, "error")
            }
            LOADING -> {
                getViewHolderFromType(builder.loadingViewType, parent, "loading")
            }
            else -> {
                val type = holders[viewType]
                when (type) {
                    is BindingViewType.Specified<*> -> getViewHolder(parent, type.layoutId)
                    else -> throw Exception("No layoutId specified for recycler view item")
                }
            }
        }

    private fun getViewHolderFromType(type: SpecialBindingViewType, parent: ViewGroup, typedName: String) =
        when (type) {
            is SpecialBindingViewType.Inflated -> BaseBindingVH(type.mBinding)
            is SpecialBindingViewType.Raw -> getViewHolder(parent, type.layoutId)
            is SpecialBindingViewType.Unspecified -> throw Exception("Tried to use $typedName view when it is not configured")
        }

    private fun getViewHolder(parent: ViewGroup, @LayoutRes id: Int) =
        BaseBindingVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), id, parent, false))

    override fun getItemCount(): Int {
        return when {
            state.value is RecyclerAdapterState.Empty && builder.hasEmptyView() -> 1
            state.value is RecyclerAdapterState.Error && builder.hasErrorView() -> 1
            state.value is RecyclerAdapterState.Loading && builder.hasLoadingView() -> 1
            state.value is RecyclerAdapterState.Data -> filteredList.size
            else -> 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            state.value is RecyclerAdapterState.Empty && builder.hasEmptyView() -> EMPTY
            state.value is RecyclerAdapterState.Error && builder.hasErrorView() -> ERROR
            state.value is RecyclerAdapterState.Loading && builder.hasLoadingView() -> LOADING
            state.value is RecyclerAdapterState.Data -> filteredList[position].classHash()
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: BaseBindingVH, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> {
                val type = builder.emptyViewType
                when (type) {
                    is SpecialBindingViewType.Raw -> type.bindFunc(holder.mBinding)
                }
            }
            ERROR -> {
                val type = builder.errorViewType
                when (type) {
                    is SpecialBindingViewType.Raw -> type.bindFunc(holder.mBinding)
                }
            }
            LOADING -> {
                val type = builder.loadingViewType
                when (type) {
                    is SpecialBindingViewType.Raw -> type.bindFunc(holder.mBinding)
                }
            }
            else -> {

                val type = holders[filteredList[position].classHash()]
                when (type) {
                    is BindingViewType.Specified -> {
                        holder.mBinding.setVariable(type.variable, filteredList[position])
                        type.bindFunc(filteredList[position], holder.mBinding)
                        holder.itemView.setOnClickListener {
                            type.clickFunc(
                                holder.itemView,
                                filteredList[holder.adapterPosition],
                                holder.adapterPosition
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun setData(newList: ArrayList<BaseModel>) {
        checkEmpty(newList)
        this.filteredList = newList
        this.baseList = ArrayList(newList)
        notifyDataSetChanged()
    }

    fun setLoading() {
        state.value = RecyclerAdapterState.Loading
        notifyDataSetChanged()
    }

    fun setError() {
        state.value = RecyclerAdapterState.Error
        notifyDataSetChanged()
    }

    private fun checkEmpty(list: ArrayList<BaseModel>) {
        state.value = when (list.size) {
            0 -> RecyclerAdapterState.Empty
            else -> RecyclerAdapterState.Data
        }
    }

    /**
     * clears filters applied on the list and shows original list
     * */
    fun clearFilter() {
        filteredList.clear()
        filteredList = ArrayList(baseList)
        notifyDataSetChanged()
    }

    /**
     * Filter class for applying filters to the list
     * */
    inner class AdapterFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val searchString = constraint.toString()
            if (searchString.isEmpty()) {
                filteredList = baseList
            } else {
                filteredList.clear()
                filteredList.addAll(baseList.filter { item ->
                    val type = holders[item.classHash()]
                    when (type) {
                        is BindingViewType.Specified -> type.filterFunc(item, searchString)
                        else -> false
                    }
                })
            }
            state.postValue(
                if (filteredList.isEmpty()) {
                    RecyclerAdapterState.Empty
                } else {
                    RecyclerAdapterState.Data
                }
            )
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}
