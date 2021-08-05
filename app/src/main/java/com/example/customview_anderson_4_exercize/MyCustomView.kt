package com.example.customview_anderson_4_exercize

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MyCustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(
    context,
    attributeSet,
    defStyle
) {

    //attributes from XML
    private var mColorHandHours = 0
    private var mColorHandMinutes = 0
    private var mColorHandSeconds = 0
    private var mColorCircle = 0

    private var mSizeHandHours = 0f
    private var mSizeHandMinutes = 0f
    private var mSizeHandSeconds = 0f

    //Fields for create clock
    private var mRect: Rect? = Rect()
    private var mHeight = 0
    private var mWidth = 0
    private var mRadius = 0
    private var mAngle = 0.0
    private var mCentreX = 0
    private var mCentreY = 0
    private var mPadding = 0
    private var mIsInit = false
    private var mPaint: Paint? = Paint()
    private var mNumbers: IntArray? = intArrayOf()
    private var mMinimum = 0
    private var mHour = 0f
    private var mMinute = 0f
    private var mSecond = 0f
    private var mHourHandSize = 0
    private var mHandSize = 0

    init {

        val typedArray: TypedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MyCustomView,
            defStyle, 0
        )

        mColorHandHours = typedArray.getColor(R.styleable.MyCustomView_color_hand_hour, Color.WHITE)
        mColorHandMinutes =
            typedArray.getColor(R.styleable.MyCustomView_color_hand_minutes, Color.WHITE)
        mColorHandSeconds =
            typedArray.getColor(R.styleable.MyCustomView_color_hand_seconds, Color.WHITE)
        mColorCircle = typedArray.getColor(R.styleable.MyCustomView_color_circle, Color.WHITE)
        mSizeHandHours = typedArray.getDimension(R.styleable.MyCustomView_size_hand_hour, 8f)
        mSizeHandMinutes = typedArray.getDimension(R.styleable.MyCustomView_size_hand_minutes, 6f)
        mSizeHandSeconds = typedArray.getDimension(R.styleable.MyCustomView_size_hand_seconds, 6f)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawColor(Color.BLACK)
        if (!mIsInit) {
            init()
        }

        drawCircle(canvas);
        drawHands(canvas);
        drawNumerals(canvas);
        drawPointInCenter(canvas)
        postInvalidate()
    }

    private fun drawPointInCenter(canvas: Canvas) {
        setPaintAttributes(Color.WHITE, Paint.Style.FILL, 0f, true)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), 15f, mPaint!!)
    }

    private fun drawCircle(canvas: Canvas) {
        setPaintAttributes(Color.WHITE, Paint.Style.STROKE, 8f, true)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat(), mPaint!!);
    }

    private fun setPaintAttributes(
        color: Int,
        style: Paint.Style,
        strokeWidth: Float,
        isAntiAlias: Boolean
    ) {
        mPaint?.apply {
            reset()
            this.color = color
            this.style = style
            this.strokeWidth = strokeWidth
            this.isAntiAlias = isAntiAlias
        }
    }

    private fun init() {
        mHeight = height;
        mWidth = width;
        mPadding = 50;
        mCentreX = mWidth / 2;
        mCentreY = mHeight / 2;
        mMinimum = min(mHeight, mWidth);
        mRadius = mMinimum / 2 - mPadding;
        mAngle = ((Math.PI / 30) - (Math.PI / 2))
        mHourHandSize = mRadius - mRadius / 2
        mHandSize = mRadius - mRadius / 4
        mNumbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        mIsInit = true
    }

    private fun drawHands(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()

        mHour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        mHour =
            if (mHour > 12) mHour - 12 else mHour //convert to 12hour                                                     format from 24 hour format

        mMinute = calendar.get(Calendar.MINUTE).toFloat()

        mSecond = calendar.get(Calendar.SECOND).toFloat()

        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute, mSecond)
        drawSecondsHand(canvas, mSecond)
    }


    private fun drawMinuteHand(canvas: Canvas, minutes: Float, seconds: Float) {
        mPaint!!.reset()

        setPaintAttributes(mColorHandMinutes, Paint.Style.STROKE, mSizeHandMinutes, true)

        val minSecond = (((minutes * 60) + seconds) / 60)

        mAngle = (Math.PI * minSecond / 30 - Math.PI / 2)

        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * (mHourHandSize + 40)).toFloat(),
            (mCentreY + sin(mAngle) * (mHourHandSize + 40)).toFloat(),
            mPaint!!
        )
    }

    private fun drawHourHand(canvas: Canvas, location: Double) {

        mPaint!!.reset()

        setPaintAttributes(mColorHandHours, Paint.Style.STROKE, mSizeHandHours, true)

        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * mHourHandSize).toFloat(),
            (mCentreY + sin(mAngle) * mHourHandSize).toFloat(),
            mPaint!!
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {

        mPaint!!.reset()

        setPaintAttributes(mColorHandSeconds, Paint.Style.STROKE, mSizeHandSeconds, true)

        mAngle = Math.PI * location / 30 - Math.PI / 2

        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + sin(mAngle) * mHandSize).toFloat(),
            mPaint!!
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        mPaint!!.textSize = 50f

        mPaint?.color = Color.WHITE

        for (number in mNumbers!!) {
            val num = number.toString()
            mPaint!!.getTextBounds(num, 0, num.length, mRect)
            val angle = (Math.PI / 6 * (number - 3))
            val x = (mCentreX + cos(angle) * (mRadius - 50) - mRect!!.width() / 2).toInt()
            val y = (mCentreY + sin(angle) * (mRadius - 50) + mRect!!.height() / 2).toInt()
            canvas.drawText(num, x.toFloat(), y.toFloat(), mPaint!!)
        }
    }

    fun setColorHandHours(color: Int) {
        mColorHandHours = color
    }

    fun setColorHandMinutes(color: Int) {
        mColorHandMinutes = color
    }

    fun setColorHandSeconds(color: Int) {
        mColorHandSeconds = color
    }

    fun setSizeHandHours(size: Float) {
        mSizeHandHours = size
    }

    fun setSizeHandMinutes(size: Float) {
        mSizeHandHours = size
    }

    fun setSizeHandSeconds(size: Float) {
        mSizeHandHours = size
    }
}