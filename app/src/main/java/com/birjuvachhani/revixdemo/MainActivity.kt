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

package com.birjuvachhani.revixdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.birjuvachhani.revix.common.BaseModel
import com.birjuvachhani.revix.smart.RVAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_video.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* val adapter: RVBindingAdapter =
             RVBindingAdapter {
                 addViewType<ChatImageModel> {
                     layout from R.layout.item_image
                     bind { model, holder ->
                         holder.itemView.tvImageText.text = model.imageTitle
                     }
                     onClick { view, model, position ->
                         Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
                     }
                     filter { model, search ->
                         model.imageTitle.contains(search)
                     }
                 }
                 addViewType<ChatVideoModel> {
                     layout from R.layout.item_video
                     bind { model, holder ->
                         holder.itemView.tvVideoText.text = model.videoTitle
                     }
                     onClick { view, model, position ->
                         Toast.makeText(this@MainActivity, model.videoTitle, Toast.LENGTH_SHORT).show()
                     }
                     filter { model, search ->
                         model.videoTitle.contains(search)
                     }
                 }
                 addEmptyView {
                     layout from R.layout.item_empty
                     bind { holder ->
                         // Empty View Binding
                         holder.itemView.tvEmpty.text = "Nothing found"
                     }
                 }
                 addLoadingView {
                     layout from R.layout.item_empty
                     bind {
                         it.itemView.tvEmpty.text = "Loading"
                     }
                 }
                 addErrorView {
                     layout from R.layout.item_empty
                     bind {
                         it.itemView.tvEmpty.text = "Something went wrong"
                     }
                 }
                 addDefaultEmptyView("Default Empty")
                 addDefaultLoadingView()
             }*/

/*        val basicAdapter = BasicAdapter<ChatImageModel> {
            setViewType {
                layout from R.layout.item_image
                bind { model, view ->
                    view.tvImageText.text = model.imageTitle
                }

                onClick { view, model, position ->
                    Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
                }
            }
            emptyViewType(R.string.no_items_found)
            loadingViewType(android.R.color.holo_red_dark)
            errorViewType("Error!")
        }
        val basicAdapter = BasicBindingAdapter<ChatImageModel> {
            setViewType(BR.image) {
                layout from R.layout.item_image

                onClick { _, model, _ ->
                    Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
                }
            }
            emptyView(R.string.no_items_found)
            loadingView(android.R.color.holo_red_dark)
            errorView("Error!")
        }*/


        val list = ArrayList<BaseModel>()
        list.add(ChatImageModel())
        list.add(ChatVideoModel())
        list.add(ChatImageModel("Image 1"))
        list.add(ChatImageModel("Image 2"))
        list.add(ChatVideoModel("Video 1"))
        list.add(ChatVideoModel("Video 2"))
        list.add(ChatVideoModel("Video 3"))
        list.add(ChatImageModel("Image 3"))

//        val adapter = RVBindingAdapter {
//            addViewType<ChatImageModel>(BR.image, R.layout.item_image) {
//                onClick { view, model, position ->
//                    Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
//                }
//            }
//            addViewType<ChatVideoModel>(BR.video, R.layout.item_video) {
//                onClick { view, model, position ->
//                    Toast.makeText(this@MainActivity, model.videoTitle, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

//        val adapter2 = BasicBindingAdapter<ChatImageModel> {
//            setViewType(BR.image) {
//                layout from R.layout.item_image
//                onClick { _, model, _ ->
//                    Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        val basicAdapter = RVAdapter {
            addViewType<ChatImageModel> {
                layout from R.layout.item_image
                bind { model, view ->
                    view.tvImageText.text = model.imageTitle
                }

                onClick { _, model, _ ->
                    Toast.makeText(this@MainActivity, model.imageTitle, Toast.LENGTH_SHORT).show()
                }
            }
            addViewType<ChatVideoModel> {
                layout from R.layout.item_video
                bind { model, view ->
                    view.tvVideoText.text = model.videoTitle
                }

                onClick { _, model, _ ->
                    Toast.makeText(this@MainActivity, model.videoTitle, Toast.LENGTH_SHORT).show()
                }
            }
            loadingView(android.R.color.holo_orange_dark)
        }
        rvList.adapter = basicAdapter
        basicAdapter.setData(
            arrayListOf(
                ChatImageModel("Image 1"),
                ChatImageModel("Image 2"),
                ChatImageModel("Image 3"),
                ChatVideoModel("Video 1"),
                ChatVideoModel("Video 2"),
                ChatVideoModel("Video 3")
            )
        )
        btnRefresh.setOnClickListener {
            basicAdapter.setLoading()
        }
//        adapter.setData(list)
    }

}