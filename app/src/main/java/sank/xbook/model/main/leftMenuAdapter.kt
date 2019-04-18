package sank.xbook.model.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/14
 */
class leftMenuAdapter (private var contexts:Context,
                       private var data:MutableList<String>,
                       private var onItemClickListener: OnItemClickListeners) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mInflate: LayoutInflater = LayoutInflater.from(contexts)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            leftMenuHolder(mInflate.inflate(R.layout.item_left_menu_layout, p0, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        with(p0 as leftMenuHolder){
            item.setOnClickListener { v -> onItemClickListener.onItemClicked(v,p1) }
            name.text = data[p1]
        }
    }

    inner class leftMenuHolder(view:View) : RecyclerView.ViewHolder(view){
        var item:LinearLayout = view.findViewById(R.id.item)
        var name:TextView = view.findViewById(R.id.name)
    }
}