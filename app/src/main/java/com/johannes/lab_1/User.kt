package com.johannes.lab_1

class User(
    var id: Int = 0,
    var name: String = "",
    var pass: String = "",
    var notes: String = ""
) {

    override fun toString(): String {
        return "User(id=$id, name=$name, pass=$pass, notes=$notes)"
    }
}