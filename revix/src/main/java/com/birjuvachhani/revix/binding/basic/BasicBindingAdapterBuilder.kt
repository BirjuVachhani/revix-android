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

package com.birjuvachhani.revix.binding.basic

import androidx.annotation.StringRes
import com.birjuvachhani.revix.R
import com.birjuvachhani.revix.binding.SpecialViewTypeBindingBuilder
import kotlinx.android.synthetic.main.item_empty_default.view.*

class BasicBindingAdapterBuilder<T> {

    internal var emptyViewBuilder: SpecialViewTypeBindingBuilder? = null
    internal var errorViewBuilder: SpecialViewTypeBindingBuilder? = null
    internal var loadingViewBuilder: SpecialViewTypeBindingBuilder? = null
    var viewBuilder: BasicBindingViewTypeBuilder<T>? = null

    inline fun setViewType(br: Int, func: BasicBindingViewTypeBuilder<T>.() -> Unit) {
        viewBuilder = BasicBindingViewTypeBuilder<T>().apply { func() }
        viewBuilder?.br = br
    }

    fun addEmptyView(func: SpecialViewTypeBindingBuilder.() -> Unit) {
        emptyViewBuilder = SpecialViewTypeBindingBuilder().apply {
            func()
        }
    }

    fun addErrorView(func: SpecialViewTypeBindingBuilder.() -> Unit) {
        errorViewBuilder = SpecialViewTypeBindingBuilder().apply {
            func()
        }
    }

    fun addLoadingView(func: SpecialViewTypeBindingBuilder.() -> Unit) {
        loadingViewBuilder = SpecialViewTypeBindingBuilder().apply {
            func()
        }
    }

    fun hasEmptyView(): Boolean = emptyViewBuilder !== null

    fun hasErrorView(): Boolean = errorViewBuilder !== null

    fun hasLoadingView(): Boolean = loadingViewBuilder !== null

    fun addDefaultEmptyView(msg: String = "No item found") {
        emptyViewBuilder = SpecialViewTypeBindingBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = msg
            }
        }
    }

    fun addDefaultEmptyView(@StringRes msgId: Int) {
        emptyViewBuilder = SpecialViewTypeBindingBuilder().apply {
            layout from R.layout.item_empty_default
            bind {
                it.itemView.tvDefaultErrorMessage.text = it.itemView.context.getString(msgId)
            }
        }
    }

    fun addDefaultLoadingView() {
        loadingViewBuilder = SpecialViewTypeBindingBuilder().apply {
            layout from R.layout.item_loading_default
        }
    }
}