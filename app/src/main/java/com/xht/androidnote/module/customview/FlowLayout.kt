package com.xht.androidnote.module.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.xht.androidnote.R
import com.xht.androidnote.utils.DimenUtil
import com.xht.androidnote.utils.L

class FlowLayout : ViewGroup {
    /**
     * 每一个孩子的左右的间距
     * 20是默认值,单位是px
     */
    private var mHSpace: Int? = 20

    /**
     * 每一行的上下的间距
     * 20是默认值,单位是px
     */
    private var mVSpace: Int? = 20

    /**
     * -1表示不限制,最多显示几行
     */
    private var mMaxLines: Int?

    /**
     * 孩子中最高的一个
     */
    private var mChildMaxHeight = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        mHSpace =
            a?.getDimensionPixelSize(R.styleable.FlowLayout_h_space, DimenUtil.dp2px(context, 10f))
        mVSpace =
            a?.getDimensionPixelSize(R.styleable.FlowLayout_v_space, DimenUtil.dp2px(context, 10f))
        mMaxLines = a?.getInt(R.styleable.FlowLayout_maxlines, -1)

        a?.recycle()
    }

    /**
     * 首先得让所有孩子都测量一遍,然后找到高度最高的那个值,然后再让孩子测量一遍
     * (为什么要再测量一遍是因为第一次测量只是为了知道每一个孩子的高度,
     * 而第二次测量是告诉所有孩子高度现在确定啦,这样子每一个孩子内部才会根据这个值做一些计算,
     * 如果没有再次测量这一步,比如TextView显示文本的时候,你设置了居中显示,可能就会失效了
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //拿到父容器推荐的宽和高以及计算模式
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        //测量孩子的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //寻找孩子中最高的一个，找到的值赋给 mChildMaxHeight 变量中
        findMaxChildMaxHeight()

        //初始化值
        var left = paddingLeft
        var top = paddingTop

        //几行
        var lines = 1

        /*
            循环所有孩子
            这里的代码分几步:
            1.测量所有孩子,找到高度最高的值
            2.然后我们模拟排列每一个孩子(View),计算出自身需要的高度
            3.然后根据父容器推荐的高度的计算模式,来决定,自身高度是直接采用父容器给的高度还是采用自身的高度
            4.最后设置自己的宽和高,把内边距考虑进去
            5.这里有一个需要注意的是,如果设置了最高显示的行数,那么如果模拟排列的时候行数超过了最大的行数,那么高度就以最高行数的高度为准
         */
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (left != paddingLeft) {//是否是一行的开头
                if ((left + view.measuredWidth) > sizeWidth - paddingRight) {//需要换行了
                    // 如果到了最大的行数,就跳出,top就是当前的
                    if (mMaxLines != -1 && lines >= mMaxLines!!) {
                        break
                    }
                    //计算出下一行的top
                    top += mChildMaxHeight + mVSpace!!
                    left = paddingLeft
                    lines++
                }
            }
            left += view.measuredWidth + mHSpace!!
        }

        L.e("modeHeight = $modeHeight===top = $top mChildMaxHeight = $mChildMaxHeight")
        if (modeHeight == MeasureSpec.EXACTLY) {
            //直接使用父类推荐的宽和高
            setMeasuredDimension(sizeWidth, sizeHeight)
        } else if (modeHeight == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                sizeWidth,
                if ((top + mChildMaxHeight + paddingBottom) > sizeHeight) sizeHeight else top + mChildMaxHeight + paddingBottom
            )
        } else {
            setMeasuredDimension(sizeWidth, top + mChildMaxHeight + paddingBottom)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        findMaxChildMaxHeight()
        //开始安排孩子的位置

        //初始化值
        var left = paddingLeft
        var top = paddingTop
        var lines = 1

        val childCount = childCount
        //循环遍历
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (left != paddingLeft) {//是否是一行的开头
                if ((left + view.measuredWidth) > width - paddingRight) {//需要换行了
                    // 如果到了最大的行数,就跳出,top就是当前的
                    if (mMaxLines != -1 && lines >= mMaxLines!!) {
                        break
                    }
                    //计算出下一行的top
                    top += mChildMaxHeight + mVSpace!!
                    left = paddingLeft
                    lines++
                }
            }
            //安排孩子的位置
            view.layout(left, top, left + view.measuredWidth, top + mChildMaxHeight)
            left += view.measuredWidth + mHSpace!!
        }
    }

    private fun findMaxChildMaxHeight() {
        mChildMaxHeight = 0
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view.measuredHeight > mChildMaxHeight) {
                mChildMaxHeight = view.measuredHeight
            }
        }
    }

    fun setHSpace(hSpace: Int) {
        mHSpace = hSpace
    }

    fun setVSpace(vSpace: Int) {
        mVSpace = vSpace
    }
}