/*
 * Copyright 2018 BirjuVachhani (https://github.com/BirjuVachhani)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.birjuvachhani.revix

import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.item_empty_default.view.*

/**
 * Created by Birju Vachhani on 04/12/18.
 */

@DslMarker
annotation class RecyclerAdapterBuilderMarker

@RecyclerAdapterBuilderMarker
@ViewTypeBuilderMarker
class RecyclerAdapterBuilder {

    internal var holderData = mutableMapOf<Int, HolderData>()
    internal var emptyView: SpecialViewTypeBuilder? = null
    internal var errorView: SpecialViewTypeBuilder? = null
    internal var loadingView: SpecialViewTypeBuilder? = null

    inline fun <reified T : BaseModel> addViewType(func: ViewTypeBuilder<T>.() -> Unit) {
        val holder = ViewTypeBuilder<T>().apply {
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

    fun addEmptyView(func: SpecialViewTypeBuilder.() -> Unit) {
        emptyView = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    fun addErrorView(func: SpecialViewTypeBuilder.() -> Unit) {
        errorView = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    fun addLoadingView(func: SpecialViewTypeBuilder.() -> Unit) {
        loadingView = SpecialViewTypeBuilder().apply {
            func()
        }
    }

    private fun <T : BaseModel> addToHolderData(holder: ViewTypeBuilder<T>) {
        @Suppress("UNCHECKED_CAST")
        holderData[holder.modelClass.hashCode()] =
                HolderData(
                    holder.layout,
                    holder as ViewTypeBuilder<BaseModel>
                )
    }

    fun hasEmptyView(): Boolean = emptyView !== null

    fun hasErrorView(): Boolean = errorView !== null

    fun hasLoadingView(): Boolean = loadingView !== null

    @PublishedApi
    internal fun <T : BaseModel> `access$addToHolderData`(holder: ViewTypeBuilder<T>) = addToHolderData(holder)

    fun addDefaultEmptyView(msg: String = "No item found") {
        emptyView = SpecialViewTypeBuilder().apply {
            layout = R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = msg
            }
        }
    }

    fun addDefaultEmptyView(@StringRes msgId: Int) {
        emptyView = SpecialViewTypeBuilder().apply {
            layout = R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = it.itemView.context.getString(msgId)
            }
        }
    }

    fun addDefaultLoadingView() {
        loadingView = SpecialViewTypeBuilder().apply {
            layout = R.layout.item_loading_default
        }
    }
}