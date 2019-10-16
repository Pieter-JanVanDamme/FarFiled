package be.pjvandamme.farfiled

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class FarFiledApplication: Application(){
    override fun onCreate(){
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(DebugTree())
        }
    }
}