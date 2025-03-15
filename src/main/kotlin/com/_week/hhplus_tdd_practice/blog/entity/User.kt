package com._week.hhplus_tdd_practice.blog.entity

import java.time.LocalDate

class User(val name: String) {
    val todos = mutableListOf<Todo>()
    private var score = 0 // 기본 점수

    fun addTodo(todo: Todo) {
        todos.add(todo)
    }

    fun completeTodo(todo: Todo) {
        if (todos.contains(todo)) {
            score += 10 // 할 일 완료 시 점수 증가
            todos.remove(todo) // 완료된 할 일 삭제
        }
    }

    fun checkOverdueTodos() {
        val today = LocalDate.now()
        val overdueTodos = todos.filter { LocalDate.parse(it.dueDate).isBefore(today) }
        if (overdueTodos.isNotEmpty()) {
            score -= 5 // 마감 기한 초과 시 점수 차감
            todos.removeAll(overdueTodos)
        }
    }

    fun getScore(): Int = score
}

data class Todo(val title: String, val dueDate: String)
