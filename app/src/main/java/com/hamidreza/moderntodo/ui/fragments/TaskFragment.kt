package com.hamidreza.moderntodo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamidreza.moderntodo.R
import com.hamidreza.moderntodo.databinding.FragmentTaskBinding
import com.hamidreza.moderntodo.ui.adapters.TaskAdapter
import com.hamidreza.moderntodo.ui.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private var _binding : FragmentTaskBinding? = null
    private val binding get() = _binding!!
    lateinit var taskAdapter:TaskAdapter
    private val viewModel : TaskViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTaskBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModel.getTasks.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }
    }


    fun setUpRecyclerView(){
        taskAdapter = TaskAdapter()
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}