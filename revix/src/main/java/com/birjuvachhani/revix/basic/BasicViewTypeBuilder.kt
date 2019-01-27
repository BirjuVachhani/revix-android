package com.birjuvachhani.revix.basic

import android.view.View
import androidx.annotation.LayoutRes
import com.birjuvachhani.revix.common.BaseVH

class BasicViewTypeBuilder<T> {

    @LayoutRes
    internal var layoutId: Int = 0
    //    lateinit var modelClass: Class<T>
    internal var bindFunc: (t: T, holder: BaseVH) -> Unit = { _, _ -> }
    internal var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    internal var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }

    val layout = this

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }

    fun bind(func: (model: T, holder: BaseVH) -> Unit) {
        this.bindFunc = func
    }

    fun onClick(func: (view: View, model: T, position: Int) -> Unit) {
        this.clickFunc = func
    }

    fun filter(func: (model: T, search: String) -> Boolean) {
        this.filterFunc = func
    }
}