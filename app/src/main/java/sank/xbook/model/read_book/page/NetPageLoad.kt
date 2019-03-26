package sank.xbook.model.read_book.page

import android.graphics.*
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import sank.xbook.R
import sank.xbook.base.ChapterContentBean
import sank.xbook.base.ChaptersDetailsBean
import sank.xbook.base.MyApp
import sank.xbook.base.TxtPage
import sank.xbook.model.read_book.ReadSettingManager
import sank.xbook.model.read_book.ScreenUtils
import sank.xbook.model.read_book.StringUtils
import java.lang.ref.WeakReference
import java.util.*

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/26
 */
interface IPageView{
    fun onSuccess(chapterContent: ChapterContentBean)
    fun onFailure()
}

class NetPageLoad(pageView: PageView) : IPageView{
    companion object {
        //当前页面的状态
        const val STATUS_LOADING = 1  //正在加载
        const val STATUS_FINISH = 2   //加载完成
        const val STATUS_ERROR = 3    //加载错误 (一般是网络加载情况)
        const val STATUS_EMPTY = 4    //空数据
        const val STATUS_PARSE = 5    //正在解析 (一般用于本地数据加载)
        const val STATUS_PARSE_ERROR = 6 //本地文件解析错误(暂未被使用)

        internal val DEFAULT_MARGIN_HEIGHT = 28
        internal val DEFAULT_MARGIN_WIDTH = 12

        private val DEFAULT_TIP_SIZE = 12
        private val EXTRA_TITLE_SIZE = 4
    }

    //当前章节列表
    internal var mChapterList: List<ChaptersDetailsBean>? = null
    //页面显示类
    private var mPageView: PageView? = null
    //当前显示的页
    private lateinit var mCurPage: TxtPage
    //上一章的页面列表缓存
    private var mWeakPrePageList: WeakReference<List<TxtPage>>? = null
    //当前章节的页面列表
    private var mCurPageList: List<TxtPage>? = null
    //下一章的页面列表缓存
    private var mNextPageList: List<TxtPage>? = null

    //下一页绘制缓冲区，用户缓解卡顿问题。
    private var mNextBitmap: Bitmap? = null

    //绘制电池的画笔
    private lateinit var mBatteryPaint: Paint
    //绘制提示的画笔
    private lateinit var mTipPaint: Paint
    //绘制标题的画笔
    private lateinit var mTitlePaint: Paint
    //绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private lateinit var mBgPaint: Paint
    //绘制小说内容的画笔
    private lateinit var mTextPaint: TextPaint
    //阅读器的配置选项
    private lateinit var mSettingManager: ReadSettingManager
    //被遮盖的页，或者认为被取消显示的页
    private lateinit var mCancelPage: TxtPage

    //当前的状态
    protected var mStatus:Int = STATUS_LOADING
    //当前章
    protected var mCurChapterPos = 0

    //上一章的记录
    private var mLastChapter = 0
    //书籍绘制区域的宽高
    private var mVisibleWidth: Int = 0
    private var mVisibleHeight: Int = 0
    //应用的宽高
    private var mDisplayWidth: Int = 0
    private var mDisplayHeight: Int = 0
    //间距
    private var mMarginWidth: Int = 0
    private var mMarginHeight: Int = 0
    //字体的颜色
    private var mTextColor: Int = 0
    //标题的大小
    private var mTitleSize: Int = 0
    //字体的大小
    private var mTextSize: Int = 0
    //行间距
    private var mTextInterval: Int = 0
    //标题的行间距
    private var mTitleInterval: Int = 0
    //段落距离(基于行间距的额外距离)
    private var mTextPara: Int = 0
    private var mTitlePara: Int = 0
    //电池的百分比
    private var mBatteryLevel: Int = 0
    //页面的翻页效果模式
    private var mPageMode: Int = 0
    //加载器的颜色主题
    private var mBgTheme: Int = 0
    //当前页面的背景
    private var mPageBg: Int = 0
    //当前是否是夜间模式
    private var isNightMode: Boolean = false
    //网络请求
    private var iRequestChapterContent : IRequestChapterContent? = null

    init {
        mPageView = pageView
        //初始化数据
        initData()
        //初始化画笔
        initPaint()
        //初始化PageView
        initPageView()
    }

    private fun initData() {
        mSettingManager = ReadSettingManager.getInstance()
        mTextSize = mSettingManager.textSize
        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE)
        mPageMode = mSettingManager.pageMode
        isNightMode = mSettingManager.isNightMode
        mBgTheme = mSettingManager.readBgTheme

