package be.pjvandamme.farfiled.database

import android.content.Context
import androidx.room.*
import be.pjvandamme.farfiled.domain.Relation
import be.pjvandamme.farfiled.domain.RelationLifeArea
import be.pjvandamme.farfiled.database.util.Converters

@Database(entities = [Relation::class, RelationLifeArea::class],
    version = 2,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class FarFiledDatabase(): RoomDatabase() {
    abstract val relationDao: RelationDao
    abstract val relationLifeAreaDao: RelationLifeAreaDao

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