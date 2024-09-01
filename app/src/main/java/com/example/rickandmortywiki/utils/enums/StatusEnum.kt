package com.example.rickandmortywiki.utils.enums

import com.example.rickandmortywiki.R

enum class StatusEnum(val resText: Int, val resColor: Int) {
    ALIVE(R.string.alive, R.color.green),
    DEAD(R.string.dead, R.color.red),
    UNKNOWN(R.string.unknown, R.color.white)
}