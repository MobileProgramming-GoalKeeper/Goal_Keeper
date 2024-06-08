package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.UserRoutine
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RoutineRepository (private val routinetable : DatabaseReference) {
    suspend fun InsertRoutine(userRoutine: UserRoutine){
        routinetable.child(userRoutine.routineNum.toString()).setValue(userRoutine)
    }

    suspend fun DeleteRoutine(userRoutine: UserRoutine){
        routinetable.child(userRoutine.routineNum.toString()).removeValue()
    }
    //루틴 이름 변경
    suspend fun UpdateroutineName(userRoutine: UserRoutine){
        routinetable.child(userRoutine.routineNum.toString()).child("routineName").setValue(userRoutine.routineName)
    }
    //알림 여부 변경
    suspend fun UpdateroutineAlert(userRoutine: UserRoutine){
        routinetable.child(userRoutine.routineNum.toString()).child("routineAlart").setValue(userRoutine.routineAlert)
    }

    fun getAllRoutine(): Flow<List<UserRoutine>> = callbackFlow {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val routineList = mutableListOf<UserRoutine>()
                for(itemSnapshot in snapshot.children){
                    val userRoutine = itemSnapshot.getValue(UserRoutine::class.java)
                    userRoutine?.let {
                        routineList.add(it)
                    }
                }
                trySend(routineList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        routinetable.addValueEventListener(listener)
        awaitClose {
            routinetable.removeEventListener(listener)
        }
    }
}