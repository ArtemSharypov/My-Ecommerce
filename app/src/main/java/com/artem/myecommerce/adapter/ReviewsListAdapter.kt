package com.artem.myecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ReviewItem
import kotlinx.android.synthetic.main.row_review.view.*

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

        view.row_review_tv_reviewers_name.text = reviewItem.reviewersName
        view.row_review_tv_date_created.text = reviewItem.date.toString()
        view.row_review_tv_review_title.text = reviewItem.reviewTitle
        view.row_review_tv_num_stars.text = reviewItem.rating.toString()
        view.row_review_tv_review_description.text = reviewItem.reviewDescription

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