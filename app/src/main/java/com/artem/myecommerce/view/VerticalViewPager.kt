package com.artem.myecommerce.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View

class VerticalViewPager(context: Context) : ViewPager(context) {

    init {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    class VerticalPageTransformer : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if(position < -1) {
                page.alpha = 0f
            } else if(position <= 1) {
                var yPosition = position * page.height

                page.alpha = 1f
                page.translationX = page.width * -position
                page.translationY = yPosition
            } else {
                page.alpha = 0f
            }
        }
    }

    private fun swapXY(event: MotionEvent) : MotionEvent {
        var newX = (event.y / height) * width
        var newY = (event.x / width) * height

        event.setLocation(newX, newY)

        return event
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = super.onInterceptTouchEvent(swapXY(ev!!))
        swapXY(ev)

        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return return super.onTouchEvent(swapXY(ev!!))
    }
}