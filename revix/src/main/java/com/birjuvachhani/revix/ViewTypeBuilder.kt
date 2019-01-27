package com.birjuvachhani.revix

import android.view.View
import androidx.annotation.LayoutRes

/**
 * Created by Birju Vachhani on 04/12/18.
 */

class ViewTypeBuilder<T : BaseModel> {

    @LayoutRes
    var layout: Int = 0
    lateinit var modelClass: Class<T>
    var bindFunc: (t: T, holder: BaseVH) -> Unit = { _, _ -> }
    var clickFunc: (view: View, model: T, position: Int) -> Unit = { _, _, _ -> }
    var filterFunc: (model: T, search: String) -> Boolean = { _, _ -> false }

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

class SpecialViewTypeBuilder {

    @LayoutRes
    var layout: Int = 0
    var bindFunc: (holder: BaseVH) -> Unit = {}

    fun bind(func: (holder: BaseVH) -> Unit) {
        this.bindFunc = func
    }
}