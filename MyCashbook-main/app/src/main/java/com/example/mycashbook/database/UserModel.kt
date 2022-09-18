package com.example.mycashbook.database

class UserModel(id: Int, username: String, password: String) {


    private var id: Int = id
    var username: String
    var password: String

    init {
        this.id = id
        this.username = username
        this.password = password
    }

    fun getIdUser() : Int{
        return id
    }

    constructor() : this(0, "","")
}