package com.iamkamrul.expandablerecyclerviewlistsample.viewholder

import android.view.View
import android.widget.TextView
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ChildViewHolder
import com.iamkamrul.expandablerecyclerviewlistsample.R
import com.iamkamrul.expandablerecyclerviewlistsample.model.CategoryList

class CategoryListViewHolder(view:View) : ChildViewHolder(view){
    fun bind(categoryList : CategoryList){
        itemView.findViewById<TextView>(R.id.nameTv).text = categoryList.name
    }
}