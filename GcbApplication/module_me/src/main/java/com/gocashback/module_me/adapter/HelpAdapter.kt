package com.gocashback.module_me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.gocashback.lib_common.network.model.help.HelpItemModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 10:25
 */
class HelpAdapter(private var list: List<HelpItemModel>, private val context: Context) : BaseExpandableListAdapter() {


    override fun getGroupCount(): Int {
        return list.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return list[groupPosition].help?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any? {
        return list[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return list[groupPosition].help?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val holder: GroupViewHolder?
        val view: View?
        if (convertView == null) {
            holder = GroupViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_help_group, parent, false)
            holder.tvHelpGroup = view.findViewById(R.id.tv_help_group)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as GroupViewHolder
        }

        holder.tvHelpGroup?.text = list[groupPosition].name

        return view!!
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val holder: ChildViewHolder?
        val view: View?
        if (convertView == null) {
            holder = ChildViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_help_child, parent, false)
            holder.tvHelpChild = view.findViewById(R.id.tv_help_child)
            holder.tvNew = view.findViewById(R.id.tv_wx_new)
            holder.tvRight = view.findViewById(R.id.tv_right)
            holder.ivRight = view.findViewById(R.id.iv_right_arrow)
            holder.viewLine = view.findViewById(R.id.view_line)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ChildViewHolder
        }

        holder.tvHelpChild?.text = list[groupPosition].help?.get(childPosition)?.title
        holder.tvRight?.text = list[groupPosition].help?.get(childPosition)?.right
        if (childPosition + 1 == list[groupPosition].help?.size) {
            holder.viewLine?.visibility = View.GONE
        } else {
            holder.viewLine?.visibility = View.VISIBLE
        }

        if (groupPosition == list.size - 1) {
            holder.tvRight?.visibility = View.VISIBLE
            holder.ivRight?.visibility = View.GONE

            if (childPosition + 1 == list[groupPosition].help?.size) {
                holder.tvNew?.visibility = View.VISIBLE
            } else {
                holder.tvNew?.visibility = View.GONE
            }

        } else {
            holder.tvNew?.visibility = View.GONE
            holder.tvRight?.visibility = View.GONE
            holder.ivRight?.visibility = View.VISIBLE
        }

        return view!!
    }


    internal inner class GroupViewHolder {
        var tvHelpGroup: TextView? = null
    }

    internal inner class ChildViewHolder {
        var tvHelpChild: TextView? = null
        var tvNew: TextView? = null
        var tvRight: TextView? = null
        var ivRight: ImageView? = null
        var viewLine: View? = null

    }

}