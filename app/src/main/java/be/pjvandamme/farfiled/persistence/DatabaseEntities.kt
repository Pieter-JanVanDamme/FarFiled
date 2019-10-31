package be.pjvandamme.farfiled.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.pjvandamme.farfiled.model.Face

@Entity
data class DatabaseFace(
    val name: String,
    val email: String,
    val position: String,
    @PrimaryKey
    val photo: String
){
    override fun toString(): String {
        return "$name, $position ($photo)"
    }
}

fun List<DatabaseFace>.asDomainModel(): List<Face>{
    return map{
        Face(
            name = it.name,
            email = it.email,
            position = it.position,
            photo = it.photo
        )
    }
}