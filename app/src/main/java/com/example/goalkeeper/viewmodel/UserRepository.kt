package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository(private val usertable: DatabaseReference) {

    fun insertUser(userInfo: UserInfo) {
        usertable.child(userInfo.userId).setValue(userInfo)
    }

    fun updateThemeColor1(userInfo: UserInfo) {
        usertable.child(userInfo.userId).child("themeColor1")
            .setValue(userInfo.themeColor1)
    }

    fun updateThemeColor2(userInfo: UserInfo) {
        usertable.child(userInfo.userId).child("themeColor2")
            .setValue(userInfo.themeColor2)
    }

    fun insertTodo(userInfo: UserInfo, todo: Todo) {
        usertable.child(userInfo.userId).child("todos").child(todo.todoName).setValue(todo)
    }

    //UserInfo 나머지 정보는 update 될 일 없을 것 같아 추가하지 않았습니다. +delete도 없습니다!

    //모든 유저 리스트로 가져옴.
    fun getAllUser(): Flow<List<UserInfo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfoList = mutableListOf<UserInfo>()
                for (itemSnapshot in snapshot.children) {
                    val userInfo = itemSnapshot.getValue(UserInfo::class.java)
                    userInfo?.let {
                        userInfoList.add(it)
                    }
                }
                trySend(userInfoList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        usertable.addValueEventListener(listener)
        awaitClose {
            usertable.removeEventListener(listener)
        }
    }

    fun getUsersTodo(userInfo: UserInfo): Flow<List<Todo>> = callbackFlow {
        val query = usertable.child(userInfo.userId).child("todos")

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

            }

        }
        query.addValueEventListener(listener)
        awaitClose {
            query.removeEventListener(listener)
        }
    }
}