        if (isNightMode) {
            setBgColor(ReadSettingManager.NIGHT_MODE)
        } else {
            setBgColor(mBgTheme)
        }

        //初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH)
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT)
        mTextInterval = mTextSize / 2
        mTitleInterval = mTitleSize / 2
        mTextPara = mTextSize //段落间距由 text 的高度决定。
        mTitlePara = mTitleSize
    }

    private fun initPaint() {
        //绘制提示的画笔
        mTipPaint = Paint()
        mTipPaint.color = mTextColor
        mTipPaint.textAlign = Paint.Align.LEFT//绘制的起始点
        mTipPaint.textSize = ScreenUtils.spToPx(DEFAULT_TIP_SIZE).toFloat()//Tip默认的字体大小
        mTipPaint.isAntiAlias = true
        mTipPaint.isSubpixelText = true

        //绘制页面内容的画笔
        mTextPaint = TextPaint()
        mTextPaint.color = mTextColor
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.isAntiAlias = true

        //绘制标题的画笔
        mTitlePaint = TextPaint()
        mTitlePaint.color = mTextColor
        mTitlePaint.textSize = mTitleSize.toFloat()
        mTitlePaint.style = Paint.Style.FILL_AND_STROKE
        mTitlePaint.typeface = Typeface.DEFAULT_BOLD
        mTitlePaint.isAntiAlias = true

        //绘制背景的画笔
        mBgPaint = Paint()
        mBgPaint.color = mPageBg

        mBatteryPaint = Paint()
        mBatteryPaint.isAntiAlias = true
        mBatteryPaint.isDither = true
        if (isNightMode) {
            mBatteryPaint.color = Color.WHITE
        } else {
            mBatteryPaint.color = Color.BLACK
        }
    }

    private fun initPageView() {
        //配置参数
        mPageView?.setPageMode(mPageMode)
        mPageView?.setBgColor(mPageBg)
        iRequestChapterContent = RequestChapterContent(this)
    }

    /**
     * 绘制背景的类
     */
    public fun setBgColor(theme: Int) {
        if (isNightMode && theme == ReadSettingManager.NIGHT_MODE) {
            mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_fff_99)
            mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.black)
        } else if (isNightMode) {
            mBgTheme = theme
            mSettingManager.setReadBackground(theme)
        } else {
            mSettingManager.setReadBackground(theme)
            when (theme) {
                ReadSettingManager.READ_BG_DEFAULT -> {
                    mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_2c)
                    mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_cec29c)
                }
                ReadSettingManager.READ_BG_1 -> {
                    mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_2f332d)
                    mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_ccebcc)
                }
                ReadSettingManager.READ_BG_2 -> {
                    mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_92918c)
                    mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_aaa)
                }
                ReadSettingManager.READ_BG_3 -> {
                    mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_383429)
                    mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_d1cec5)
                }
                ReadSettingManager.READ_BG_4 -> {
                    mTextColor = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_627176)
                    mPageBg = ContextCompat.getColor(MyApp.getInstance()!!, R.color.color_001c27)
                }
            }
        }
    }

    fun setDisplaySize(w: Int, h: Int) {
        //获取PageView的宽高
        mDisplayWidth = w
        mDisplayHeight = h

        //获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2

        //创建用来缓冲的 Bitmap
        mNextBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)

        //如果章节已显示，那么就重新计算页面
        if (mStatus == STATUS_FINISH) {
            //mCurPageList = loadPageList(mCurChapterPos);
            //重新设置文章指针的位置
            mCurPage = getCurPage(mCurPage.position)
        }

        mPageView?.drawCurPage(false)
    }

    /**
     * @return:获取初始显示的页面
     */
    private fun getCurPage(pos: Int): TxtPage {
//        if (mPageChangeListener != null) {
//            mPageChangeListener.onPageChange(pos)
//        }
        return mCurPageList!![pos]
    }

    /**
     * 翻页动画
     */
    fun setPageMode(pageMode: Int) {
        mPageMode = pageMode
        mPageView?.setPageMode(mPageMode)
        mSettingManager.pageMode = mPageMode
        //重绘
        mPageView?.drawCurPage(false)
    }


    //获取当前页的状态
    fun getPageStatus(): Int {
        return mStatus
    }

    //获取当前章节的章节位置
    fun getChapterPos(): Int {
        return mCurChapterPos
    }

    //获取当前页的页码
    fun getPagePos(): Int {
        return mCurPage.position
    }


    /**
     * 打开书本，初始化书籍
     */
    fun openBook(chapters: List<ChaptersDetailsBean>) {
        mChapterList = chapters
        iRequestChapterContent?.startRequest(mChapterList!![0].id)
    }

    /**
     * 请求章节内容成功
     */
    override fun onSuccess(chapterContent: ChapterContentBean) {
        loadChapter(chapterContent.title,chapterContent.content)
    }

    /**
     * 请求章节内容失败
     */
    override fun onFailure() {
        mStatus = STATUS_ERROR
        //显示加载错误
        mPageView?.drawCurPage(false)
    }


    private fun loadChapter(title:String,content:String) : List<TxtPage>{
        //生成的页面
        val pages = ArrayList<TxtPage>()
        val lines = ArrayList<String>()                     //一页的文字
        var page = TxtPage()                               //单页
        var rHeight = mVisibleHeight                               //界面的高
        val fontWidth = mTitlePaint.measureText("哈")
        val wordCount = (mVisibleWidth / fontWidth).toInt()          //一行所占的字数
        val subStr = ""

        Log.e("TAG", "mVisibleWidth - > $mVisibleWidth")
        Log.e("TAG", "mVisibleHeight - > $mVisibleHeight")
        Log.e("TAG", "width - > $fontWidth")
        Log.e("TAG", "content - > $content")
        Log.e("TAG", "content.length - > " + content.length)
        Log.e("TAG", "content - > " + content.substring(0, 2))

        while (!content.isEmpty()) {

            if (rHeight < 0) {
                pages.add(page)
                page = TxtPage()
                rHeight = mVisibleHeight
                lines.clear()
                continue
            }

            page.position = pages.size
            if (pages.size == 0) {     //只有在第一页才绘制标题
                page.title = title
                page.titleLines = 1
                rHeight -= mTitlePaint.textSize.toInt()
            } else {
                page.title = title
                page.titleLines = 0
            }
            if (content.length < wordCount) {
                lines.add(content)
                content = ""
            } else {
                val temp = content
                lines.add(temp.substring(0, wordCount))
            }
            rHeight -= mTextPaint.textSize.toInt()

            if (content.length > wordCount) {
                content.substring(wordCount, content.length)
            }
        }
        return pages
    }

    /**
     * 绘制背景与内容
     */
    fun onDraw(bitmap: Bitmap, isUpdate: Boolean) {
        drawBackground(mPageView?.bgBitmap, isUpdate)
        if (!isUpdate) {
            drawContent(bitmap)
        }
        //更新绘制
        mPageView?.invalidate()
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(bitmap: Bitmap?, isUpdate: Boolean) {
        val canvas = Canvas(bitmap!!)
        val tipMarginHeight = ScreenUtils.dpToPx(3)
        if (!isUpdate) {
            /****绘制背景 */
            canvas.drawColor(mPageBg)

            /*****初始化标题的参数 */
            //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
            val tipTop = tipMarginHeight - mTipPaint.fontMetrics.top
            //根据状态不一样，数据不一样
            if (mStatus != STATUS_FINISH) {
                if (mChapterList != null && mChapterList?.size != 0) {
                    canvas.drawText(mChapterList?.get(mCurChapterPos)!!.chapter, mMarginWidth.toFloat(), tipTop, mTipPaint)
                }
            } else {
                canvas.drawText(Objects.requireNonNull<String>(mCurPage.title), mMarginWidth.toFloat(), tipTop, mTipPaint)
            }

            /******绘制页码 */
            //底部的字显示的位置Y
            val y = mDisplayHeight.toFloat() - mTipPaint.fontMetrics.bottom - tipMarginHeight.toFloat()
            //只有finish的时候采用页码
            if (mStatus == STATUS_FINISH) {
                val percent = (mCurPage.position + 1).toString() + "/" + mCurPageList?.size
                canvas.drawText(percent, mMarginWidth.toFloat(), y, mTipPaint)
            }
        } else {
            //擦除区域
            mBgPaint.color = mPageBg
            canvas.drawRect((mDisplayWidth / 2).toFloat(), (mDisplayHeight - mMarginHeight + ScreenUtils.dpToPx(2)).toFloat(), mDisplayWidth.toFloat(), mDisplayHeight.toFloat(), mBgPaint)
        }
        /******绘制电池 */

        val visibleRight = mDisplayWidth - mMarginWidth
        val visibleBottom = mDisplayHeight - tipMarginHeight

        val outFrameWidth = mTipPaint.measureText("xxx").toInt()
        val outFrameHeight = mTipPaint.textSize.toInt()

        val polarHeight = ScreenUtils.dpToPx(6)
        val polarWidth = ScreenUtils.dpToPx(2)
        val border = 1
        val innerMargin = 1

        //电极的制作
        val polarLeft = visibleRight - polarWidth
        val polarTop = visibleBottom - (outFrameHeight + polarHeight) / 2
        val polar = Rect(polarLeft, polarTop, visibleRight,
                polarTop + polarHeight - ScreenUtils.dpToPx(2))

        mBatteryPaint.style = Paint.Style.FILL
        canvas.drawRect(polar, mBatteryPaint)

        //外框的制作
        val outFrameLeft = polarLeft - outFrameWidth
        val outFrameTop = visibleBottom - outFrameHeight
        val outFrameBottom = visibleBottom - ScreenUtils.dpToPx(2)
        val outFrame = Rect(outFrameLeft, outFrameTop, polarLeft, outFrameBottom)

        mBatteryPaint.style = Paint.Style.STROKE
        mBatteryPaint.strokeWidth = border.toFloat()
        canvas.drawRect(outFrame, mBatteryPaint)

        //内框的制作
        val innerWidth = (outFrame.width() - innerMargin * 2 - border) * (mBatteryLevel / 100.0f)
        val innerFrame = RectF((outFrameLeft + border + innerMargin).toFloat(), (outFrameTop + border + innerMargin).toFloat(),
                outFrameLeft.toFloat() + border.toFloat() + innerMargin.toFloat() + innerWidth, (outFrameBottom - border - innerMargin).toFloat())

        mBatteryPaint.style = Paint.Style.FILL
        canvas.drawRect(innerFrame, mBatteryPaint)

        /******绘制当前时间 */
        //底部的字显示的位置Y
        val y = mDisplayHeight.toFloat() - mTipPaint.fontMetrics.bottom - tipMarginHeight.toFloat()
        val time = StringUtils.dateConvert(System.currentTimeMillis(), "HH:mm")
        val x = outFrameLeft.toFloat() - mTipPaint.measureText(time) - ScreenUtils.dpToPx(4).toFloat()
        canvas.drawText(time, x, y, mTipPaint)
    }

    /**
     * 绘制内容文字
     */
    private fun drawContent(bitmap: Bitmap) {
        val canvas = Canvas(bitmap)

        if (mPageMode == PageView.PAGE_MODE_SCROLL) {
            canvas.drawColor(mPageBg)
        }
        /******绘制内容 */
        if (mStatus != STATUS_FINISH) {
            //绘制字体
            var tip = ""
            when (mStatus) {
                STATUS_LOADING -> tip = "正在拼命加载中..."
                STATUS_ERROR -> tip = "加载失败(点击边缘重试)"
                STATUS_EMPTY -> tip = "文章内容为空"
                STATUS_PARSE -> tip = "正在排版请等待..."
                STATUS_PARSE_ERROR -> tip = "文件解析错误"
            }

            //将提示语句放到正中间
            val fontMetrics = mTextPaint.fontMetrics
            val textHeight = fontMetrics.top - fontMetrics.bottom
            val textWidth = mTextPaint.measureText(tip)
            val pivotX = (mDisplayWidth - textWidth) / 2
            val pivotY = (mDisplayHeight - textHeight) / 2
            canvas.drawText(tip, pivotX, pivotY, mTextPaint)
        } else {
            var top: Float

            if (mPageMode == PageView.PAGE_MODE_SCROLL) {
                top = -mTextPaint.fontMetrics.top
            } else {
                top = mMarginHeight - mTextPaint.fontMetrics.top
            }

            //设置总距离
            val interval = mTextInterval + mTextPaint.textSize.toInt()
            val para = mTextPara + mTextPaint.textSize.toInt()
            val titleInterval = mTitleInterval + mTitlePaint.textSize.toInt()
            val titlePara = mTitlePara + mTextPaint.textSize.toInt()
            var str: String? = null

            //对标题进行绘制
            for (i in 0 until mCurPage.titleLines) {
                str = mCurPage.lines!![i]

                //设置顶部间距
                if (i == 0) {
                    top += mTitlePara.toFloat()
                }

                //计算文字显示的起始点
                val start = (mDisplayWidth - mTitlePaint.measureText(str)).toInt() / 2
                //进行绘制
                canvas.drawText(str, start.toFloat(), top, mTitlePaint)

                //设置尾部间距
                top += if (i == mCurPage.titleLines - 1) {
                    titlePara.toFloat()
                } else {
                    //行间距
                    titleInterval.toFloat()
                }
            }

            //对内容进行绘制
            for (i in mCurPage.titleLines until mCurPage.lines!!.size) {
                str = mCurPage.lines!![i]

                canvas.drawText(str, mMarginWidth.toFloat(), top, mTextPaint)
                top += if (str.endsWith("\n")) {
                    para.toFloat()
                } else {
                    interval.toFloat()
                }
            }
        }
    }

    /**
     * 检测当前状态是否能够进行加载章节数据
     */
    private fun checkStatus(): Boolean {
        if (mStatus == STATUS_LOADING) {
            Toast.makeText(MyApp.getInstance(), "正在加载中", Toast.LENGTH_SHORT).show()
            return false
        } else if (mStatus == STATUS_ERROR) {
            //点击重试
            mStatus = STATUS_LOADING
            mPageView?.drawCurPage(false)
            return false
        }
        //解析失败，退出
        return true
    }

    /**
     * 翻阅上一页
     */
    fun prev(): Boolean {
        if (!checkStatus()) return false

        //判断是否达到章节的起始点
        val prevPage = getPrevPage() ?: //载入上一章。

                if (!prevChapter()) {
                    return false
                } else {
                    mCancelPage = mCurPage
                    mCurPage = getPrevLastPage()
                    mPageView?.drawNextPage()
                    return true
                }

        mCancelPage = mCurPage
        mCurPage = prevPage

        mPageView?.drawNextPage()
        return true
    }

    /**
     * 加载上一章
     */
    private fun prevChapter(): Boolean {
        //判断是否上一章节为空
        if (mCurChapterPos - 1 < 0) {
            Toast.makeText(MyApp.getInstance(), "没有上一章了", Toast.LENGTH_SHORT).show()
            return false
        }

        //加载上一章数据
        val prevChapter = mCurChapterPos - 1
        //当前章变成下一章
        mNextPageList = mCurPageList

        //判断上一章缓存是否存在，如果存在则从缓存中获取数据。
        if (mWeakPrePageList != null && mWeakPrePageList?.get() != null) {
            mCurPageList = mWeakPrePageList?.get()
            mWeakPrePageList = null
        } else {
            if (prevChapter < 0) {
                mCurPageList = loadPageList(mChapterList.get(0))
            } else {
                mCurPageList = loadPageList(mChapterList.get(prevChapter))
            }
        }//如果不存在则加载数据

        mLastChapter = mCurChapterPos
        mCurChapterPos = prevChapter

        if (mCurPageList != null) {
            mStatus = STATUS_FINISH
        } else {
            mStatus = STATUS_LOADING
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0
            mPageView?.drawNextPage()
        }//如果当前章不存在，则表示在加载中

//        if (mPageChangeListener != null) {
//            mPageChangeListener.onChapterChange(mCurChapterPos)
//        }

        return true
    }

    /**
     * 翻阅下一页
     */
    operator fun next(): Boolean {
        if (!checkStatus()) return false
        //判断是否到最后一页了
        val nextPage = getNextPage() ?: return if (!nextChapter()) {
            false
        } else {
            mCancelPage = mCurPage
            mCurPage = getCurPage(0)
            mPageView?.drawNextPage()
            true
        }

        mCancelPage = mCurPage
        mCurPage = nextPage
        mPageView?.drawNextPage()

        //为下一页做缓冲

        //加载下一页的文章

        return true
    }

    /**
     * 加载下一章
     */
    private fun nextChapter(): Boolean {
        //加载一章
        if (mCurChapterPos + 1 >= mChapterList.size) {
            Toast.makeText(MyApp.getInstance(), "没有下一章了", Toast.LENGTH_SHORT).show()
            return false
        }

        //如果存在下一章，则存储当前Page列表为上一章
        if (mCurPageList != null) {
            mWeakPrePageList = WeakReference(ArrayList(mCurPageList))
        }

        val nextChapter = mCurChapterPos + 1
        //如果存在下一章预加载章节。
        if (mNextPageList != null) {
            mCurPageList = mNextPageList
            mNextPageList = null
        } else {
            //这个PageList可能为 null，可能会造成问题。
            if (nextChapter > mChapterList!!.size) {
                mCurPageList = loadPageList(mChapterList!![mChapterList!!.size - 1])
            } else {
                mCurPageList = loadPageList(mChapterList!![nextChapter])
            }
        }

        mLastChapter = mCurChapterPos
        mCurChapterPos = nextChapter

        //如果存在当前章，预加载下一章
        if (mCurPageList != null) {
            mStatus = STATUS_FINISH
            preLoadNextChapter()
        } else {
            mStatus = STATUS_LOADING
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0
            mPageView?.drawNextPage()
        }//如果当前章不存在，则表示在加载中

//        if (mPageChangeListener != null) {
//            mPageChangeListener.onChapterChange(mCurChapterPos)
//        }
        return true
    }

    /**
     * 预加载下一章
     */
    private fun preLoadNextChapter() {
        //判断是否存在下一章
        if (mCurChapterPos + 1 >= mChapterList!!.size) {
            return
        }
        //判断下一章的文件是否存在
        val nextChapter = mCurChapterPos + 1

        //如果之前正在加载则取消
//        if (mPreLoadDisp != null) {
//            mPreLoadDisp.dispose()
//        }
    }

    /**
     * 取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
     */
    fun pageCancel() {
        //加载到下一章取消了
        if (mCurPage.position == 0 && mCurChapterPos > mLastChapter) {
            prevChapter()
        } else if (mCurPageList == null || mCurPage.position == mCurPageList!!.size - 1 && mCurChapterPos < mLastChapter) {
            nextChapter()
        }//加载上一章取消了 (可能有点小问题)
        //假设加载到下一页了，又取消了。那么需要重新装载的问题
        mCurPage = mCancelPage
    }

    /**
     * @return:获取上一个页面
     */
    private fun getPrevPage(): TxtPage? {
        val pos = mCurPage.position - 1
        if (pos < 0) {
            return null
        }
//        if (mPageChangeListener != null) {
//            mPageChangeListener.onPageChange(pos)
//        }
        return mCurPageList?.get(pos)
    }

    /**
     * @return:获取下一的页面
     */
    private fun getNextPage(): TxtPage? {
        if (mCurPage != null) {
            val pos = mCurPage.position + 1
            if (pos >= mCurPageList!!.size) {
                return null
            }
//            if (mPageChangeListener != null) {
//                mPageChangeListener.onPageChange(pos)
//            }
            return mCurPageList?.get(pos)
        }
        return TxtPage()
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private fun getPrevLastPage(): TxtPage {
        val pos = mCurPageList!!.size - 1
        return mCurPageList!![pos]
    }

    /**
     * 设置文字大小
     */
    fun setTextSize(textSize: Int) {
        //        if (!isBookOpen) return;

        //设置textSize
        mTextSize = textSize
        mTextInterval = mTextSize / 2
        mTextPara = mTextSize

        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE)
        mTitleInterval = mTitleInterval / 2
        mTitlePara = mTitleSize

        //设置画笔的字体大小
        mTextPaint.textSize = mTextSize.toFloat()
        //设置标题的字体大小
        mTitlePaint.textSize = mTitleSize.toFloat()
        //存储状态
        mSettingManager.textSize = mTextSize
        //取消缓存
        mWeakPrePageList = null
        mNextPageList = null
        //如果当前为完成状态。
        if (mStatus == STATUS_FINISH) {
            //重新计算页面
            //mCurPageList = loadPageList(mCurChapterPos);

            //防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
            if (mCurPage.position >= mCurPageList!!.size) {
                mCurPage.position = mCurPageList!!.size - 1
            }
        }
        //重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.position)
        //绘制
        mPageView?.refreshPage()
    }


    /**
     * 设置夜间模式
     */
    fun setNightMode(nightMode: Boolean) {
        isNightMode = nightMode
        if (isNightMode) {
            mBatteryPaint.color = Color.WHITE
            setBgColor(ReadSettingManager.NIGHT_MODE)
        } else {
            mBatteryPaint.color = Color.BLACK
            setBgColor(mBgTheme)
        }
        mSettingManager.isNightMode = nightMode
    }


    /**
     * 更新时间
     */
    fun updateTime() {
        if (mPageView!!.isPrepare && !mPageView?.isRunning!!) {
            mPageView?.drawCurPage(true)
        }
    }

    /**
     * 更新电量
     */
    fun updateBattery(level: Int) {
        mBatteryLevel = level
        if (mPageView!!.isPrepare && !mPageView?.isRunning!!) {
            mPageView?.drawCurPage(true)
        }
    }


    /**
     * 关闭书籍，清除记录
     */
    fun closeBook() {
        mPageView = null
        if(iRequestChapterContent != null){
            iRequestChapterContent = null
        }
    }
}







