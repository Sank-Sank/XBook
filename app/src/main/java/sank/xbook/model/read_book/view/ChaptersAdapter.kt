package sank.xbook.model.read_book.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.ChaptersDetailsBean

class ChaptersAdapter(private var contexts: Context,
                      private var data:MutableList<ChaptersDetailsBean>,
                      private var onItemClickListener: OnItemClickListeners) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mInflate: LayoutInflater = LayoutInflater.from(contexts)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            ChapterHolder(mInflate.inflate(R.layout.chapter_item_layout, p0, false))

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        with(p0 as ChapterHolder){
            chapter.text = data[p1].chapter
            chapterItem.setOnClickListener{ v -> onItemClickListener.onItemClicked(v,p1) }
        }
    }

    inner class ChapterHolder(view:View): RecyclerView.ViewHolder(view){
        var chapterItem:LinearLayout = view.findViewById(R.id.chapterItem)
        val chapter:TextView = view.findViewById(R.id.chapter)
    }
}