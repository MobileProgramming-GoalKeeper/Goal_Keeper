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

class UserRepository(private val usertable : DatabaseReference) {

    suspend fun InsertUser(userInfo: UserInfo){
        usertable.child(userInfo.userId.toString()).setValue(userInfo)
    }

    suspend fun UpdatePostPonedNum(userInfo: UserInfo){
        usertable.child(userInfo.userId.toString()).child("postponedNum").setValue(userInfo.postponedNum)
    }

    //UserInfo 나머지 정보는 update 될 일 없을 것 같아 추가하지 않았습니다. +delete도 없습니다!

    //모든 유저 리스트로 가져옴.
    fun getAllUser(): Flow<List<UserInfo>> = callbackFlow {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfoList = mutableListOf<UserInfo>()
                for(itemSnapshot in snapshot.children){
                    val userInfo = itemSnapshot.getValue(UserInfo::class.java)
                    userInfo?.let {
                        userInfoList.add(it)
                    }
                }
                trySend(userInfoList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        usertable.addValueEventListener(listener)
        awaitClose {
            usertable.removeEventListener(listener)
        }
    }






}