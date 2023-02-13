package com.example.coroutine

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MainAdapter:BaseQuickAdapter<MainBean,BaseViewHolder>(R.layout.item_main) {
    override fun convert(holder: BaseViewHolder, item: MainBean) {
        holder.getView<TextView>(R.id.tv_item).text = item.title
    }
}