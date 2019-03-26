package sank.xbook.model.read_book.page;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sank.xbook.base.ChapterContentBean;
import sank.xbook.base.ChaptersDetailsBean;
import sank.xbook.base.TxtPage;

/**
 * Created by newbiechen on 17-5-29.
 * 网络页面加载器
 */

public class NetPageLoader extends PageLoader{

    public NetPageLoader(PageView pageView) {
        super(pageView);
    }

    //初始化书籍
    @Override
    public void openBook(List<ChaptersDetailsBean> chapters){
        super.openBook(chapters);
//        isBookOpen = false;
        //if (collBook.getBookChapters() == null) return;
//        mChapterList = convertTxtChapter(collBook.getChapters());

        //设置目录回调
        if (mPageChangeListener != null){
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
        //提示加载下面的章节
        //loadCurrentChapter();
    }

//    private List<TxtChapter> convertTxtChapter(List<ChapterContentBean> bookChapters){
//        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
//        for (ChapterContentBean bean : bookChapters){
//            TxtChapter chapter = new TxtChapter();
////            chapter.bookId = bean.getBookId();
////            chapter.title = bean.getTitle();
////            chapter.link = bean.getLink();
//            txtChapters.add(chapter);
//        }
//        return txtChapters;
//    }

    String contents = "";

    @Nullable
    @Override
    protected List<TxtPage> loadPageList(ChaptersDetailsBean chapter) {
        if (mChapterList == null) {
            throw new IllegalArgumentException("chapter list must not null");
        }
//
//        //获取要加载的文件
//        TxtChapter txtChapter = mChapterList.get(chapter);
//        File file = new File(File.separator + mChapterList.get(chapter).title + ".wy");
//        if (!file.exists()) return null;
//
//        Reader reader = null;
//        try {
//            reader = new FileReader(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader br = new BufferedReader(reader);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        RequestChapterAPI api = retrofit.create(RequestChapterAPI.class);
        Log.e("TAG", "id == " + chapter.getId());
        Call<ChapterContentBean> call = api.getChapter(chapter.getId());
        call.enqueue(new Callback<ChapterContentBean>() {
            @Override
            public void onResponse(@NonNull Call<ChapterContentBean> call, @NonNull Response<ChapterContentBean> response) {
                contents = response.body().getContent();
            }

            @Override
            public void onFailure(@NonNull Call<ChapterContentBean> call, @NonNull Throwable t) {
                Log.e("TAG","请求网络失败");
            }
        });
//
//        try {
//            URL url = new URL("http://www.xyxhome.cn/book/");
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        return loadPages(chapter.getChapter(), contents);
    }


    //装载上一章节的内容
    @Override
    boolean prevChapter(){

        boolean hasPrev = super.prevChapter();
        if (!hasPrev) return false;

        if (mStatus == STATUS_FINISH){
            loadCurrentChapter();
            return true;
        }
        else if (mStatus == STATUS_LOADING){
            loadCurrentChapter();
            return false;
        }
        return false;
    }

    //装载下一章节的内容
    @Override
    boolean nextChapter(){
        boolean hasNext = super.nextChapter();
        if (!hasNext) return false;

        if (mStatus == STATUS_FINISH){
            loadNextChapter();
            return true;
        }
        else if (mStatus == STATUS_LOADING){
            loadCurrentChapter();
            return false;
        }
        return false;
    }

    //跳转到指定章节
    public void skipToChapter(int pos){
        super.skipToChapter(pos);

        //提示章节改变，需要下载
        loadCurrentChapter();
    }

    private void loadPrevChapter(){
        //提示加载上一章
        if (mPageChangeListener != null){
            //提示加载前面3个章节（不包括当前章节）
            int current = mCurChapterPos;
            int prev = current - 3;
            if (prev < 0){
                prev = 0;
            }
            mPageChangeListener.onLoadChapter(mChapterList.subList(prev,current),mCurChapterPos);
        }
    }

    private void loadCurrentChapter(){
        if (mPageChangeListener != null){
            List<ChaptersDetailsBean> bookChapters = new ArrayList<>(5);
            //提示加载当前章节和前面两章和后面两章
            int current = mCurChapterPos;
            bookChapters.add(mChapterList.get(current));

            //如果当前已经是最后一章，那么就没有必要加载后面章节
            if (current != mChapterList.size()){
                int begin = current + 1;
                int next = begin + 2;
                if (next > mChapterList.size()){
                    next = mChapterList.size();
                }
                bookChapters.addAll(mChapterList.subList(begin,next));
            }

            //如果当前已经是第一章，那么就没有必要加载前面章节
            if (current != 0){
                int prev = current - 2;
                if (prev < 0){
                    prev = 0;
                }
                bookChapters.addAll(mChapterList.subList(prev,current));
            }
            mPageChangeListener.onLoadChapter(bookChapters,mCurChapterPos);
        }
    }

    private void loadNextChapter(){
        //提示加载下一章
        if (mPageChangeListener != null){
            //提示加载当前章节和后面3个章节
            int current = mCurChapterPos + 1;
            int next = mCurChapterPos + 3;
            if (next > mChapterList.size()){
                next = mChapterList.size();
            }
            mPageChangeListener.onLoadChapter(mChapterList.subList(current,next),mCurChapterPos);
        }
    }

    @Override
    public void setChapterList(List<ChapterContentBean> bookChapters) {
        if (bookChapters == null) return;

        //mChapterList = convertTxtChapter(bookChapters);

        if (mPageChangeListener != null){
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

//    @Override
//    public void saveRecord() {
//        super.saveRecord();
//        if (mCollBook != null && isBookOpen){
//            //表示当前CollBook已经阅读
//            mCollBook.setUpdate(false);
//            mCollBook.setLastRead(StringUtils.
//                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
//            //直接更新
//            CollBookHelper.getsInstance().saveBook(mCollBook);
//            BookRepository.getInstance()
//                    .saveCollBook(mCollBook);
//        }
//    }
}

