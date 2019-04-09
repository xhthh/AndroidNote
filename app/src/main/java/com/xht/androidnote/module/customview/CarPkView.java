package com.xht.androidnote.module.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xht.androidnote.utils.ScreenUtils;


/**
 * 创建日期：2017/9/5 on 9:46
 * 描述: 爱车pk view
 * 作者:秦佳东 ASUS
 */

public class CarPkView extends View {
    private final int barHeight = ScreenUtils.dip2px(getContext(), 4);//比例条的高度
    private final int triangleLength = barHeight;//三角形边长
    private final int textH = ScreenUtils.dip2px(getContext(), 11);//文字高度
    private final int textMargin = ScreenUtils.dip2px(getContext(), 5);//文字距离比例条的距离

    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;

    private int leftColor;//左边颜色
    private int rightColor;//右边颜色
    private int leftProportion;//左边占比(百分之XX，比如60%就传60)
    private int rightProportion;//右边占比
    private String leftText;
    private String rightText;

    private Paint paintLeft;
    private Paint paintRight;
    private Path pathLeft = new Path();
    private Path pathRight = new Path();

    public CarPkView(Context context) {
        this(context, null);
    }

    public CarPkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarPkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paintLeft = new Paint();
        paintLeft.setAntiAlias(true);//去锯齿
        paintLeft.setTextSize(textH);

        paintRight = new Paint();
        paintRight.setAntiAlias(true);
        paintRight.setTextSize(textH);

        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //没有setData之前，不做任何绘制
        if (paintLeft == null || paintRight == null || leftColor == 0 || rightColor == 0) return;

        float leftW = (getWidth() - paddingLeft - paddingRight) / 100f * leftProportion;
        float rightW = (getWidth() - paddingLeft - paddingRight) / 100f * rightProportion;

        paintLeft.setColor(leftColor);
        //绘制多边形
        pathLeft.moveTo(paddingLeft, paddingTop);
        pathLeft.lineTo(paddingLeft + leftW - triangleLength, paddingTop);
        pathLeft.lineTo(paddingLeft + leftW, barHeight + paddingTop);
        pathLeft.lineTo(paddingLeft, barHeight + paddingTop);
        pathLeft.close();
        canvas.drawPath(pathLeft, paintLeft);
        //绘制文字
        canvas.drawText(leftText, paddingLeft, paddingTop + barHeight + textMargin + textH, paintLeft);


        Log.i("xht","左边线 leftW=" + leftW +
                "   \nlineTo1=" + (paddingLeft + leftW - triangleLength) + " , " + paddingTop +
                "   \nlineTo2=" + (paddingLeft + leftW) + " , " + (barHeight + paddingTop) +
                "   \nlineTo3=" + paddingLeft + " , " + (barHeight + paddingTop));

        paintRight.setColor(rightColor);
        //绘制多边形
        pathRight.moveTo(paddingLeft + leftW, paddingTop);
        pathRight.lineTo(paddingLeft + leftW + rightW - paddingRight, paddingTop);
        pathRight.lineTo(paddingLeft + leftW + rightW - paddingRight, barHeight + paddingTop);
        pathRight.lineTo(paddingLeft + leftW + triangleLength, barHeight + paddingTop);
        pathRight.close();
        canvas.drawPath(pathRight, paintRight);
        //绘制文字
        canvas.drawText(rightText,
                getWidth() - paddingLeft - paintRight.measureText(rightText),
                paddingTop + barHeight + textMargin + textH, paintRight);

        Log.i("xht","右边线 rightW=" + rightW +
                "   \nlineTo1=" + (paddingLeft + leftW + rightW) + " , " + paddingTop +
                "   \nlineTo2=" + (paddingLeft + leftW + rightW) + " , " + (barHeight + paddingTop) +
                "   \nlineTo3=" + (paddingLeft + leftW + triangleLength) + " , " + (barHeight + paddingTop));

        Log.i("xht","paddingLeft==" + paddingLeft + "  paddingRight==" + paddingRight + "  triangleLength==" + triangleLength + "  barheight==" +barHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取宽-测量规则的大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(widthSize, textH + textMargin + barHeight + paddingTop + paddingBottom);
    }

    public synchronized void setData(int leftColor, int rightColor, int leftProportion, int rightProportion) {
        if (leftProportion < 0 || leftProportion > 100 || rightProportion < 0 || rightProportion > 100)
            return;

        //如果leftProportion和rightProportion之和不为100，则rightProportion=100 - leftProportion
        if (leftProportion + rightProportion != 100) {
            rightProportion = 100 - leftProportion;
        }

        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.leftProportion = leftProportion;
        this.rightProportion = rightProportion;
        leftText = leftProportion + "%";
        rightText = rightProportion + "%";
        invalidate();
    }
}
