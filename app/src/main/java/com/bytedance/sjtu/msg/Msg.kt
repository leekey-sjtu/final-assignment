package com.bytedance.sjtu.msg

class Msg(val msg: String, val msgType: Int) {

    companion object {
        const val SEND = 0
        const val GET = 1
    }

}