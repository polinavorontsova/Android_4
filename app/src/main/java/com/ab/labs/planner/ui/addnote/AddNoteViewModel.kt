package com.ab.labs.planner.ui.addnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.core.viewmodel.SingleLiveEvent
import com.ab.labs.planner.data.entity.Note
import com.ab.labs.planner.data.repository.NoteRepository
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val noteRepository: NoteRepository,
    private var id: Long
) : ViewModel() {

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    private val _savedNote = SingleLiveEvent<Boolean>()
    val savedNote: LiveData<Boolean> = _savedNote

    init {
        if (id > 0) {
            viewModelScope.launch {
                val notes = noteRepository.getNote(id)
                if (notes.isNotEmpty()) {
                    _note.value = notes[0]
                }
            }
        }
    }

    fun save(note: Note) {
        viewModelScope.launch {
            if (id > 0) {
                note.id = id
                noteRepository.update(note)
            } else {
                id = noteRepository.insert(note)
                note.id = id
            }
            _note.value = note
            _savedNote.value = true
        }
    }

}