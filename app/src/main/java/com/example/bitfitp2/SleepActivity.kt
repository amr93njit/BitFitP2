package com.example.bitfitp1

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SleepActivity : AppCompatActivity() {
    private lateinit var datePicker: DatePicker
    private lateinit var hoursSlider: SeekBar
    private lateinit var feelingSlider: SeekBar
    private lateinit var notesEditText: EditText

    private var selectedDate = ""
    private var sleepHours = 0
    private var feelingValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        datePicker = findViewById(R.id.datePicker)
        hoursSlider = findViewById(R.id.hoursSlider)
        feelingSlider = findViewById(R.id.feelingSlider)
        notesEditText = findViewById(R.id.notesEditText)
        val submitButton= findViewById<Button>(R.id.submitButton)

        val dateSetListener = DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            selectedDate = "${monthOfYear + 1}/$dayOfMonth/$year"
        }

        datePicker.init(Calendar.getInstance()[Calendar.YEAR], Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH], dateSetListener)

        hoursSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, user: Boolean) {
                sleepHours = progress
            }
            //why do I even need this if I don't use it? bloatcode moment
            override fun onStartTrackingTouch(seekBar: SeekBar?) {} override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        feelingSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, user: Boolean) {
                feelingValue = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {} override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        submitButton.setOnClickListener {
            var date = selectedDate.toString()
            var hours = sleepHours
            var feeling = feelingValue
            var notes = notesEditText.text.toString()
            hideKeyboard()
            lifecycleScope.launch(IO) {
                (application as SleepApplication).db.SleepDao().insertAll(
                    listOf(
                        SleepEntity(
                            date = date,
                            hours = hours,
                            feeling = feeling,
                            notes = notes,
                        )
                    )
                )
            }

            finish()
        }
    }
    //https://stackoverflow.com/a/45857155
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
