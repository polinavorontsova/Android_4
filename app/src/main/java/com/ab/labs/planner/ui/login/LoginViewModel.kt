package com.ab.labs.planner.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.core.viewmodel.SingleLiveEvent
import com.ab.labs.planner.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    private val _loginError = SingleLiveEvent<Boolean>()
    val loginError: LiveData<Boolean> = _loginError

    fun login(name: String, password: String) {
        viewModelScope.launch {
            val users = userRepository.getUser(name, password)
            if (users.isEmpty()) {
                _loginError.value = true
            } else {
                _userId.value = users[0].id
            }
        }
    }

}