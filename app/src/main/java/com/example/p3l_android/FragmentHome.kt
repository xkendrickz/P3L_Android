package com.example.p3l_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.adapters.ScheduleAdapter
import com.example.p3l_android.models.ScheduleItem

class FragmentHome : Fragment() {
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        scheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        populateSchedule()
    }

    private fun setupRecyclerView() {
        scheduleAdapter = ScheduleAdapter()
        scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        scheduleRecyclerView.adapter = scheduleAdapter
    }

    private fun populateSchedule() {
        // Retrieve or generate the schedule data
        val scheduleList: List<ScheduleItem> = getScheduleData()

        // Pass the schedule data to the adapter
        scheduleAdapter.setData(scheduleList)
    }

    // Function to retrieve or generate the schedule data
    private fun getScheduleData(): List<ScheduleItem> {
        // Implement your logic here to retrieve or generate the schedule data
        // Create a list of ScheduleItem objects representing the schedule items
        return listOf(/* Create and return the schedule items */)
    }
}