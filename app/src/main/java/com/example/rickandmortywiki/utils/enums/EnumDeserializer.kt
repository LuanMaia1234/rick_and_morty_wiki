package com.example.rickandmortywiki.utils.enums

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CustomEnumDeserializer<T : Enum<T>>(
    private val enumClass: Class<T>,
    private val defaultEnumValue: T,
) : JsonDeserializer<T> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): T {
        val jsonValue = json?.asString
        val enum = jsonValue?.let { value ->
            enumClass.enumConstants?.firstOrNull {
                it.name.equals(value, true)
            }
        } ?: defaultEnumValue
        return enum
    }
}
