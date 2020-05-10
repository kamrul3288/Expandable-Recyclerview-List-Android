package com.iamkamrul.expandablerecyclerviewlist.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iamkamrul.expandablerecyclerviewlist.helper.ExpandableRecyclerAdapterHelper
import com.iamkamrul.expandablerecyclerviewlist.listener.ExpandCollapseListener
import com.iamkamrul.expandablerecyclerviewlist.listener.ParentListItemExpandCollapseListener
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem
import com.iamkamrul.expandablerecyclerviewlist.model.ParentWrapper
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ChildViewHolder
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ParentViewHolder
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
abstract class ExpandableRecyclerAdapter<PVH : ParentViewHolder, CVH : ChildViewHolder>:
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ParentListItemExpandCollapseListener {

    private var mParentItemList = mutableListOf<ParentListItem>()
    private var attachRecyclerViewListPool = mutableListOf<RecyclerView>()
    private var itemList = mutableListOf<Any>()

    private val typeParent = 0
    private val typeChild = 1
    private lateinit var expandCollapseListener: ExpandCollapseListener
    private lateinit var context: Context


    //----------- set view holder view-------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            typeParent -> {
                val phv: PVH = onCreateParentViewHolder(parent)
                phv.setParentListItemExpandCollapseListener(this)
                return phv
            }
            typeChild -> onCreateChildViewHolder(parent)
            else -> throw IllegalStateException("Incorrect ViewType found")
        }
    }

    abstract fun onCreateParentViewHolder(parentViewGroup: ViewGroup): PVH
    abstract fun onCreateChildViewHolder(parentViewGroup: ViewGroup): CVH

    //-------------bind view holder------------------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val listItem = getItemList(position)) {
            null -> throw IllegalStateException("Incorrect ViewHolder found")
            is ParentWrapper -> {
                val parentViewHolder: PVH = holder as PVH
                parentViewHolder.setParentItemClickToExpand()
                parentViewHolder.setExpanded(listItem.isExpanded)
                onBindParentViewHolder(parentViewHolder, position, listItem.parentListItem)
            }
            else -> onBindChildViewHolder(holder as CVH, position, listItem)

        }

    }

    abstract fun onBindParentViewHolder(parentViewHolder: PVH, position: Int, parentListItem: ParentListItem)
    abstract fun onBindChildViewHolder(childViewHolder: CVH, position: Int, childListItem: Any)

    private fun getItemList(position: Int): Any? {
        val indexInRange = position >= 0 && position < itemList.size
        return if (indexInRange) itemList[position]
        else null
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (getItemList(position)) {
            null -> throw IllegalStateException("Null object added")
            is ParentWrapper -> typeParent
            else -> typeChild
        }
    }

    fun getParentItemList(): List<ParentListItem> = mParentItemList

    //---------Expand parent list item---------------------
    override fun onParentListItemExpanded(position: Int) {
        if (getItemList(position) is ParentWrapper)
            expandParentListItem(getItemList(position) as ParentWrapper, position)
    }

    private fun expandParentListItem(parentWrapper: ParentWrapper, parentIndex: Int) {
        if (!parentWrapper.isExpanded) {
            parentWrapper.isExpanded = true
            if (parentWrapper.childListItem.isNotEmpty()) {
                parentWrapper.childListItem.forEachIndexed { index, it ->
                    itemList.add(parentIndex + index + 1, it!!)
                }
                notifyItemRangeInserted(parentIndex + 1, parentWrapper.childListItem.size)
            }
            if (::expandCollapseListener.isInitialized) {
                val expandedCountBeforePosition = getExpandedItemCount(parentIndex)
                expandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition)
            }

        }
    }

    private fun getExpandedItemCount(position: Int): Int {
        return if (position == 0) 0
        else {
            var expandCount = 0
            for (i in 0 until position) {
                if (getItemList(i) !is ParentWrapper) expandCount++
            }
            expandCount
        }
    }

    override fun onParentListItemCollapsed(position: Int) {
        if (getItemList(position) is ParentWrapper) collapseParentListItem(
            getItemList(position) as ParentWrapper,
            position
        )
    }

    private fun collapseParentListItem(parentWrapper: ParentWrapper, parentIndex: Int) {
        if (parentWrapper.isExpanded) {
            parentWrapper.isExpanded = false
            if (parentWrapper.childListItem.isNotEmpty()) {
                for (i in parentWrapper.childListItem.size - 1 downTo 0) {
                    itemList.removeAt(parentIndex + i + 1)
                }
                notifyItemRangeRemoved(parentIndex + 1, parentWrapper.childListItem.size)
            }
            if (::expandCollapseListener.isInitialized) {
                val expandedCountBeforePosition = getExpandedItemCount(parentIndex)
                expandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachRecyclerViewListPool.add(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        attachRecyclerViewListPool.remove(recyclerView)
    }

    fun setExpandCollapseListener(expandCollapseListener: ExpandCollapseListener) {
        this.expandCollapseListener = expandCollapseListener
    }

    fun setExpandableParentItemList(parentItemList:List<ParentListItem>){
        mParentItemList.addAll(parentItemList)
        itemList.addAll(ExpandableRecyclerAdapterHelper.generateParentChildItemList(parentItemList = parentItemList) as MutableList<Any>)
        notifyDataSetChanged()
    }



}