package com.bytedance.sjtu.shop.Utils

class Utils {
    companion object{
        fun format(num : Int):String{
            return if(num<10){
                "0${num}"
            }else{
                "$num"
            }
        }
    }
}