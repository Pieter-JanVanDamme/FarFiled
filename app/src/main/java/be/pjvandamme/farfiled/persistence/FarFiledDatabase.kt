package be.pjvandamme.farfiled.persistence

import android.content.Context
import androidx.room.*
import be.pjvandamme.farfiled.dao.FaceDao
import be.pjvandamme.farfiled.dao.RelationDao
import be.pjvandamme.farfiled.dao.RelationLifeAreaDao
import be.pjvandamme.farfiled.model.Face
import be.pjvandamme.farfiled.model.Relation
import be.pjvandamme.farfiled.model.RelationLifeArea
import be.pjvandamme.farfiled.util.Converters

@Database(entities = [Relation::class, RelationLifeArea::class, DatabaseFace::class],
    version = 27,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class FarFiledDatabase: RoomDatabase() {
    abstract val relationDao: RelationDao
    abstract val relationLifeAreaDao: RelationLifeAreaDao
    abstract val faceDao: FaceDao

    companion object{
        @Volatile
        private var INSTANCE: FarFiledDatabase? = null

        fun getInstance(context: Context): FarFiledDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FarFiledDatabase::class.java,
                        "farfiled_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}