package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.Todo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TodoRepository(private val todotable: DatabaseReference) {

    //키값(String)자동생성
    suspend fun insertTodo(todo: Todo): String? {
        val newTodoRef = todotable.push()
        val todoId = newTodoRef.key

        todoId?.let {
            val newTodo = todo.copy(todoId = it)
            newTodoRef.setValue(newTodo)
        }
        return todoId
    }

    suspend fun deleteTodo(todo: Todo) {
        todotable.child(todo.todoId.toString()).removeValue()
    }

    //속한 그룹 변경
    suspend fun updateTodoGroup(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("groupNum").setValue(todo.groupId)
    }

    //투두 이름 변경
    suspend fun updateTodoName(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("todoName").setValue(todo.todoName)
    }

    //투두 날짜 변경
    suspend fun UpdatetodoDate(todo: Todo, newDate: String) {
        todotable.child(todo.todoId.toString()).child("todoDate").setValue(newDate)
    }

    //투두 시간 변경
    suspend fun UpdatetodoTime(todo: Todo, newStartAt: String, newEndAt: String) {
        todotable.child(todo.todoId.toString()).child("todoStartAt").setValue(newStartAt)
        todotable.child(todo.todoId.toString()).child("todoEndAt").setValue(newEndAt)
    }

    //투두 알림 여부 변경
    fun updatetodoAlert(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("todoAlart").setValue(todo.todoAlert)
    }

    //투두 메모 내용 변경
    fun updatetodoMemo(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("todoMemo").setValue(todo.todoMemo)
    }

    //투두 완료 여부 변경
    fun updatetodoDone(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("todoDone").setValue(todo.todoDone)
    }

    suspend fun UpdatePostPonedNum(todo: Todo) {
        todotable.child(todo.todoId.toString()).child("postponedNum").setValue(todo.postponedNum+1)
    }

    //모든 투두 리스트로 가져옴.
    //혹시 그룹별로 가져오는 기능 등 추가 함수 필요하면 말씀해주시면 추가하겠습니다 !
    fun getAllTodo(): Flow<List<Todo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoList = mutableListOf<Todo>()
                for (itemSnapshot in snapshot.children) {
                    val todo = itemSnapshot.getValue(Todo::class.java)
                    todo?.let {
                        todoList.add(it)
                    }
                }
                trySend(todoList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        todotable.addValueEventListener(listener)
        awaitClose {
            todotable.removeEventListener(listener)
        }
    }
}