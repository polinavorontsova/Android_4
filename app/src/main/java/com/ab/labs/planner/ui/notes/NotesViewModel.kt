package com.ab.labs.planner.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.data.entity.Note
import com.ab.labs.planner.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteRepository: NoteRepository,
    val userId: Long
) : ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
            _notes.value = noteRepository.getNotes(userId)
        }
    }

    fun load() {
        viewModelScope.launch {
            _notes.value = noteRepository.getNotes(userId)
        }
    }

}