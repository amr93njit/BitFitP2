package com.example.bitfitp1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONException
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.bitfitp1.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private const val TAG = "StatsFragment"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StatsFragment : Fragment() {
    // Add these properties
    private lateinit var binding: ActivityMainBinding
    private val sleeps = mutableListOf<DisplaySleep>()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart = view.findViewById<LineChart>(R.id.chart)

        var avgFeelingTV = view.findViewById<TextView>(R.id.avgFeelingTextView)
        var avgHoursTV = view.findViewById<TextView>(R.id.avgHoursTextView)
        lifecycleScope.launch {
            val sleepDao = (requireActivity().application as SleepApplication).db.SleepDao()
            val avgFeeling = sleepDao.getFeelingAvg().firstOrNull() ?: 0.0
            val avgHours = sleepDao.getHoursAvg().firstOrNull() ?: 0.0
            avgFeelingTV.text = avgFeeling.toString()
            avgHoursTV.text = avgHours.toString()

        }
        fetchChart(chart)
    }


    private fun fetchChart(chart: LineChart) {
        val sleepDao = (requireActivity().application as SleepApplication).db.SleepDao()

        lifecycleScope.launch {
            val sleepEntries = sleepDao.getAll().firstOrNull() //!
            val entryFeeling = ArrayList<Entry>()
            val entryHours = ArrayList<Entry>()
            val labels = ArrayList<String>()

            sleepEntries?.forEachIndexed { index, entry ->
                val feeling = entry.feeling.toFloat()
                val hours = entry.hours.toFloat()
                entryFeeling.add(Entry(index.toFloat(), feeling))
                entryHours.add(Entry(index.toFloat(), hours))
            }
            val data = ArrayList<ILineDataSet>()
            data.add(LineDataSet(entryFeeling, "Feeling"))
            data.add(LineDataSet(entryHours, "Hours"))

            val lineData = LineData(data)
            chart.data = lineData
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    }

    companion object {
        fun newInstance(): StatsFragment {
            return StatsFragment()
        }
    }
}

