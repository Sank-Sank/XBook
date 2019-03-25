package sank.xbook.base

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class BookBean(var status:Int,
               var bookname:String,
               var booktype:String,
               var book_author:String,
               var book_synopsis:String,
               var book_image:String,
               var update_time:String)

data class ChaptersBean(var status:Int,
                   var chapters:List<ChaptersDetailsBean>)

data class ChaptersDetailsBean(var id:Int,
                          var chapter:String)

data class ChapterContentBean(var status: Int,
                         var content:String){
    override fun toString(): String {
        return "status = $status , content = $content"
    }
}

