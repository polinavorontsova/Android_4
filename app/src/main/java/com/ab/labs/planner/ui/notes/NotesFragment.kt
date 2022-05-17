package com.ab.labs.planner.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ab.labs.planner.App
import com.ab.labs.R
import com.ab.labs.planner.core.BaseFragment
import com.ab.labs.planner.core.extension.getViewModelExt
import com.ab.labs.planner.core.extension.navigateExt
import com.ab.labs.planner.data.entity.Note
import com.ab.labs.databinding.FragmentNotesBinding

class NotesFragment : BaseFragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        notesViewModel = getViewModelExt {
            NotesViewModel(
                noteRepository = App.instance.noteRepository,
                userId = checkUserId()
            )
        }
        initRecycler()
        notesViewModel.load()
        return binding.root
    }

    override fun initFab() {
        fab.setImageResource(R.drawable.ic_add_24)
        fab.show()
        fab.setOnClickListener {
            navigateExt(NotesFragmentDirections.actionNavNotesToNavAddNote())
        }
    }

    private fun initRecycler() {
        val recyclerView = binding.list

        //recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        //recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = NotesAdapter()
        recyclerView.adapter = adapter

        adapter.shotClickListener = { item, _ ->
            navigateExt(NotesFragmentDirections.actionNavNotesToNavNote(item.id))
        }

        notesViewModel.notes.observe(viewLifecycleOwner) {
            it.let {
                adapter.setData(it as ArrayList<Note>)
                if (it.isEmpty()) {
                    binding.list.visibility = View.GONE
                    binding.emptyList.visibility = View.VISIBLE
                } else {
                    binding.list.visibility = View.VISIBLE
                    binding.emptyList.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}