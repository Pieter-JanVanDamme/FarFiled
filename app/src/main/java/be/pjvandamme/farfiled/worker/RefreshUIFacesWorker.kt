package be.pjvandamme.farfiled.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import be.pjvandamme.farfiled.persistence.repository.FaceRepository

class RefreshUIFacesWorker(context: Context, params: WorkerParameters):
        CoroutineWorker(context, params){

    companion object{
        const val WORKER_NAME = "RefreshUIFacesWorker"
    }
    override suspend fun doWork(): Payload{
        val database = FarFiledDatabase.getInstance(applicationContext)
        val repository = FaceRepository(database)
        repository.refreshFaces()

        return try{
            return Payload(Result.SUCCESS)
        } catch (ex: Exception){
            return Payload(Result.RETRY)
        }
    }
}