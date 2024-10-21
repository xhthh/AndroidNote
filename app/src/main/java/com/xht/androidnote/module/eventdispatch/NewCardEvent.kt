package com.xht.androidnote.module.eventdispatch

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.cardview.widget.CardView

/**
 * 点击实现按压效果
 */
class NewCardEvent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var initialElevation: Float = 0f
    private var animatorSet: AnimatorSet? = null
    private var isPressed = false  // 新增：跟踪按压状态

    init {
        initialElevation = cardElevation
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                pressedEffect()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isPressed) {
                    if (!isPointInside(event.x, event.y)) {
                        isPressed = false
                        releasedEffect()
                    }
                } else {
                    if (isPointInside(event.x, event.y)) {
                        isPressed = true
                        pressedEffect()
                    }
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (isPressed) {
                    isPressed = false
                    releasedEffect()
                    performClick()
                }
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                if (isPressed) {
                    isPressed = false
                    releasedEffect()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun isPointInside(x: Float, y: Float): Boolean {
        return x >= 0 && x < width && y >= 0 && y < height
    }

    private fun pressedEffect() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@NewCardEvent, "scaleX", 1f, 0.97f),
                ObjectAnimator.ofFloat(this@NewCardEvent, "scaleY", 1f, 0.97f),
                ObjectAnimator.ofFloat(this@NewCardEvent, "alpha", 1f, 0.9f)
            )
            duration = 100
            start()
        }
        cardElevation = initialElevation + 4f
    }

    private fun releasedEffect() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@NewCardEvent, "scaleX", 0.97f, 1f),
                ObjectAnimator.ofFloat(this@NewCardEvent, "scaleY", 0.97f, 1f),
                ObjectAnimator.ofFloat(this@NewCardEvent, "alpha", 0.9f, 1f)
            )
            duration = 100
            start()
        }
        cardElevation = initialElevation
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}