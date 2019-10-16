package be.pjvandamme.farfiled.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Relation::class,
    parentColumns=["relationId"],
    childColumns = ["relationId"])],
    indices = arrayOf(
        Index(value = *arrayOf("ephemeronId", "relationId"))
    ))
data class Ephemeron(
    @PrimaryKey(autoGenerate=true)
    var ephemeronId: Long = 0L,
    var relationId: Long,
    var title: String,
    var content: String
    // TODO: add dateEdited + aanpassen in Dao
){
    companion object{
        val description: String = "currently relevant topics"
        val detailedDescription: String = "currently relevant projects/circumstances or latest " +
                "developments in some area, topics of conversation, anything important for " +
                "future interactions (promises/debts/favors owed, important dates in the " +
                "near-future)"
    }
}