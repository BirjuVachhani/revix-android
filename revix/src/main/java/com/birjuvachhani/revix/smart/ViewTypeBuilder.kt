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
    internal var layoutId: Int = 0
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
    internal var bindFunc: (holder: BaseVH) -> Unit = {}

    fun bind(func: (holder: BaseVH) -> Unit) {
        this.bindFunc = func
    }

    infix fun from(@LayoutRes id: Int) {
        layoutId = id
    }
}