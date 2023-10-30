package tj.mobile_hgu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class LessonAdapter( private val sessions: JSONArray) : BaseAdapter() {

    override fun getCount(): Int {
        return sessions.length()
    }

    override fun getItem(position: Int): Any {
        return sessions.getJSONObject(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        }

        val lessonTextView = view!!.findViewById<TextView>(R.id.tv_lesson)
        val timeTextView = view.findViewById<TextView>(R.id.tv_time)
        val tvIndex = view.findViewById<TextView>(R.id.tv_index)
        val teacherTextView = view.findViewById<TextView>(R.id.tv_teacher)

        val session = sessions.getJSONObject(position)
        tvIndex.text = (position+1).toString()
        lessonTextView.text = session.getString("lesson")
        timeTextView.text = session.getString("time")
        teacherTextView.text = session.getString("teacher")

        return view
    }
}
