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

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.birjuvachhani.revix.smart.isValidRes

class BasicBindingViewTypeBuilder<T> {

    @LayoutRes
    internal var layoutId: Int = -1
    private var bindFunc: (t: T, mBinding: ViewDataBinding) -> Unit = { _, _ -> }
    private var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    private var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }
    private var itemIdFunc: (position: Int) -> Long = { 0 }
    var br: Int = -1

    val layout = this

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    fun bind(func: (model: T, mBinding: ViewDataBinding) -> Unit) {
        this.bindFunc = func
    }

    fun onClick(func: (view: View, model: T, position: Int) -> Unit) {
        this.clickFunc = func
    }

    fun filter(func: (model: T, search: String) -> Boolean) {
        this.filterFunc = func
    }

    fun itemId(func: (position: Int) -> Long) {
        this.itemIdFunc = func
    }

    internal fun build(): BasicBindingViewType<T> {
        return when {
            layoutId.isValidRes() && br.isValidRes() -> BasicBindingViewType.Specified(
                layoutId = layoutId,
                variable = br,
                bindFunc = bindFunc,
                clickFunc = clickFunc,
                filterFunc = filterFunc,
                itemIdFunc = itemIdFunc
            )
            else -> throw Exception("No Binding variable/No layout is specified for specified type")
        }
    }
}

sealed class BasicBindingViewType<out T> {
    data class Specified<T>(
        @LayoutRes val layoutId: Int,
        val variable: Int,
        val bindFunc: (t: T, mBinding: ViewDataBinding) -> Unit,
        val clickFunc: (view: View, model: T, position: Int) -> Unit,
        val filterFunc: (model: T, search: String) -> Boolean,
        val itemIdFunc: (position: Int) -> Long
    ) : BasicBindingViewType<T>()

    class Unspecified<T> : BasicBindingViewType<T>()
}