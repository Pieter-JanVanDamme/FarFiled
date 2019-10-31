package be.pjvandamme.farfiled.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.pjvandamme.farfiled.model.Face
import timber.log.Timber

@Entity
data class DatabaseFace(
    // There are 211 unique names in the API (https://uifaces.co/names), setting the name
    // as the primary key instead of the photo (the "face" itself, which is the basis for
    // the entity) makes sure we have at most 211 entries in the database at any given time.
    // Because names are assigned randomly, this means it's possible to have the same photo
    // multiple times though the odds are slight.
    @PrimaryKey
    val name: String,
    val email: String,
    val position: String,
    val photo: String
){
    override fun toString(): String {
        return "$name, $position ($photo)"
    }
}

fun List<DatabaseFace>.asDomainModel(): List<Face>{
    Timber.i("List<DatabaseFace>.asDomainModel called.")
    return map{
        Face(
            name = it.name,
            email = it.email,
            position = it.position,
            photo = it.photo
        )
    }
}