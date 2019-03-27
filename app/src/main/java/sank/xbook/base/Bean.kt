package sank.xbook.base


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
                              var title:String,
                         var content:String){
    override fun toString(): String {
        return "status = $status , content = $content"
    }
}

class TxtPage {
    var position: Int = 0
    var title: String? = null
    var titleLines: Int = 0         //当前 lines 中为 title 的行数。
    var lines: MutableList<String>? = null

    override fun toString(): String {
        return "position = $position , title = $title  , titleLines = $titleLines , lines = ${lines.toString()}"
    }
}