package be.pjvandamme.farfiled.model

import be.pjvandamme.farfiled.persistence.DatabaseFace
import com.squareup.moshi.Json
import timber.log.Timber

data class Face(
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String,
    @Json(name = "position") val position: String,
    @Json(name = "photo") val photo: String
){
    fun asDatabaseModel(): DatabaseFace{
        Timber.i("Face.asDatabaseModel called for $name ($position)")
        return DatabaseFace(
            name = name,
            email = email,
            position = position,
            photo = photo
        )
    }
    override fun toString(): String{
        return "$name, $position ($photo)"
    }
}