package com.packages.main.model.user

import com.packages.main.model.Establishment
import com.packages.main.model.Item

class Manager(
    userName: Int,
    name: String,
    email: String,
    password: String,
    private val establishment: Establishment
) : Account(userName, name, email, password) {

        fun addItemToMenu(item: Item){
            establishment.menu[item.type]?.add(item)
        }
        fun removeItemFromMenu(item: Item){
            establishment.menu[item.type]?.remove(item)
        }
        fun modifyItemPrice(item: Item, newPrice: Float){
            establishment.menu[item.type]?.find { it.name == item.name }?.price = newPrice
        }
    }
