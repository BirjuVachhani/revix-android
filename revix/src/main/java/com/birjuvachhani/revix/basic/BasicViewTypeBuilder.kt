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

package com.birjuvachhani.revix.basic

import android.view.View
import androidx.annotation.LayoutRes
import com.birjuvachhani.revix.smart.isValidRes

class BasicViewTypeBuilder<T> {

    @LayoutRes
    internal var layoutId: Int = 0
    private var bindFunc: (t: T, view: View) -> Unit = { _, _ -> }
    private var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    private var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }

    val layout = this

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    fun bind(func: (model: T, view: View) -> Unit) {
        this.bindFunc = func
    }

    fun onClick(func: (view: View, model: T, position: Int) -> Unit) {
        this.clickFunc = func
    }

    fun filter(func: (model: T, search: String) -> Boolean) {
        this.filterFunc = func
    }

    internal fun build(): BasicViewType<T> {
        return when {
            layoutId.isValidRes() -> BasicViewType.Specified(false, true, layoutId, bindFunc, clickFunc, filterFunc)
            else -> throw Exception("No layout is specified for specified type")
        }
    }
}

sealed class BasicViewType<out T> {
    data class Specified<T>(
        val temp: Boolean,
        val temp2: Boolean,
        @LayoutRes val layoutId: Int,
        val bindFunc: (t: T, view: View) -> Unit,
        val clickFunc: (view: View, model: T, position: Int) -> Unit,
        val filterFunc: (model: T, search: String) -> Boolean
    ) : BasicViewType<T>()

    class Unspecified<T> : BasicViewType<T>()
}