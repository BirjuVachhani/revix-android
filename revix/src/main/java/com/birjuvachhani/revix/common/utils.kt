package com.birjuvachhani.revix.common

/**
 * Created by Birju Vachhani on 04/12/18.
 */

fun Any.classHash(): Int {
    return this::class.java.hashCode()
}