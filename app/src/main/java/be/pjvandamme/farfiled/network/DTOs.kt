package be.pjvandamme.farfiled.network


import be.pjvandamme.farfiled.persistence.DatabaseFace
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkFace(
    val name: String,
    val email: String,
    val position: String,
    val photo: String
) {
    override fun toString(): String {
        return "$name, $position ($photo)"
    }
}

fun List<NetworkFace>.asDatabaseModel(): Array<DatabaseFace>{
    return map{
        DatabaseFace(
            name = it.name,
            email = it.email,
            position = it.position,
            photo = it.photo
        )
    }.toTypedArray()
}

