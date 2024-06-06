package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.Todo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TodoRepository(private val todotable : DatabaseReference) {

    suspend fun InsertTodo(todo : Todo){
        todotable.child(todo.todoNum.toString()).setValue(todo)
    }

    suspend fun DeleteTodo(todo : Todo){
        todotable.child(todo.todoNum.toString()).removeValue()
    }
    //속한 그룹 변경
    suspend fun UpdateTodoGroup(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("groupNum").setValue(todo.groupNum)
    }
    //투두 이름 변경
    suspend fun UpdateTodoName(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoName").setValue(todo.todoName)
    }
    //투두 날짜 변경
    suspend fun UpdatetodoDate(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoDate").setValue(todo.todoDate)
    }
    //투두 시간 변경
    suspend fun UpdatetodoTime(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoTime").setValue(todo.todoTime)
    }
    //투두 알림 여부 변경
    suspend fun UpdatetodoAlart(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoAlart").setValue(todo.todoAlart)
    }
    //투두 메모 내용 변경
    suspend fun UpdatetodoMemo(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoMemo").setValue(todo.todoMemo)
    }
    //투두 완료 여부 변경
    suspend fun UpdatetodoDone(todo: Todo){
        todotable.child(todo.todoNum.toString()).child("todoDone").setValue(todo.todoDone)
    }

    //모든 투두 리스트로 가져옴.
    //혹시 그룹별로 가져오는 기능 등 추가 함수 필요하면 말씀해주시면 추가하겠습니다 !
    fun getAllTodo(): Flow<List<Todo>> = callbackFlow {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoList = mutableListOf<Todo>()
                for(itemSnapshot in snapshot.children){
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