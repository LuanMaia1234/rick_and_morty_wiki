package com.example.rickandmortywiki.utils.extensions

import com.example.rickandmortywiki.data.entities.LocationEntity

val LocationEntity.residentIdList: List<String>
    get() = this.residents.map { it.substring(it.lastIndexOf("/") + 1) }
