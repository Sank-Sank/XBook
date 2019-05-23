package sank.xbook.base

import java.io.Serializable


data class BookBean(var status:Int,
                   var bookname:String,
                   var booktype:String,
                   var book_author:String,
                   var book_synopsis:String,
                   var book_image:String,
                   var update_time:String, var message:String?) : Serializable

data class ChaptersBean(var status:Int,
                   var chapters:List<ChaptersDetailsBean>)

data class ChaptersDetailsBean(var id:Int,
                          var chapter:String)

data class ChapterContentBean(var status: Int,
                              var title:String,
                         var content:String){
    override fun toString(): String {
        return "status = $status , content = $content"
    }
}

/**
 * 单页实例
 */
class TxtPage {
    var position: Int = 0
    var title: String? = null
    var titleLines: Int = 0         //当前 lines 中为 title 的行数。
    var lines: MutableList<String>? = null

    override fun toString(): String {
        return "position = $position , title = $title  , titleLines = $titleLines , lines = ${lines.toString()}"
    }
}

class TypeBean1(var status:Int,
               var count:Int,
               var pages:List<TypeBean2>)

class TypeBean2(var model:String,
                var pk:Int,
                var fields:TypeBean3)

class TypeBean3(var book_name:String,
                var book_type:String,
                var book_author:String,
                var book_synopsis:String,
                var book_image:String,
                var update_time:String)

data class BookMallBean1(var status: Int,
                         var data:List<BookMallBean2>)

data class BookMallBean2(var tag:Int,
                         var bookList:List<BookBean>?,
                         var imageList:List<String>?)

data class BookRackBean1(var status: Int,
                         var books:List<BookRackBean2>?)

data class BookRackBean2(var name:String,
                         var author:String,
                         var synopsis:String,
                         var image:String,
                         var update_time:String)