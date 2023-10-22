package com.example.bitfitp1
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.bitfitp1.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

private const val TAG = "SleepFragment"

class SleepFragment : Fragment() {
    private lateinit var sleepRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private val sleeps = mutableListOf<DisplaySleep>()
    private lateinit var sleepAdapter: SleepAdapter

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_sleep, container, false)

        sleepRecyclerView = view.findViewById(R.id.list)
        sleepAdapter = SleepAdapter(this, sleeps)
        sleepRecyclerView.adapter = sleepAdapter
        sleepRecyclerView.layoutManager = LinearLayoutManager(context).also {
            val dividerItemDecoration = DividerItemDecoration(context, 0)
            sleepRecyclerView.addItemDecoration(dividerItemDecoration)
        }
        //fetchSleep()
        return view
    }

    private var isDataLoaded = false // Add this flag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isDataLoaded) {
            fetchSleep()
            isDataLoaded = true
        }
    }
    private fun fetchSleep() {
        lifecycleScope.launch {
            (requireActivity().application as SleepApplication).db.SleepDao().getAll().collect { databaseList ->
                val mappedList = databaseList.map { entity ->
                    DisplaySleep(
                        entity.date,
                        entity.notes,
                        entity.hours.toString(),
                        entity.feeling.toString()
                    )
                }
                sleeps.clear()
                sleeps.addAll(mappedList)
                sleepAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newInstance(): SleepFragment {
            return SleepFragment()
        }
    }
}