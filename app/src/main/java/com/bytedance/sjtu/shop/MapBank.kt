package com.bytedance.sjtu.shop

import com.bytedance.sjtu.shop.Utils.Utils
import java.util.*

class MapBank {
    companion object{
        val map = TreeMap<String, String>()
    }
    public fun encapsulation(rank : Int){
        for(i in 1..6){
            var discountNum = when(rank){
                1-> (1..3).random()
                2-> (4..6).random()
                else -> (7..9).random()
            }

            map["第${Utils.format(i)}件商品"]="折扣为${discountNum}折"
        }
    }

    public fun getMap(): TreeMap<String, String>{
        return map
    }
}