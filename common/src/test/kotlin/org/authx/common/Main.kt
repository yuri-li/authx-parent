package org.authx.common

fun main(args: Array<String>) {
    val path = "http://auth.mgt.com/api/v1/auth/user/current"
    println(path.replace("/user/current",""))
    println(path)
}