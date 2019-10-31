package be.pjvandamme.farfiled.model

import com.squareup.moshi.Json

data class Face(
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String,
    @Json(name = "position") val position: String,
    @Json(name = "photo") val photo: String
){
    override fun toString(): String{
        return "$name, $position ($photo)"
    }
}