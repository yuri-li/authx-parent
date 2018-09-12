package org.authx.account.service

import org.springframework.stereotype.Service
import java.util.*

interface UserServiceI {
    fun getRole(): String
}

@Service
class UserServiceImpl : UserServiceI {
    val id:String = UUID.randomUUID().toString()
    override fun getRole(): String = "USER ${id}"
}