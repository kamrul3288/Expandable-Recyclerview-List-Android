package com.iamkamrul.expandablerecyclerviewlistsample.model

import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem

data class Category(val name:String,val movieList:List<CategoryList>) : ParentListItem {
    override fun getChildItemList(): List<*> = movieList
    override fun isInitiallyExpanded(): Boolean = false
}