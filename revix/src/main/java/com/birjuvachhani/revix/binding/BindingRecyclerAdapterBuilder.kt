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

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.birjuvachhani.revix.R
import com.birjuvachhani.revix.common.BaseModel
import kotlinx.android.synthetic.main.item_empty_default.view.*

/**
 * Created by Birju Vachhani on 04/12/18.
 */

class BindingRecyclerAdapterBuilder {

    internal var holderData = mutableMapOf<Int, BindingHolderData>()
    internal var emptyView: SpecialBindingViewTypeBuilder? = null
    internal var errorView: SpecialBindingViewTypeBuilder? = null
    internal var loadingView: SpecialBindingViewTypeBuilder? = null

    inline fun <reified T : BaseModel> addViewType(br: Int, func: ViewTypeBindingBuilder<T>.() -> Unit) {
        val holder = ViewTypeBindingBuilder<T>().apply {
            this.br = br
            func()
            modelClass = T::class.java
        }
        try {
            `access$addToHolderData`(holder)
        } catch (e: Exception) {
            e.printStackTrace()
            throw java.lang.RuntimeException("Cannot cast to ViewTypeBuilder<BaseModel>")
        }
    }

    inline fun <reified T : BaseModel> addViewType(
        br: Int, @LayoutRes id: Int,
        func: ViewTypeBindingBuilder<T>.() -> Unit = {}
    ) {
        val holder = ViewTypeBindingBuilder<T>().apply {
            this.br = br
            layout from id
            func()
            modelClass = T::class.java
        }
        try {
            `access$addToHolderData`(holder)
        } catch (e: Exception) {
            e.printStackTrace()
            throw java.lang.RuntimeException("Cannot cast to ViewTypeBuilder<BaseModel>")
        }
    }

    fun addEmptyView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        emptyView = SpecialBindingViewTypeBuilder().apply {
            func()
        }
    }

    fun addErrorView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        errorView = SpecialBindingViewTypeBuilder().apply {
            func()
        }
    }

    fun addLoadingView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        loadingView = SpecialBindingViewTypeBuilder().apply {
            func()
        }
    }

    private fun <T : BaseModel> addToHolderData(holder: ViewTypeBindingBuilder<T>) {
        @Suppress("UNCHECKED_CAST")
        holderData[holder.modelClass.hashCode()] =
            BindingHolderData(
                holder.layoutId,
                holder as ViewTypeBindingBuilder<BaseModel>
            )
    }

    fun hasEmptyView(): Boolean = emptyView !== null

    fun hasErrorView(): Boolean = errorView !== null

    fun hasLoadingView(): Boolean = loadingView !== null

    @PublishedApi
    internal fun <T : BaseModel> `access$addToHolderData`(holder: ViewTypeBindingBuilder<T>) = addToHolderData(holder)

    fun addDefaultEmptyView(msg: String = "No item found") {
        emptyView = SpecialBindingViewTypeBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.root.tvDefaultErrorMessage.text = msg
            }
        }
    }

    fun addDefaultEmptyView(@StringRes msgId: Int) {
        emptyView = SpecialBindingViewTypeBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.root.tvDefaultErrorMessage.text = it.root.context.getString(msgId)
            }
        }
    }

    fun addDefaultLoadingView() {
        loadingView = SpecialBindingViewTypeBuilder().apply {
            layout from R.layout.item_loading_default
        }
    }
}