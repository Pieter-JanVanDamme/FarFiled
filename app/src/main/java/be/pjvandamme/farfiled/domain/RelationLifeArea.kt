package be.pjvandamme.farfiled.domain

import androidx.room.*
import be.pjvandamme.farfiled.database.util.Converters

@Entity(tableName = "relation_life_area",
    foreignKeys = [ForeignKey(entity = Relation::class,
    parentColumns=["relationId"],
    childColumns = ["relationId"])],
    indices = arrayOf(
        Index(value = *arrayOf("relationLifeAreaId", "relationId"))
    ))
data class RelationLifeArea(
    @PrimaryKey(autoGenerate = true)
    var relationLifeAreaId: Long = 0L,
    var relationId: Long,
    @TypeConverters(Converters::class)
    var lifeArea: LifeArea,
    var content: String
)