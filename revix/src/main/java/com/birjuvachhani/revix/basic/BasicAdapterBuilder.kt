package com.birjuvachhani.revix.basic

import androidx.annotation.StringRes
import com.birjuvachhani.revix.R
import com.birjuvachhani.revix.smart.SpecialViewTypeBuilder
import kotlinx.android.synthetic.main.item_empty_default.view.*

class BasicAdapterBuilder<T> {

    internal var emptyViewBuilder: SpecialViewTypeBuilder? = null
    internal var errorViewBuilder: SpecialViewTypeBuilder? = null
    internal var loadingViewBuilder: SpecialViewTypeBuilder? = null
    var viewBuilder: BasicViewTypeBuilder<T>? = null

    inline fun setViewType(func: BasicViewTypeBuilder<T>.() -> Unit) {
        viewBuilder = BasicViewTypeBuilder<T>().apply { func() }
    }

    fun addEmptyView(func: SpecialViewTypeBuilder.() -> Unit) {
        emptyViewBuilder = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    fun addErrorView(func: SpecialViewTypeBuilder.() -> Unit) {
        errorViewBuilder = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    fun addLoadingView(func: SpecialViewTypeBuilder.() -> Unit) {
        loadingViewBuilder = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    fun hasEmptyView(): Boolean = emptyViewBuilder !== null

    fun hasErrorView(): Boolean = errorViewBuilder !== null

    fun hasLoadingView(): Boolean = loadingViewBuilder !== null

    fun addDefaultEmptyView(msg: String = "No item found") {
        emptyViewBuilder = SpecialViewTypeBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = msg
            }
        }
    }

    fun addDefaultEmptyView(@StringRes msgId: Int) {
        emptyViewBuilder = SpecialViewTypeBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = it.itemView.context.getString(msgId)
            }
        }
    }

    fun addDefaultLoadingView() {
        loadingViewBuilder = SpecialViewTypeBuilder().apply {
            layout from R.layout.item_loading_default
        }
    }
}