package com.iamkamrul.expandablerecyclerviewlist.listener

interface ExpandCollapseListener{
    fun onListItemExpanded(position:Int)
    fun onListItemCollapsed(position:Int)
}