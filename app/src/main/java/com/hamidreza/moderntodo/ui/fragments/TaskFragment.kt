package com.hamidreza.moderntodo.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamidreza.moderntodo.R
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.databinding.FragmentTaskBinding
import com.hamidreza.moderntodo.ui.adapters.TaskAdapter
import com.hamidreza.moderntodo.ui.viewmodels.TaskViewModel
import com.hamidreza.moderntodo.utils.SortOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    lateinit var taskAdapter: TaskAdapter
    private val viewModel: TaskViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTaskBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModel.getTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        binding.fabAddTask.setOnClickListener {

        }
        setHasOptionsMenu(true)
    }


    fun setUpRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_task_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText.orEmpty()
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.action_delete_all_completed_tasks -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}