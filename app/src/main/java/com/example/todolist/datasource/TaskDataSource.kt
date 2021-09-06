package com.example.todolist.datasource

import com.example.todolist.model.Task

object TaskDataSource {

    private val list = arrayListOf<Task>()

    fun getList(): List<Task> = list.map { it }

    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        } else {
            val oldTask = findById(task.id)
            val index = list.indexOf(oldTask)
            list.removeAt(index)
            list.add(index, task)
        }

    }

    fun findById(taskId: Int): Task? {
        return list.find { it.id == taskId }
    }

    fun deleteTask(task: Task) {
        list.remove(task)
    }
}
