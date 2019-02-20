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

import android.view.View
import androidx.annotation.LayoutRes
import com.birjuvachhani.revix.common.BaseModel
import com.birjuvachhani.revix.common.BaseVH

/**
 * Created by Birju Vachhani on 04/12/18.
 */

class ViewTypeBuilder<T : BaseModel> {
    val layout = this
    @LayoutRes
    internal var layoutId: Int = -1
    lateinit var modelClass: Class<T>
    internal var bindFunc: (t: T, holder: BaseVH) -> Unit = { _, _ -> }
    internal var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    internal var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }

    fun bind(func: (model: T, holder: BaseVH) -> Unit) {
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
}

class SpecialViewTypeBuilder {

    val layout = this
    @LayoutRes
    internal var layoutId: Int = 0
    internal var bindFunc: (view: View) -> Unit = {}
    internal var view: View? = null

    fun bind(func: (view: View) -> Unit) {
        this.bindFunc = func
    }

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    internal fun build(): SpecialViewType {
        val v = view
        return when {
            v != null -> SpecialViewType.Inflated(v)
            layoutId.isValidRes() -> SpecialViewType.Raw(layoutId, bindFunc)
            else -> throw Exception("No view configuration is provided for Special View. Layout res or view is missing")
        }
    }
}

sealed class SpecialViewType {
    data class Inflated(val view: View) : SpecialViewType()
    data class Raw(@LayoutRes val layoutId: Int, val bindFunc: (view: View) -> Unit) : SpecialViewType()
    object Unspecified : SpecialViewType()
}

fun Int.isValidRes(): Boolean = this != -1 && this != 0