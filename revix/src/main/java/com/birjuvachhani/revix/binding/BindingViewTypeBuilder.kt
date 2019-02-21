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

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.birjuvachhani.revix.common.BaseModel
import com.birjuvachhani.revix.smart.isValidRes

/**
 * Created by Birju Vachhani on 04/12/18.
 */

class ViewTypeBindingBuilder<T : BaseModel> {
    val layout = this
    @LayoutRes
    internal var layoutId: Int = -1
    lateinit var modelClass: Class<T>
    internal var bindFunc: (t: T, mBinding: ViewDataBinding) -> Unit = { _, _ -> }
    internal var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    internal var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }
    var br: Int = -1

    fun bind(func: (model: T, mBinding: ViewDataBinding) -> Unit) {
        this.bindFunc = func
    }

    fun onClick(func: (view: View, model: T, position: Int) -> Unit) {
        this.clickFunc = func
    }

    fun filter(func: (model: T, search: String) -> Boolean) {
        this.filterFunc = func
    }

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    internal fun build(): BindingViewType<T> {
        return when {
            layoutId.isValidRes() -> BindingViewType.Specified(layoutId, br, bindFunc, clickFunc, filterFunc)
            else -> throw Exception("No layout is specified for specified type")
        }
    }
}

sealed class BindingViewType<out T> {
    data class Specified<T>(
        @LayoutRes val layoutId: Int,
        val variable: Int,
        val bindFunc: (t: T, mBinding: ViewDataBinding) -> Unit,
        val clickFunc: (view: View, model: T, position: Int) -> Unit,
        val filterFunc: (model: T, search: String) -> Boolean
    ) : BindingViewType<T>()

    class Unspecified<T> : BindingViewType<T>()
}

class SpecialBindingViewTypeBuilder {

    val layout = this
    @LayoutRes
    internal var layoutId: Int = 0
    internal var bindFunc: (mBinding: ViewDataBinding) -> Unit = {}
    internal var mBinding: ViewDataBinding? = null

    fun bind(func: (mBinding: ViewDataBinding) -> Unit) {
        this.bindFunc = func
    }

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    internal fun build(): SpecialBindingViewType {
        val v = mBinding
        return when {
            v != null -> SpecialBindingViewType.Inflated(v)
            layoutId.isValidRes() -> SpecialBindingViewType.Raw(layoutId, bindFunc)
            else -> throw Exception("No view configuration is provided for Special View. Layout res or view is missing")
        }
    }
}

sealed class SpecialBindingViewType {
    data class Inflated(val mBinding: ViewDataBinding) : SpecialBindingViewType()
    data class Raw(@LayoutRes val layoutId: Int, val bindFunc: (mBinding: ViewDataBinding) -> Unit) :
        SpecialBindingViewType()

    object Unspecified : SpecialBindingViewType()
}

fun Int.isValidRes(): Boolean = this != -1 && this != 0