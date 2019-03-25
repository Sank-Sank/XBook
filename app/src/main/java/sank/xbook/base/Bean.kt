package sank.xbook.base

class BookBean(var status:Int,
               var bookname:String,
               var booktype:String,
               var book_author:String,
               var book_synopsis:String,
               var book_image:String,
               var update_time:String)

class ChaptersBean(var status:Int,
                   var chapters:List<ChaptersDetailsBean>)

class ChaptersDetailsBean(var id:Int,
                          var chapter:String)

class ChapterContentBean(var status: Int,
                         var content:String)