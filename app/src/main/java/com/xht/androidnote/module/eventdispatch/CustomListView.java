package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView {
    public CustomListView(Context context) {
        this(context,null);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *  解决listview和scrollView嵌套只显示一条问题（重新设置模式）
     *  因为ListView的模式是UNSPECIFIED,是top+bottom+一条item的高
     *
     *      if (heightMode == MeasureSpec.UNSPECIFIED) {
     *             heightSize = mListPadding.top + mListPadding.bottom + childHeight +
     *                     getVerticalFadingEdgeLength() * 2;
     *         }
     *
     *  右移2位是AT_MOST共32位,前两位是模式信息，后30位是值，后移2位获得30位的值
     *
     *  Integer.MAX_VALUE:不设置成这个就会走下面方法只显示一条
     *
     *     if (returnedHeight >= maxHeight) {
     *                 // We went over, figure out which height to return.  If returnedHeight > maxHeight,
     *                 // then the i'th position did not fit completely.
     *                 return (disallowPartialChildPosition >= 0) // Disallowing is enabled (> -1)
     *                             && (i > disallowPartialChildPosition) // We've past the min pos
     *                             && (prevHeightWithoutPartialChild > 0) // We have a prev height
     *                             && (returnedHeight != maxHeight) // i'th child did not fit completely
     *                         ? prevHeightWithoutPartialChild
     *                         : maxHeight;
     *             }
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

