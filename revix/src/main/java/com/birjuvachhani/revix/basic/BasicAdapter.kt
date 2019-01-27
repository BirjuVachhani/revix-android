package com.birjuvachhani.revix.basic

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.birjuvachhani.revix.common.BaseVH
import com.birjuvachhani.revix.common.RecyclerAdapterState

class BasicAdapter<T>(func: BasicAdapterBuilder<T>.() -> Unit) : RecyclerView.Adapter<BaseVH>(), Filterable {

    protected var baseList: ArrayList<T> = ArrayList()
    protected var filteredList: ArrayList<T> = ArrayList()
    protected val builder: BasicAdapterBuilder<T>
    protected val state = MutableLiveData<RecyclerAdapterState>()
    protected var filter = AdapterFilter()

    protected val EMPTY = 1
    protected val NON_EMPTY = 2
    protected val LOADING = 3
    protected val ERROR = 4

    init {
        state.value = RecyclerAdapterState.Empty
        builder = BasicAdapterBuilder<T>().apply {
            func()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            EMPTY -> {
                builder.emptyViewBuilder?.run {
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutId, parent, false)
                    )
                } ?: throw Exception("Layout Res not found for empty view")
            }
            ERROR -> {
                builder.errorViewBuilder?.run {
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutId, parent, false)
                    )
                } ?: throw Exception("Layout Res not found for error view")
            }
            LOADING -> {
                builder.loadingViewBuilder?.run {
                    BaseVH(
                        LayoutInflater.from(parent.context)
                            .inflate(layoutId, parent, false)
                    )
                } ?: throw Exception("Layout Res not found loading view")
            }
            else -> {
                builder.viewBuilder?.let {
                    if (it.layoutId == 0) {
                        throw RuntimeException("No layoutId specified for recycler item")
                    }
                    val view = LayoutInflater.from(parent.context).inflate(it.layoutId, parent, false)
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
            else -> NON_EMPTY
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> {
                builder.emptyViewBuilder?.apply { bindFunc(holder) }
            }
            ERROR -> {
                builder.errorViewBuilder?.apply { bindFunc(holder) }
            }
            LOADING -> {
                builder.loadingViewBuilder?.apply { bindFunc(holder) }
            }
            NON_EMPTY -> {
                builder.viewBuilder?.bindFunc?.invoke(
                    filteredList[position],
                    holder
                )
                holder.itemView.setOnClickListener {
                    builder.viewBuilder?.clickFunc?.invoke(
                        holder.itemView,
                        filteredList[position],
                        holder.adapterPosition
                    )
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
                filteredList.addAll(baseList.filter { item ->
                    builder.viewBuilder?.filterFunc?.invoke(item, searchString) ?: false
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