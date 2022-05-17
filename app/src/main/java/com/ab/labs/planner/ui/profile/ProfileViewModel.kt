package com.ab.labs.planner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.core.viewmodel.SingleLiveEvent
import com.ab.labs.planner.data.entity.User
import com.ab.labs.planner.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    val userId: Long
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _existUser = SingleLiveEvent<Boolean>()
    val existUser: LiveData<Boolean> = _existUser

    private val _savedUser = SingleLiveEvent<Boolean>()
    val savedUser: LiveData<Boolean> = _savedUser

    init {
        viewModelScope.launch {
            val users = userRepository.getUser(userId)
            if (users.isNotEmpty()) {
                _user.value = users[0]
            }
        }
    }

    fun save(user: User) {
        viewModelScope.launch {
            val users = userRepository.existUser(name = user.name, email = user.email)
            var notExist = true
            for (u in users) {
                if (u.id != userId) {
                    notExist = false
                    break
                }
            }
            if (notExist) {
                user.id = userId
                userRepository.update(user)
                _user.value = user
                _savedUser.value = true
            } else {
                _existUser.value = true
            }
        }
    }

}