package com.birjuvachhani.revix

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Birju Vachhani on 30/11/18.
 */

class RVAdapter(config: RecyclerAdapterBuilder.() -> Unit) :
    RecyclerView.Adapter<BaseVH>(), Filterable {

    protected var baseList: ArrayList<BaseModel> = ArrayList()
    protected var filteredList: ArrayList<BaseModel> = ArrayList()
    internal var holders: MutableMap<Int, HolderData> = mutableMapOf()
    protected val builder: RecyclerAdapterBuilder
    protected val state = MutableLiveData<RecyclerAdapterState>()
    protected var filter = AdapterFilter()

    protected val EMPTY = 1
    protected val NON_EMPTY = 2
    protected val LOADING = 3
    protected val ERROR = 4

    init {
        state.value = RecyclerAdapterState.Empty
        builder = RecyclerAdapterBuilder().apply {
            config()
        }
        holders = builder.holderData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            EMPTY -> {
                builder.emptyView?.layout?.let { layoutRes ->
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutRes, parent, false)
                    )
                } ?: throw Exception("Layout Res not found for empty view")
            }
            ERROR -> {
                builder.errorView?.layout?.let { layoutRes ->
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutRes, parent, false)
                    )
                } ?: throw Exception("Layout Res not found for error view")
            }
            LOADING -> {
                builder.loadingView?.layout?.let { layoutRes ->
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutRes, parent, false)
                    )
                } ?: throw Exception("Layout Res not found loading view")
            }
            else -> {
                holders[viewType]?.let {
                    if (it.layout == 0) {
                        throw RuntimeException("No layout specified for model: ${it.builder.modelClass.simpleName}")
                    }
                    val view = LayoutInflater.from(parent.context).inflate(it.layout, parent, false)
                    return BaseVH(view)
                } ?: throw RuntimeException("View type is not found")
            }
        }
    }

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

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> {
                builder.emptyView?.apply { bindFunc(holder) }
            }
            ERROR -> {
                builder.errorView?.apply { bindFunc(holder) }
            }
            LOADING -> {
                builder.loadingView?.apply { bindFunc(holder) }
            }
            else -> {
                holders[filteredList[position].classHash()]?.run {
                    builder.bindFunc.invoke(
                        filteredList[position],
                        holder
                    )
                }
                holder.itemView.setOnClickListener {
                    holders[filteredList[position].classHash()]?.run {
                        builder.clickFunc.invoke(
                            holder.itemView,
                            filteredList[position],
                            holder.adapterPosition
                        )
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
                    holders[item.classHash()]
                        ?.builder?.filterFunc
                        ?.invoke(item, searchString) ?: false
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
