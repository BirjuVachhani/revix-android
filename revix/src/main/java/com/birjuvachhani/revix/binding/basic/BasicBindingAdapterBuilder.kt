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

import android.graphics.drawable.AnimationDrawable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.birjuvachhani.revix.R
import com.birjuvachhani.revix.binding.SpecialBindingViewType
import com.birjuvachhani.revix.binding.SpecialBindingViewTypeBuilder
import kotlinx.android.synthetic.main.item_empty_default.view.*
import kotlinx.android.synthetic.main.item_loading_default.view.*

class BasicBindingAdapterBuilder<T> {

    internal var emptyViewType: SpecialBindingViewType = SpecialBindingViewType.Unspecified
    internal var errorViewType: SpecialBindingViewType = SpecialBindingViewType.Unspecified
    internal var loadingViewType: SpecialBindingViewType = SpecialBindingViewType.Unspecified
    var itemViewType: BasicBindingViewType<T> = BasicBindingViewType.Unspecified()

    fun setViewType(br: Int, func: BasicBindingViewTypeBuilder<T>.() -> Unit) {
        itemViewType = BasicBindingViewTypeBuilder<T>().apply {
            this.br = br
            func()
        }.build()
    }

    /**
     * Allows to configure empty view for recycler view
     * */
    fun emptyView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        emptyViewType = SpecialBindingViewTypeBuilder().apply(func).build()
    }

    /**
     * Enables default empty view
     * @param message is the text that will be displayed when empty view is shown
     * */
    fun emptyView(message: String = "No item found") {
        emptyViewType = SpecialBindingViewType.Raw(R.layout.item_empty_default) {
            it.root.tvDefaultErrorMessage.text = message
        }
    }

    /**
     * sets an empty view with text retrieved from given resource
     * @param id is the string resource from which empty message text will be retrieved
     * */
    fun emptyView(@StringRes id: Int) {
        emptyViewType = SpecialBindingViewType.Raw(R.layout.item_empty_default) {
            it.root.tvDefaultErrorMessage.text = it.root.context.getString(id)
        }
    }

    /**
     * sets an empty view using given binding
     * */
    fun emptyView(mBinding: ViewDataBinding) {
        emptyViewType = SpecialBindingViewType.Inflated(mBinding)
    }

    /**
     * Allows to configure loading view for recycler view
     * */
    fun loadingView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        loadingViewType = SpecialBindingViewTypeBuilder().apply(func).build()
    }

    /**
     * sets loading view by applying provided color on ProgressBar
     * */
    fun loadingView(@ColorRes color: Int) {
        loadingViewType = SpecialBindingViewType.Raw(R.layout.item_loading_default) {
            it.root.progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(it.root.context, color), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }

    /**
     * sets a loading view with from given animation drawable
     * */
    fun loadingView(drawable: AnimationDrawable) {
        loadingViewType = SpecialBindingViewType.Raw(R.layout.item_loading_default) {
            it.root.progressBar.indeterminateDrawable = drawable
        }
    }

    /**
     * sets loading view from given view
     * */
    fun loadingView(mBinding: ViewDataBinding) {
        loadingViewType = SpecialBindingViewType.Inflated(mBinding)
    }

    /**
     * Allows to configure error view for recycler view
     * */
    fun errorView(func: SpecialBindingViewTypeBuilder.() -> Unit) {
        errorViewType = SpecialBindingViewTypeBuilder().apply(func).build()
    }

    /**
     * Enables default error view
     * @param message is the text that will be displayed when error view is shown
     * */
    fun errorView(message: String = "No item found") {
        errorViewType = SpecialBindingViewType.Raw(R.layout.item_empty_default) {
            it.root.tvDefaultErrorMessage.text = message
        }
    }

    /**
     * sets an error view with text retrieved from given resource
     * */
    fun errorView(@StringRes id: Int) {
        errorViewType = SpecialBindingViewType.Raw(R.layout.item_empty_default) {
            it.root.tvDefaultErrorMessage.text = it.root.context.getString(id)
        }
    }

    /**
     * sets an error view from given view
     * */
    fun errorView(mBinding: ViewDataBinding) {
        errorViewType = SpecialBindingViewType.Inflated(mBinding)
    }

    fun hasEmptyView(): Boolean = emptyViewType !is SpecialBindingViewType.Unspecified

    fun hasErrorView(): Boolean = errorViewType !is SpecialBindingViewType.Unspecified

    fun hasLoadingView(): Boolean = loadingViewType !is SpecialBindingViewType.Unspecified
}