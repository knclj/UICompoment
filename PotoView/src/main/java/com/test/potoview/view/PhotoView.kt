package com.test.potoview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import com.test.potoview.R
import com.test.potoview.utils.BitmapUtils

/**
 *
 */
public class PhotoView:View{

    companion object{
        const val OVER_SCALE_FACTOR = 1.5f
    }

    val IMAGE_WIDTH = BitmapUtils.dpToPix(300f)
    lateinit var mPaint:Paint
    lateinit var mBitmap: Bitmap
    private var orignalOffsetX:Float = 0.0f
    private var orignaloffsetY:Float = 0.0f
    var smallScale:Float = 0f
    var bigScale:Float = 0f
   private  var currentScale = 0.0f
    lateinit var gestureDetector:GestureDetector
    var isEnlarger:Boolean = false
    private var scaleAnimator:ObjectAnimator?= null
    private var offsetX = 0f
    private var offsetY = 0f
    private var overScroller:OverScroller? = null
    private var photoScaleGestureDetector:ScaleGestureDetector? = null

    constructor(context:Context,attributeSet: AttributeSet):super(context,attributeSet){
        init(context)
    }
    private fun init(context: Context){
        mBitmap = BitmapUtils.getBitmapFromRes(resources, R.drawable.photo2,IMAGE_WIDTH)!!
        mPaint = Paint()
        mPaint.isAntiAlias = true
        gestureDetector = GestureDetector(context,PhotoGestureLister())
        overScroller = OverScroller(context)
        photoScaleGestureDetector = ScaleGestureDetector(context,scaleListener())
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = photoScaleGestureDetector?.onTouchEvent(event)
        if(!photoScaleGestureDetector?.isInProgress!!){
            result =  gestureDetector.onTouchEvent(event)
        }
       return result!!
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        orignalOffsetX = (width - mBitmap.width)/2f
        orignaloffsetY = (height - mBitmap.height)/2f
        if(mBitmap.width/mBitmap.height > width/height){
            smallScale = width.toFloat().div(mBitmap.width)
            bigScale = height.toFloat().div(mBitmap.height)*OVER_SCALE_FACTOR
        }else{
            smallScale = height.toFloat().div(mBitmap.height)
            bigScale = width.toFloat().div(mBitmap.width)*OVER_SCALE_FACTOR
        }
        currentScale = smallScale
    }

    private fun fixOffset(){
        offsetX = offsetX.coerceAtMost((mBitmap.width*bigScale - width) / 2)
        offsetX = offsetX.coerceAtLeast(-((mBitmap.width*bigScale - width) / 2))
        offsetY = offsetY.coerceAtMost((mBitmap.height*bigScale - height) / 2)
        offsetY = offsetY.coerceAtLeast(-((mBitmap.height*bigScale - height) / 2))
    }

    override fun onDraw(canvas: Canvas?) {
        var scaleFaction = (currentScale - smallScale)/(bigScale - smallScale);
        canvas?.translate(offsetX*scaleFaction,offsetY*scaleFaction)
        canvas?.scale(currentScale,currentScale,width/2f,height/2f)
        canvas?.drawBitmap(mBitmap,orignalOffsetX,orignaloffsetY,mPaint)
    }


    inner class PhotoGestureLister: GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            isEnlarger = !isEnlarger
            if(isEnlarger){
                offsetX = (e!!.x - width/2f) - (e.x - width/2f)*bigScale/smallScale
                offsetY = (e.y - height/2f) - (e.y - height/2f)*bigScale/smallScale
                fixOffset()
                getScaleAnimator().start()
            }else{
                getScaleAnimator().reverse()
            }
            return super.onDoubleTap(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            //必须要返回true才能响应事件
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if(isEnlarger){
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if(isEnlarger){
                overScroller?.fling(offsetX.toInt(),offsetY.toInt(),
                    velocityX.toInt(),velocityY.toInt(),
                    (-(mBitmap.width*bigScale-width)/2f).toInt(),
                    ((mBitmap.width*bigScale-width)/2f).toInt(),
                    (-(mBitmap.height*bigScale-height)/2f).toInt(),
                    ((mBitmap.height*bigScale-height)/2f).toInt()
                ,300,300)
                postOnAnimation(FlingRunnable())
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

    }

    inner class scaleListener:ScaleGestureDetector.OnScaleGestureListener{
        var initScale= 0f;
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            initScale = currentScale
            return true
        }


        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if ((currentScale > smallScale && !isEnlarger)
                || (currentScale == smallScale && !isEnlarger)) {
                isEnlarger = !isEnlarger
            }
            if (detector != null ) {
                currentScale = initScale*detector.scaleFactor
            }
            invalidate()
            return false
        }

    }

    inner class FlingRunnable:Runnable{
        override fun run() {
            if(overScroller?.computeScrollOffset()!!){
                offsetX = overScroller?.currX!!.toFloat()
                offsetY = overScroller?.currY!!.toFloat()
                invalidate()
                postOnAnimation(this)
            }
        }

    }

    fun getCurrentScale():Float{
        return this.currentScale
    }

    fun setCurrentScale(scale:Float){
        this.currentScale = scale
        invalidate()
    }
    fun getScaleAnimator():ObjectAnimator{
        if(scaleAnimator == null){
            scaleAnimator = ObjectAnimator.ofFloat(this,"currentScale",0f)
        }
        scaleAnimator!!.setFloatValues(smallScale,bigScale)

        return scaleAnimator as ObjectAnimator
    }


}