package com.packages.main.backend.model.user

import com.packages.main.backend.model.Establishment
import com.packages.main.backend.model.Item

class Manager(
    userName: Int,
    name: String,
    email: String,
    password: String,
    private val establishment: Establishment
) : Account(userName, name, email) {

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
