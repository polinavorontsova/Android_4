package com.ab.labs.planner.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.core.viewmodel.SingleLiveEvent
import com.ab.labs.planner.data.entity.User
import com.ab.labs.planner.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _existUser = SingleLiveEvent<Boolean>()
    val existUser: LiveData<Boolean> = _existUser

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    fun save(user: User) {
        viewModelScope.launch {
            val users = userRepository.existUser(name = user.name, email = user.email)
            if (users.isEmpty()) {
                _userId.value = userRepository.insert(user)
            } else {
                _existUser.value = true
            }
        }
    }

}