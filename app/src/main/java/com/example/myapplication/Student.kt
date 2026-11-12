package com.example.myapplication

data class Student(
    val name: String,
    val id: String,
) {
    override fun toString(): String {
        return "Name: $name, ID: $id"
    }
}