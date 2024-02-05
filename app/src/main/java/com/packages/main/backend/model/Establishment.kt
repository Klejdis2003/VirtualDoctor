package com.packages.main.backend.model

import java.util.*
import kotlin.collections.HashMap

class Establishment(
    val address: String,
    val phoneNumber: String,
    )
{
    var menu: HashMap<ItemType, TreeSet<Item>> = HashMap()
    companion object{
        var list: HashSet<Establishment> = HashSet()
    }

    init{
        menu[ItemType.FOOD] = TreeSet()
        menu[ItemType.DRINK] = TreeSet()
        menu[ItemType.DESSERT] = TreeSet()
        list.add(this)
    }

    override fun toString(): String {
        return "com.packages.main.data.Establishment(address='$address', phoneNumber='$phoneNumber', menu=$menu)"
    }
}

