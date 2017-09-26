package com.demo.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.demo.customviewdemo.R
import java.util.*

/**
 * Created by 54095 on 2017/9/25.
 */
class FireView : View {
    val DEFAULT_WIDTH = 200
    val DEFAULT_HEIGHT = 200

    var fireCount = 20//默认显示20个烟花
    var fireColor1: Int = 0xff0000//默认是红色
    var fireColor2: Int = 0xff0000//默认是红色
    var fireColor3: Int = 0xff0000//默认是红色
    var fireColor4: Int = 0xff0000//默认是红色
    var fireColor5: Int = 0xff0000//默认是红色
    var fireList: ArrayList<Point>? = null
    var centerPoint: Point? = null
    var isFiring: Boolean = false

    var mPaint1: Paint? = null
    var mPaint2: Paint? = null
    var mPaint3: Paint? = null
    var mPaint4: Paint? = null
    var mPaint5: Paint? = null
    var radius = 5f

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var array: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.FireView, defStyleAttr, 0)
        fireCount = array.getInt(R.styleable.FireView_firecount, 20)
        fireColor1 = array.getColor(R.styleable.FireView_firecolor1, R.color.red)
        fireColor2 = array.getColor(R.styleable.FireView_firecolor2, R.color.red)
        fireColor3 = array.getColor(R.styleable.FireView_firecolor3, R.color.red)
        fireColor4 = array.getColor(R.styleable.FireView_firecolor4, R.color.red)
        fireColor5 = array.getColor(R.styleable.FireView_firecolor5, R.color.red)
        init()
        //千万不要忘记回收
        array.recycle()

    }

    fun init() {
        mPaint1 = Paint()
        mPaint1!!.color = fireColor1
        mPaint1!!.isAntiAlias = true
        mPaint2 = Paint()
        mPaint2!!.color = fireColor2
        mPaint2!!.isAntiAlias = true
        mPaint3 = Paint()
        mPaint3!!.color = fireColor3
        mPaint3!!.isAntiAlias = true
        mPaint4 = Paint()
        mPaint4!!.color = fireColor4
        mPaint4!!.isAntiAlias = true
        mPaint5 = Paint()
        mPaint5!!.color = fireColor5
        mPaint5!!.isAntiAlias = true
    }

    fun getFirePoints() {
        if (fireList == null) {
            fireList = ArrayList()
        }
        if (centerPoint != null) {
            fireList?.clear()
            for (i in 0..fireCount) {
                var offset_x = Math.random() * 500
                var offset_y = Math.random() * Math.sqrt(500 * 500 - (offset_x * offset_x))
                fireList?.add(Point((centerPoint?.x!! + (if (Math.random() > 0.5) 1 else -1) * offset_x).toInt(), (centerPoint?.y!! + (if (Math.random() > 0.5) 1 else -1) * offset_y).toInt()))
            }
        }
        setAnimatorForElements()
    }

    /**
     * 给每个元素设置动画
     */
    fun setAnimatorForElements() {
        if (fireList != null) {
            for ((index, element) in fireList!!.withIndex()) {
                var animator_x: ValueAnimator = ValueAnimator.ofFloat(centerPoint?.x?.toFloat()!!, element.x.toFloat())
                animator_x.addUpdateListener { valueAnimator ->
                    element.x = (valueAnimator.animatedValue as Float).toInt()
                }
                animator_x.interpolator = AccelerateDecelerateInterpolator()
                animator_x.duration = 1000
                animator_x.start()

                var animator_y: ValueAnimator = ValueAnimator.ofFloat(centerPoint?.y?.toFloat()!!, element.y.toFloat())
                animator_y.addUpdateListener { valueAnimator ->
                    element.y = (valueAnimator.animatedValue as Float).toInt()
                }
                animator_y.interpolator = AccelerateDecelerateInterpolator()
                animator_y.duration = 1000
                animator_y.start()
            }
        }
    }

    /**
     * 进行绘制
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (fireList != null) {
            if (fireList?.size != 0) {
                for ((index, element) in fireList!!.withIndex()) {
                    if (index in 0..fireCount / 5) {
                        canvas?.drawCircle(element.x.toFloat(), element.y.toFloat(), radius.toFloat(), mPaint1)
                    } else if (index in fireCount / 5..fireCount / 5 * 2) {
                        canvas?.drawCircle(element.x.toFloat(), element.y.toFloat(), radius.toFloat(), mPaint2)
                    } else if (index in fireCount / 5 * 2..fireCount / 5 * 3) {
                        canvas?.drawCircle(element.x.toFloat(), element.y.toFloat(), radius.toFloat(), mPaint3)
                    } else if (index in fireCount / 5 * 3..fireCount / 5 * 4) {
                        canvas?.drawCircle(element.x.toFloat(), element.y.toFloat(), radius.toFloat(), mPaint4)
                    } else {
                        canvas?.drawCircle(element.x.toFloat(), element.y.toFloat(), radius.toFloat(), mPaint5)
                    }
                }
            }
        }

    }

    /**
     * 进行测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_HEIGHT)
        }
    }

    /**
     * 事件处理
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN && !isFiring) {
            isFiring = true
            centerPoint = Point(event.x.toInt(), event.y.toInt())
            getFirePoints()
            var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 5f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.addUpdateListener { valueAnimator ->
                radius = valueAnimator.getAnimatedValue() as Float
                invalidate()
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                    isFiring = true
                }

                override fun onAnimationEnd(p0: Animator?) {
                    isFiring = false
                    fireList?.clear()
                    invalidate()
                }
            })
            animator.duration = 1000
            animator.start()
        }
        return super.onTouchEvent(event)
    }

}