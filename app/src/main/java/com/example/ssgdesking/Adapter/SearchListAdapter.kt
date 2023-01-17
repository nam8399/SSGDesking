package com.example.ssgdesking.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ssgdesking.Data.SearchData
import com.example.ssgdesking.R

class SearchListAdapter (val context: Context, val searchList: ArrayList<SearchData>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_search, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val name = view.findViewById<TextView>(R.id.name)
        val floor = view.findViewById<TextView>(R.id.floor)
        val seat = view.findViewById<TextView>(R.id.seat)
        val emp = view.findViewById<TextView>(R.id.emp)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val searchdata = searchList[position]
        name.text = searchdata.ename
        floor.text = searchdata.floor
        seat.text = searchdata.section + "-" + searchdata.location
        emp.text = searchdata.dname

        return view
    }

    override fun getItem(position: Int): Any {
        return searchList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return searchList.size
    }
}