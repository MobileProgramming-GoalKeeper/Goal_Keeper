package com.example.goalkeeper.viewmodel

import com.example.goalkeeper.model.TodoGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull

class GroupRepository (private val grouptable : DatabaseReference) {
    //키값(String)자동생성
    fun InsertGroup(todoGroup: TodoGroup): String? {
        val newGroupRef = grouptable.push()
        val groupId = newGroupRef.key

        groupId?.let {
            val newTodoGroup = todoGroup.copy(groupId = it)
            newGroupRef.setValue(newTodoGroup)
            return groupId
        }
        return null
    }

    fun insertGroup(todoGroup: TodoGroup) {
        grouptable.child(todoGroup.groupName).setValue(todoGroup)
    }

    suspend fun DeleteGroup(todoGroup: TodoGroup){
        grouptable.child(todoGroup.groupId.toString()).removeValue()
    }
    //그룹 이름 변경
    suspend fun UpdateGroupName(todoGroup: TodoGroup){
        grouptable.child(todoGroup.groupId.toString()).child("groupName").setValue(todoGroup.groupName)
    }
    suspend fun findGroupById(groupId : String) : TodoGroup? {
        val allGroups = getAllGroup().firstOrNull() ?: return null
        val group = allGroups.find { it.groupId == groupId }
        return group
    }
    suspend fun updateGroup(group: TodoGroup) {
        val groupRef = grouptable.child(group.groupId)
        groupRef.setValue(group)
    }

    fun getAllGroup(): Flow<List<TodoGroup>> = callbackFlow {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoGroupList = mutableListOf<TodoGroup>()
                for(itemSnapshot in snapshot.children){
                    val todoGroup = itemSnapshot.getValue(TodoGroup::class.java)
                    todoGroup?.let {
                        todoGroupList.add(it)
                    }
                }
                trySend(todoGroupList) //for문으로 데이터 모두 배열에 저장하고 방출
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        grouptable.addValueEventListener(listener)
        awaitClose {
            grouptable.removeEventListener(listener)
        }
    }

}