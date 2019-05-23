package sank.xbook.model.main.cache

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import sank.xbook.R
import sank.xbook.database.Books

class CacheAdapter(private var contexts:Context,private var data:List<Books>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            CacheHolder(LayoutInflater.from(contexts).inflate(R.layout.cacha_item_layout,p0,false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    inner class CacheHolder(view:View): RecyclerView.ViewHolder(view){
        var bookName:TextView = view.findViewById(R.id.bookName)
        var progress:ProgressBar = view.findViewById(R.id.progress)
    }
}