package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.SubTodo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SubTodoRepository(private val subTodotable: DatabaseReference) {

    //삽입
    //키값(String)자동생성
    suspend fun insertSubTodo(subtodo: SubTodo, rootId: String): String? {
        val newTodoRef = subTodotable.push()
        val subTodoId = newTodoRef.key

        subTodoId?.let {
            val newSubTodo = subtodo.copy(subTodoId = it, rootTodoId = rootId)
            newTodoRef.setValue(newSubTodo)
        }
        return subTodoId
    }

    //삭제
    suspend fun deleteSubTodo(stodo: SubTodo) {
        subTodotable.child(stodo.subTodoId.toString()).removeValue()
    }
    //이름 변경
    suspend fun updateSubName(stodo: SubTodo) {
        subTodotable.child(stodo.subTodoId.toString()).child("subName").setValue(stodo.subName)
    }

    //메모 내용 변경
    fun updateSubMemo(stodo: SubTodo) {
        subTodotable.child(stodo.subTodoId.toString()).child("subMemo").setValue(stodo.subMemo)
    }

    //완료 여부 변경
    fun updateSubDone(stodo: SubTodo) {
        subTodotable.child(stodo.subTodoId.toString()).child("subDone").setValue(stodo.subDone)
    }

    //모든 서브투두 리스트로 가져옴.
    fun getAllSubTodo(): Flow<List<SubTodo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subTodoList = mutableListOf<SubTodo>()
                for (itemSnapshot in snapshot.children) {
                    val subtodo = itemSnapshot.getValue(SubTodo::class.java)
                    subtodo?.let {
                        subTodoList.add(it)
                    }
                }
                trySend(subTodoList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        subTodotable.addValueEventListener(listener)
        awaitClose {
            subTodotable.removeEventListener(listener)
        }
    }


}