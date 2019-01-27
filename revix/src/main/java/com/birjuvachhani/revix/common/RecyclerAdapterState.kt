package com.birjuvachhani.revix.common

sealed class RecyclerAdapterState {
    object Empty : RecyclerAdapterState()
    object Error : RecyclerAdapterState()
    object Loading : RecyclerAdapterState()
    object Data : RecyclerAdapterState()
}