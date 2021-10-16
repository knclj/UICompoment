package com.test.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEachIndexed

/**
 * 自定义流布局
 */
class  FlowLayout(context: Context, attributeSet: AttributeSet?) :
    ViewGroup(context, attributeSet) {

    private val verticalSpace = 10
    private val horizontalSpace = 10
    //每行的view
   private var lineHeightViews:MutableList<Int> = mutableListOf()
    //所有行的view
    private val linesViews:MutableList<MutableList<View>> = mutableListOf()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        lineHeightViews.clear()
        linesViews.clear()
        val parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)

        val parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)

        val pLeft = paddingLeft
        val pRight = paddingRight
        val pTop = paddingTop
        val pBottom = paddingBottom

        var width = 0
        var height = 0

        var lineWidth = 0
        var lineHeight = 0

        var lineViews:MutableList<View> = mutableListOf()

        forEachIndexed { index, childrenView  ->

           val childLayoutParams =   childrenView.layoutParams
            val  childWidthMeasureSpec  = getChildMeasureSpec(widthMeasureSpec,pLeft+pRight,childLayoutParams.width)
            val  childHeightMeasureSpec  = getChildMeasureSpec(heightMeasureSpec,pTop+pBottom,childLayoutParams.height)
           if(childrenView.visibility != View.GONE){
               //测量子view
               childrenView.measure(childWidthMeasureSpec,childHeightMeasureSpec)
               val childWidth = childrenView.measuredWidth
               val childHeight = childrenView.measuredHeight
               //换行
               if(lineWidth+childWidth + horizontalSpace> parentWidthSize) {
                   linesViews.add(lineViews)
                   lineHeightViews.add(lineHeight)
                   //最大宽度
                   width = width.coerceAtLeast(lineWidth + horizontalSpace)
                   //解决高度
                   height += lineHeight + verticalSpace

                   lineViews = mutableListOf()
                   lineWidth = 0
                   lineHeight = 0
               }
               //处理宽度
               lineWidth += childWidth + horizontalSpace;
               //高度
               lineHeight = lineHeight.coerceAtLeast(childHeight)
               lineViews.add(childrenView)
               //处理最后一行
               if(index == childCount -1){
                   linesViews.add(lineViews)
                   lineHeightViews.add(lineHeight)
                   width = width.coerceAtLeast(lineWidth + horizontalSpace)
                   //解决高度
                   height += lineHeight + verticalSpace
               }
           }
        }

       val realWidth = if(parentWidthMode == MeasureSpec.EXACTLY){parentWidthSize} else {width}
        val realHeight = if(parentHeightMode == MeasureSpec.EXACTLY){parentHeightSize} else {height}
        setMeasuredDimension(realWidth,realHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        //行
        linesViews.forEachIndexed { index, mutableList ->
            val lineHeight =  lineHeightViews[index]
            mutableList.forEach {  view ->
                view.layout(currentLeft, currentTop, currentLeft+view.measuredWidth, currentTop+view.measuredHeight)
                currentLeft += view.measuredWidth + horizontalSpace
            }
            currentTop += lineHeight + verticalSpace
            currentLeft = paddingLeft
        }
    }
}