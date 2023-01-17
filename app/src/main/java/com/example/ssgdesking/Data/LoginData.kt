package com.example.ssgdesking.Data

class LoginData {
    var id: String? = null
    private var seatid: String? = null

    constructor(id: String?) {
        this.id = id
    }

    constructor(id: String?, seatid: String?) {
        this.id = id
        this.seatid = seatid
    }
}