package sank.xbook.model.read_book.page

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.ChapterContentBean

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/25
 */



interface RequestChapterAPI{
    @GET("chaptercontent")
    fun getChapter(@Query("id")
                   id:Int) : Call<ChapterContentBean>
}

