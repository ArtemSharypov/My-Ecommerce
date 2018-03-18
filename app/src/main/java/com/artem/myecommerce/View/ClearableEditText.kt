package com.artem.myecommerce.View

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.artem.myecommerce.R

class ClearableEditText : AppCompatEditText, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher{

    var clearTextIcon: Drawable? = null
    var onFocusChangeListener: ClearableEditText? = null
    var onTouchListener: ClearableEditText? = null

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context)
    }

    private fun init(context: Context) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_action_cancel)
        val wrappedDrawable = DrawableCompat.wrap(drawable)

        DrawableCompat.setTint(wrappedDrawable, currentHintTextColor)
        clearTextIcon = wrappedDrawable
        clearTextIcon!!.setBounds(0, 0, clearTextIcon!!.intrinsicHeight, clearTextIcon!!.intrinsicHeight)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(this)
    }

    private fun setClearIconVisible(visible: Boolean) {
        clearTextIcon!!.setVisible(visible, false)
        var icon: Drawable? = null

        if(visible) {
            icon = clearTextIcon
        }

        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], icon, compoundDrawables[3])
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        val x = motionEvent?.x!!.toInt()

        if(clearTextIcon!!.isVisible && x > width - paddingRight - clearTextIcon!!.intrinsicWidth){
            if(motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }

            return true
        }

        return onTouchListener != null && onTouchListener!!.onTouch(view, motionEvent)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }

        if(onFocusChangeListener != null) {
            onFocusChangeListener?.onFocusChange(view, hasFocus)
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        if(isFocused) {
            setClearIconVisible(text?.isNotEmpty()!!)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}