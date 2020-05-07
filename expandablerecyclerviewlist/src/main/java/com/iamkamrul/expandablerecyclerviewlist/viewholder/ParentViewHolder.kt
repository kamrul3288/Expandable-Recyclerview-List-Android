package com.iamkamrul.expandablerecyclerviewlist.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iamkamrul.expandablerecyclerviewlist.listener.ParentListItemExpandCollapseListener

open class ParentViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private var expanded = false
    private lateinit var parentListItemExpandCollapseListener: ParentListItemExpandCollapseListener

    //-------- set expand child view when parent item clicked--------
    fun setParentItemClickToExpand(){
        itemView.setOnClickListener(this)
    }

    //-------the parent item is expanded or not----------
    open fun setExpanded(expanded:Boolean){
        this.expanded = expanded
    }

    //------- controls the expand and collapse arrow with animation-------
    open fun onExpansionToggled(expanded:Boolean){

    }

    fun setParentListItemExpandCollapseListener(parentListItemExpandCollapseListener: ParentListItemExpandCollapseListener){
        this.parentListItemExpandCollapseListener = parentListItemExpandCollapseListener
    }

    override fun onClick(v: View?) {
        if (expanded)collapseView()
        else expandView()
    }

    private fun collapseView(){
        setExpanded(false)
        onExpansionToggled(true)
        if (::parentListItemExpandCollapseListener.isInitialized)
            parentListItemExpandCollapseListener.onParentListItemCollapsed(adapterPosition)
    }

    private fun expandView(){
        setExpanded(true)
        onExpansionToggled(false)
        if (::parentListItemExpandCollapseListener.isInitialized)
            parentListItemExpandCollapseListener.onParentListItemExpanded(adapterPosition)
    }

}