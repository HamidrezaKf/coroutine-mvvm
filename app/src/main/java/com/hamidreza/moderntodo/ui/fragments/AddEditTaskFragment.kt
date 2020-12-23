package com.hamidreza.moderntodo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.hamidreza.moderntodo.R
import com.hamidreza.moderntodo.databinding.FragmentAddEditTaskBinding
import com.hamidreza.moderntodo.ui.viewmodels.AddEditTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private var _binding:FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditTaskBinding.bind(view)
        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "Created: ${viewModel.task?.createdDateFormatted()}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}