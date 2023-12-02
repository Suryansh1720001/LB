package com.example.legal_bridge.ui.account_details.accountSections

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.legal_bridge.R

class FAQAdapter(private val context: Context, private val listData: HashMap<String, List<String>>) :
    BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return listData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listData.values.elementAt(groupPosition).size
    }

//    override fun getGroup(groupPosition: Int): String {
//        return listData.keys.elementAt(groupPosition)
//    }
//
//    override fun getChild(groupPosition: Int, childPosition: Int): String {
//        return listData.values.elementAt(groupPosition)[childPosition]
//    }


    override fun getGroup(groupPosition: Int): String {
        return listData.keys.elementAt(groupPosition) // This retrieves the question as String
    }

//    override fun getChild(groupPosition: Int, childPosition: Int): String {
//        val question = getGroup(groupPosition)
//        return listData[question]?.get(childPosition) ?: "" // This retrieves the answer as String
//    }
    override fun getChild(groupPosition: Int, childPosition: Int): String {
        val question = getGroup(groupPosition)
        return listData[question]?.get(childPosition) ?: ""
    }


    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val groupView: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.group_layout, null)
        val questionTextView: TextView = groupView.findViewById(R.id.groupTextView)

        val question = getGroup(groupPosition)
        questionTextView.text = question

        return groupView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val childView: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.child_layout, null)
        val answerTextView: TextView = childView.findViewById(R.id.answerContentTextView)

        val answer = getChild(groupPosition, childPosition)
        answerTextView.text = answer

        return childView
    }
}
