package be.pjvandamme.farfiled.util

import androidx.room.TypeConverter
import be.pjvandamme.farfiled.model.LifeArea

class Converters{

    companion object {
        @TypeConverter
        @JvmStatic
        fun toLifeArea(value: Int?): LifeArea? {
            // iterate over the enumValues of LifeArea to find one whose sequencenumber matches
            // the argument
            enumValues<LifeArea>().forEach {
                if (it.sequenceNumber == value)
                    return it
            }
            // return null if no matching sequencenumber was found
            return null
        }

        @TypeConverter
        @JvmStatic
        fun fromLifeArea(lifeArea: LifeArea): Int = lifeArea.sequenceNumber
    }
}