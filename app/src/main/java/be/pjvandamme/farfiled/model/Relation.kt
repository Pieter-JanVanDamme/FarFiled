package be.pjvandamme.farfiled.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Relation(
    @PrimaryKey(autoGenerate=true)
    var relationId: Long = 0L,
    var name: String,
    var synopsis: String,
    var avatarUrl: String?

    // TODO: invent a way to keep track of a relation's relevancy
    // simple idea: subtract relevancy points on a daily basis, add relevancy points
    // when adding/editing ephemerons or editing their data and lifeareas
    // in any case: add it to the Dao
)