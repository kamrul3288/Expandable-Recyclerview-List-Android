package com.iamkamrul.expandablerecyclerviewlist.model

interface ParentListItem {
    fun getChildItemList():List<*>
    fun isInitiallyExpanded():Boolean
}