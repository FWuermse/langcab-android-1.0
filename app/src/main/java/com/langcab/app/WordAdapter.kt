package com.langcab.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(private val dataSet: List<Word>) :
        RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chineseView: TextView = view.findViewById(R.id.chinese)
        val pinyinView: TextView = view.findViewById(R.id.pinyin)
        val englishView: TextView = view.findViewById(R.id.english)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.word_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.chineseView.text = dataSet[position].wordChinese
        viewHolder.pinyinView.text = dataSet[position].wordPinyin
        viewHolder.englishView.text = dataSet[position].wordEnglish
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
