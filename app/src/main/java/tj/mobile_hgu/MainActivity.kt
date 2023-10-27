package tj.mobile_hgu

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import org.json.JSONException
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var lvOffice: ListView
    lateinit var tvGetClass: TextView
    lateinit var officeNames: ArrayList<String>
    lateinit var jsOffices: JSONArray

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.title = ""
        }
        val sessionsArray = JSONArray(jsonSessions)
        val adapter = LessonAdapter(this, sessionsArray)
        //cv
        val cvMonDay = findViewById<CardView>(R.id.cv_monday)
        val cvTuesday = findViewById<CardView>(R.id.cv_tuesday)
        val cvWednesday = findViewById<CardView>(R.id.cv_wednesday)
        val cvThursday = findViewById<CardView>(R.id.cv_thursday)
        val cvFriday = findViewById<CardView>(R.id.cv_friday)
        val cvSaturday = findViewById<CardView>(R.id.cv_saturday)


        //monday
        val listView = findViewById<ListView>(R.id.lv_monday)
        listView.adapter = adapter
        val btnMonDay = findViewById<Button>(R.id.btn_monday)
        btnMonDay.setOnClickListener {
            if (cvMonDay.visibility == View.GONE) {
                cvMonDay.visibility = View.VISIBLE
                cvTuesday.visibility=View.GONE
                cvWednesday.visibility=View.GONE
                cvThursday.visibility=View.GONE
                cvFriday.visibility=View.GONE
                cvSaturday.visibility=View.GONE
            } else {
                cvMonDay.visibility = View.GONE
            }
        }
        //tuesday
        val lvTuesday = findViewById<ListView>(R.id.lv_tuesday)
        lvTuesday.adapter = adapter
        val btnTuesday = findViewById<Button>(R.id.btn_tuesday)
        btnTuesday.setOnClickListener {
            if (cvTuesday.visibility == View.GONE) {
                cvTuesday.visibility = View.VISIBLE
                cvMonDay.visibility = View.GONE
                cvWednesday.visibility=View.GONE
                cvThursday.visibility=View.GONE
                cvFriday.visibility=View.GONE
                cvSaturday.visibility=View.GONE
            } else {
                cvTuesday.visibility = View.GONE
            }
        }
        //Wednesday
        val lvWednesday = findViewById<ListView>(R.id.lv_wednesday)
        lvWednesday.adapter = adapter
        val btnWednesday = findViewById<Button>(R.id.btn_wednesday)
        btnWednesday.setOnClickListener {
            if (cvWednesday.visibility == View.GONE) {
                cvWednesday.visibility = View.VISIBLE
                cvMonDay.visibility = View.GONE
                cvTuesday.visibility=View.GONE
                cvThursday.visibility=View.GONE
                cvFriday.visibility=View.GONE
                cvSaturday.visibility=View.GONE
            } else {
                cvWednesday.visibility = View.GONE
            }
        }
        //Thursday
        val lvThursday = findViewById<ListView>(R.id.lv_thursday)
        lvThursday.adapter = adapter
        val btnThursday = findViewById<Button>(R.id.btn_thursday)
        btnThursday.setOnClickListener {
            if (cvThursday.visibility == View.GONE) {
                cvThursday.visibility = View.VISIBLE
                cvMonDay.visibility = View.GONE
                cvTuesday.visibility=View.GONE
                cvWednesday.visibility=View.GONE
                cvFriday.visibility=View.GONE
                cvSaturday.visibility=View.GONE
            } else {
                cvThursday.visibility = View.GONE
            }
        }
//Friday
        val lvFriday = findViewById<ListView>(R.id.lv_friday)
        lvFriday.adapter = adapter
        val btnFriday = findViewById<Button>(R.id.btn_friday)
        btnFriday.setOnClickListener {
            if (cvFriday.visibility == View.GONE) {
                cvFriday.visibility = View.VISIBLE
                cvMonDay.visibility = View.GONE
                cvTuesday.visibility=View.GONE
                cvWednesday.visibility=View.GONE
                cvThursday.visibility=View.GONE
                cvSaturday.visibility=View.GONE
            } else {
                cvFriday.visibility = View.GONE
            }
        }
//Saturday
        val lvSaturday = findViewById<ListView>(R.id.lv_saturday)
        lvSaturday.adapter = adapter
        val btnSaturday = findViewById<Button>(R.id.btn_saturday)
        btnSaturday.setOnClickListener {
            if (cvSaturday.visibility == View.GONE) {
                cvSaturday.visibility = View.VISIBLE
                cvMonDay.visibility = View.GONE
                cvTuesday.visibility=View.GONE
                cvWednesday.visibility=View.GONE
                cvThursday.visibility=View.GONE
                cvFriday.visibility=View.GONE
            } else {
                cvSaturday.visibility = View.GONE
            }
        }
        tvGetClass = findViewById(R.id.tv_get_class)
        tvGetClass.setOnClickListener {
            if (jsOffices.length() > 1) {
                openDialogOffice()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openDialogOffice() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.activity_group_payment, null)
        bottomSheetView.findViewById<View>(R.id.toolbar).visibility = View.GONE
        bottomSheetView.setBackgroundResource(R.drawable.bottom_sheet_bg)
        bottomSheetDialog.setContentView(bottomSheetView)
        lvOffice = bottomSheetView.findViewById<ListView>(R.id.gvPayment)
        officeNames = ArrayList<String>()
        for (i in 0 until jsOffices.length()) {
            try {
                officeNames.add(jsOffices.getJSONObject(i).getString("Value"))
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, officeNames)
        lvOffice.adapter = adapter
        lvOffice.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            try {
                tvGetClass.text = jsOffices.getJSONObject(position)?.getString("Value")
                bottomSheetDialog.dismiss()
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }
        val search = bottomSheetView.findViewById<EditText>(R.id.search)
        search.hint = "Найти"
        search.filters = arrayOf<InputFilter>(LengthFilter(40))
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (search.text.toString() == "") {
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        officeNames
                    )
                    lvOffice.adapter = adapter
                    return
                }
                getOfficeSearch(jsOffices, search.text.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val btnClear = bottomSheetView.findViewById<Button>(R.id.calc_clear_txt_Prise)
        btnClear.setOnClickListener { v: View? ->
            search.setText(
                ""
            )
        }
        bottomSheetDialog.show()
    }

    private fun getOfficeSearch(jsOffices: JSONArray, search: String) {
        lvOffice.adapter = null
        val searchNames = java.util.ArrayList<String>()
        for (i in 0 until jsOffices.length()) {
            try {
                if (jsOffices.getJSONObject(i).getString("Value").lowercase(Locale.getDefault())
                        .contains(search.lowercase(Locale.getDefault()))
                ) {
                    searchNames.add(jsOffices.getJSONObject(i).getString("Value"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchNames)
        lvOffice.adapter = adapter
    }

}