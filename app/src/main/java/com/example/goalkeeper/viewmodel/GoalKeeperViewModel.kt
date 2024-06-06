package com.example.goalkeeper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.model.UserInfo
import com.example.goalkeeper.model.UserRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoalKeeperViewModel(
    private val grouprepository: GroupRepository,
    private val routineRepository: RoutineRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository ): ViewModel() {

    private var _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val itemList = _todoList.asStateFlow()
    private var _userList = MutableStateFlow<List<UserInfo>>(emptyList())
    val userList = _userList.asStateFlow()
    private var _groupList = MutableStateFlow<List<TodoGroup>>(emptyList())
    val groupList = _groupList.asStateFlow()
    private var _routineList = MutableStateFlow<List<UserRoutine>>(emptyList())
    val routineList = _routineList.asStateFlow()


        val userID = "user"
    //    val userPassword = "1234"

        var themeColor1 = mutableStateOf(Color.Red)
        var themeColor2 = mutableStateOf(Color.Green)

        fun login(userID: String, userPassword: String): Boolean {
    //        return this.userID == userID && this.userPassword == userPassword
            return true
        }

        fun register(userID: String, userPassword: String): Boolean {
            return true
        }

    }