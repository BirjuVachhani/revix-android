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

package com.birjuvachhani.revix.basic

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.birjuvachhani.revix.common.BaseVH
import com.birjuvachhani.revix.common.RecyclerAdapterState
import com.birjuvachhani.revix.smart.SpecialViewType

open class BasicAdapter<T>(func: BasicAdapterBuilder<T>.() -> Unit) : RecyclerView.Adapter<BaseVH>(), Filterable {

    protected var baseList: ArrayList<T> = ArrayList()
    protected var filteredList: ArrayList<T> = ArrayList()
    protected val builder: BasicAdapterBuilder<T>
    private val state = MutableLiveData<RecyclerAdapterState>()
    protected var filter = AdapterFilter()

    companion object {
        private const val EMPTY = 1
        private const val NON_EMPTY = 2
        private const val LOADING = 3
        private const val ERROR = 4
    }

    init {
        state.value = RecyclerAdapterState.Empty
        builder = BasicAdapterBuilder<T>().apply(func)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH = when (viewType) {
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
            val type = builder.itemViewType
            when (type) {
                is BasicViewType.Specified<*> -> getViewHolder(parent, type.layoutId)
                is BasicViewType.Unspecified -> throw Exception("No layoutId specified for recycler view item")
            }
        }
    }

    private fun getViewHolderFromType(type: SpecialViewType, parent: ViewGroup, typedName: String) =
        when (type) {
            is SpecialViewType.Inflated -> BaseVH(type.view)
            is SpecialViewType.Raw -> getViewHolder(parent, type.layoutId)
            is SpecialViewType.Unspecified -> throw Exception("Tried to use $typedName view when it is not configured")
        }

    private fun getViewHolder(parent: ViewGroup, layoutId: Int): BaseVH =
        BaseVH(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun getItemCount(): Int = when {
        state.value is RecyclerAdapterState.Empty && builder.hasEmptyView() -> 1
        state.value is RecyclerAdapterState.Error && builder.hasErrorView() -> 1
        state.value is RecyclerAdapterState.Loading && builder.hasLoadingView() -> 1
        state.value is RecyclerAdapterState.Data -> filteredList.size
        else -> 0
    }

    override fun getItemViewType(position: Int): Int = when {
        state.value is RecyclerAdapterState.Empty && builder.hasEmptyView() -> EMPTY
        state.value is RecyclerAdapterState.Error && builder.hasErrorView() -> ERROR
        state.value is RecyclerAdapterState.Loading && builder.hasLoadingView() -> LOADING
        else -> NON_EMPTY
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> {
                val type = builder.emptyViewType
                when (type) {
                    is SpecialViewType.Raw -> type.bindFunc(holder.itemView)
                }
            }
            ERROR -> {
                val type = builder.errorViewType
                when (type) {
                    is SpecialViewType.Raw -> type.bindFunc(holder.itemView)
                }
            }
            LOADING -> {
                val type = builder.loadingViewType
                when (type) {
                    is SpecialViewType.Raw -> type.bindFunc(holder.itemView)
                }
            }
            NON_EMPTY -> {
                val type = builder.itemViewType
                when (type) {
                    is BasicViewType.Specified -> {
                        type.bindFunc.invoke(filteredList[position], holder.itemView)
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

    fun setData(newList: ArrayList<T>) {
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

    private fun checkEmpty(list: ArrayList<T>) {
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
                val type = builder.itemViewType
                filteredList.addAll(baseList.filter { item ->
                    when (type) {
                        is BasicViewType.Specified -> type.filterFunc(item, searchString)
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