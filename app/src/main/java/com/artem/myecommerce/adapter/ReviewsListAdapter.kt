package com.artem.myecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ReviewItem

class ReviewsListAdapter(var context: Context, var reviewsList: ArrayList<ReviewItem>) : BaseAdapter() {
    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View

        if(convertView == null){
            view = inflater.inflate(R.layout.row_review, parent, false)
        } else {
            view = convertView
        }

        var reviewItem = reviewsList[position]
        //todo setup onClickListeners & setting any necessary data here

        return view
    }

    override fun getCount(): Int {
        return reviewsList.size
    }

    override fun getItem(position: Int): Any {
        return reviewsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}