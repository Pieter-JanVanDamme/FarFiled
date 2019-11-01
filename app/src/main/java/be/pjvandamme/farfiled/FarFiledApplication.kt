package be.pjvandamme.farfiled

import android.app.Application
import android.os.Build
import androidx.work.*
import be.pjvandamme.farfiled.worker.RefreshUIFacesWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit

class FarFiledApplication: Application(){

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // TODO: Material design
    // TODO: integratietesten
    // TODO: documentatie
    override fun onCreate(){
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(DebugTree())
        }
        delayedInit()
    }

    private fun delayedInit(){
        applicationScope.launch{
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest
                = PeriodicWorkRequestBuilder<RefreshUIFacesWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshUIFacesWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}