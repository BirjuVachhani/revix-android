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

package com.birjuvachhani.revix.smart

import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.birjuvachhani.revix.R
import com.birjuvachhani.revix.common.BaseModel
import kotlinx.android.synthetic.main.item_empty_default.view.*
import kotlinx.android.synthetic.main.item_loading_default.view.*

/**
 * Created by Birju Vachhani on 04/12/18.
 */

class RecyclerAdapterBuilder {

    internal var holderData = mutableMapOf<Int, ViewType<BaseModel>>()
    internal var emptyViewType: SpecialViewType = SpecialViewType.Unspecified
    internal var errorViewType: SpecialViewType = SpecialViewType.Unspecified
    internal var loadingViewType: SpecialViewType = SpecialViewType.Unspecified

    inline fun <reified T : BaseModel> addViewType(func: ViewTypeBuilder<T>.() -> Unit) {
        val holder = ViewTypeBuilder<T>().apply {
            func()
            modelClass = T::class.java
        }
        try {
            `access$addToHolderData`(holder)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Cannot cast to ViewTypeBuilder<BaseModel>")
        }
    }

    /**
     * Allows to configure empty view for recycler view
     * */
    fun emptyView(func: SpecialViewTypeBuilder.() -> Unit) {
        emptyViewType = SpecialViewTypeBuilder().apply(func).build()
    }

    /**
     * Enables default empty view
     * @param message is the text that will be displayed when empty view is shown
     * */
    fun emptyView(message: String = "No item found") {
        emptyViewType = SpecialViewType.Raw(R.layout.item_empty_default) {
            it.tvDefaultErrorMessage.text = message
        }
    }

    /**
     * sets an empty view with text retrieved from given resource
     * @param id is the string resource from which empty message text will be retrieved
     * */
    fun emptyView(@StringRes id: Int) {
        emptyViewType = SpecialViewType.Raw(R.layout.item_empty_default) {
            it.tvDefaultErrorMessage.text = it.context.getString(id)
        }
    }

    /**
     * sets an empty view using given view
     * */
    fun emptyView(view: View) {
        emptyViewType = SpecialViewType.Inflated(view)
    }

    /**
     * Allows to configure loading view for recycler view
     * */
    fun loadingView(func: SpecialViewTypeBuilder.() -> Unit) {
        loadingViewType = SpecialViewTypeBuilder().apply(func).build()
    }

    /**
     * sets loading view by applying provided color on ProgressBar
     * */
    fun loadingView(@ColorRes color: Int) {
        loadingViewType = SpecialViewType.Raw(R.layout.item_loading_default) {
            it.progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(it.context, color), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }

    /**
     * sets a loading view with from given animation drawable
     * */
    fun loadingView(drawable: AnimationDrawable) {
        loadingViewType = SpecialViewType.Raw(R.layout.item_loading_default) {
            it.progressBar.indeterminateDrawable = drawable
        }
    }

    /**
     * sets loading view from given view
     * */
    fun loadingView(view: View) {
        loadingViewType = SpecialViewType.Inflated(view)
    }

    /**
     * Allows to configure error view for recycler view
     * */
    fun errorView(func: SpecialViewTypeBuilder.() -> Unit) {
        errorViewType = SpecialViewTypeBuilder().apply(func).build()
    }

    /**
     * Enables default error view
     * @param message is the text that will be displayed when error view is shown
     * */
    fun errorView(message: String = "No item found") {
        errorViewType = SpecialViewType.Raw(R.layout.item_empty_default) {
            it.tvDefaultErrorMessage.text = message
        }
    }

    /**
     * sets an error view with text retrieved from given resource
     * */
    fun errorView(@StringRes id: Int) {
        errorViewType = SpecialViewType.Raw(R.layout.item_empty_default) {
            it.tvDefaultErrorMessage.text = it.context.getString(id)
        }
    }

    /**
     * sets an error view from given view
     * */
    fun errorView(view: View) {
        errorViewType = SpecialViewType.Inflated(view)
    }

    private fun <T : BaseModel> addToHolderData(holder: ViewTypeBuilder<T>) {
        holderData[holder.modelClass.hashCode()] = holder.build()
    }

    fun hasEmptyView(): Boolean = emptyViewType !is SpecialViewType.Unspecified

    fun hasErrorView(): Boolean = errorViewType !is SpecialViewType.Unspecified

    fun hasLoadingView(): Boolean = loadingViewType !is SpecialViewType.Unspecified

    @PublishedApi
    internal fun <T : BaseModel> `access$addToHolderData`(holder: ViewTypeBuilder<T>) = addToHolderData(holder)

    fun loadingView() {
        loadingViewType = SpecialViewTypeBuilder().apply {
            layout from R.layout.item_loading_default
        }.build()
    }
}