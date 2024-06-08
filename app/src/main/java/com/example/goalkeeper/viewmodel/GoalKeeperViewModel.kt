package com.example.goalkeeper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.model.UserInfo
import com.example.goalkeeper.model.UserRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalKeeperViewModelFactory(
    private val groupRepository: GroupRepository,
    private val routineRepository: RoutineRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalKeeperViewModel::class.java)) {
            return GoalKeeperViewModel(
                groupRepository,
                routineRepository,
                todoRepository,
                userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GoalKeeperViewModel(
    private val groupRepository: GroupRepository,
    private val routineRepository: RoutineRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList = _todoList.asStateFlow()
    private var _userList = MutableStateFlow<List<UserInfo>>(emptyList())
    val userList = _userList.asStateFlow()
    private var _groupList = MutableStateFlow<List<TodoGroup>>(emptyList())
    val groupList = _groupList.asStateFlow()
    private var _routineList = MutableStateFlow<List<UserRoutine>>(emptyList())
    val routineList = _routineList.asStateFlow()

    var user = mutableStateOf<UserInfo?>(null)

    fun login(userID: String, userPassword: String) {
        viewModelScope.launch {
            userRepository.getAllUser().collect {
                user.value = it.firstOrNull { it.userId == userID && it.userPw == userPassword }
            }
        }
    }

    fun register(userId: String, userPassword: String, userName: String) {
        userRepository.InsertUser(UserInfo(userId, userPassword, userName))
    }

    fun updateThemeColor1() {
        userRepository.updateThemeColor1(user.value!!)
    }

    fun updateThemeColor2() {
        userRepository.updateThemeColor2(user.value!!)
    }

    fun insertTodo(todo: Todo) {
        userRepository.insertTodo(user.value!!, todo)
    }

    fun loadTodoList(userInfo: UserInfo) {
        viewModelScope.launch {
            userRepository.getUsersTodo(userInfo).collect {
                _todoList.value = it
            }
        }
    }
}