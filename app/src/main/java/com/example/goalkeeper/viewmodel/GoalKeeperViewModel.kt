package com.example.goalkeeper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.goalkeeper.model.ToDoGroupColor
import com.example.goalkeeper.model.ToDoGroupIcon
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.model.UserInfo
import com.example.goalkeeper.model.UserRoutine
import com.example.goalkeeper.model.toStringFormat
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GoalKeeperViewModelFactory(
    private val dbReference: DatabaseReference,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalKeeperViewModel::class.java)) {
            return GoalKeeperViewModel(dbReference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GoalKeeperViewModel(private val dbReference: DatabaseReference) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(dbReference.child("users"))
    private lateinit var groupRepository: GroupRepository
    private lateinit var routineRepository: RoutineRepository
    private lateinit var todoRepository: TodoRepository

    private var _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList = _todoList.asStateFlow()
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
        userRepository.insertUser(UserInfo(userId, userPassword, userName))
    }

    fun updateThemeColor1() {
        userRepository.updateThemeColor1(user.value!!)
    }

    fun updateThemeColor2() {
        userRepository.updateThemeColor2(user.value!!)
    }

    fun loadTodoList(userInfo: UserInfo) {
        viewModelScope.launch {
            userRepository.getUsersTodo(userInfo).collect {
                _todoList.value = it
            }
        }
    }

    fun insertGroup(group: TodoGroup) {
        groupRepository.insertGroup(group)
    }

    fun insertRoutine(routine: UserRoutine) {
        routineRepository.insertRoutine(routine)
    }

    fun fetchTodos() {
        viewModelScope.launch {
            todoRepository.getAllTodo().collect { todos ->
                _todoList.value = todos
            }
        }
    }

    fun fetchGroups() {
        viewModelScope.launch {
            groupRepository.getAllGroup().collect { groups ->
                _groupList.value = groups
            }
        }
    }
    fun insertTodoItem(todo: Todo): String { //inserTodo 이걸로 사용해주세용
        viewModelScope.launch {
            val todoID = todoRepository.insertTodo(todo) ?: ""
            var group = groupRepository.findGroupById(todo.groupId)
            todo.todoId = todoID
            if (group != null) {
                // 찾은 그룹이 있다면 해당 그룹에 todo를 추가
                group.mainTodo.add(todo)
                groupRepository.updateGroup(group)
            } else {
                // 그룹을 찾지 못했을 경우 예외 처리
            }
            fetchTodos()
            fetchGroups()
            return@launch
        }
        return todo.todoId
    }

    suspend fun insertGroupItem(group: TodoGroup): String? {
        var groupID: String = ""
        viewModelScope.launch {
            groupID = groupRepository.InsertGroup(group).toString()
            fetchGroups()
            return@launch
        }
        return groupID
    }

    suspend fun setupTestData() {
        // Fetch current data
        fetchGroups()
        fetchTodos()

        viewModelScope.launch {
            groupList.value.forEach {
                groupRepository.DeleteGroup(it)
            }
        }.join()

        viewModelScope.launch {
            todoList.value.forEach {
                todoRepository.deleteTodo(it)
            }
        }.join()

        // Wait for deletions to complete
        _groupList.emit(emptyList())
        _todoList.emit(emptyList())


        val exTodoGroup = TodoGroup(
            groupId = "1",
            groupName = "공부",
            color = ToDoGroupColor.RED.toString(),
            icon = ToDoGroupIcon.PENCIL.toString()
        )

        var groupId = insertGroupItem(exTodoGroup)

        groupId?.let {
            val mainTodo = createMainTodoList(it)
            mainTodo.forEach { todo -> insertTodoItem(todo) }
        }

        val exTodoGroup2 = TodoGroup(
            groupId = "2",
            groupName = "위시리스트",
            color = ToDoGroupColor.PINK.toString(),
            icon = ToDoGroupIcon.SHOPPING_CART.toString()
        )
        groupId = insertGroupItem(exTodoGroup2)
        groupId?.let {
            val mainTodo = createMainTodoList(it).mapIndexed { index, todo ->
                todo.copy(todoId = (index + 5).toString())
            }
            mainTodo.forEach { todo -> insertTodoItem(todo) }
        }
    }

    fun createMainTodoList(groupId: String): List<Todo> {
        var todoEX = Todo()
        todoEX.groupId=groupId
        todoEX.todoName="치과가기"
        todoEX.todoDate= LocalDateTime.of(2024, 6, 2, 0, 0).toStringFormat(true)
        todoEX.todoStartAt = LocalDateTime.of(2024, 6, 2, 9, 0).toStringFormat(true)
        todoEX.todoEndAt = LocalDateTime.of(2024, 6, 2, 10, 30).toStringFormat(true)
        todoEX.todoMemo = "사랑니 빼야됨 ㅜㅜ"

        return listOf(
            todoEX.copy(todoId = "1", groupId = groupId),
            todoEX.copy(todoId = "2", groupId = groupId, todoName = "병원가기", todoMemo = "감기약 타오기"),
            todoEX.copy(todoId = "3", groupId = groupId, todoName = "약속 잡기", todoMemo = "친구 만나기"),
            todoEX.copy(todoId = "4", groupId = groupId, todoName = "운동하기", todoMemo = "헬스장 가기")
        )
    }

    fun updateTodo(todoId: String, newTodo: Todo) {
        val updatedList = todoList.value?.map { todo ->
            if (todo.todoId == todoId) {
                newTodo
            } else {
                todo
            }
        }
        _todoList.value = updatedList!!
    }

    fun deleteTodoItem(todo: Todo) {
        viewModelScope.launch {
            var group = groupRepository.findGroupById(todo.groupId)
            if(group!=null){
                group.mainTodo.remove(todo)
                groupRepository.updateGroup(group)
            }
            todoRepository.deleteTodo(todo)

            fetchTodos()
            fetchGroups()
        }
    }

    fun updatePostponeTodoItem(todo: Todo) {
        viewModelScope.launch {
            todoRepository.UpdatePostPonedNum(todo)
            fetchTodos()
        }
    }

    fun initRepositories(userInfo: UserInfo) {
        todoRepository =
            TodoRepository(dbReference.child("users").child(userInfo.userId).child("todos"))
        groupRepository =
            GroupRepository(dbReference.child("users").child(userInfo.userId).child("groups"))
        routineRepository =
            RoutineRepository(dbReference.child("users").child(userInfo.userId).child("routines"))
    }
}