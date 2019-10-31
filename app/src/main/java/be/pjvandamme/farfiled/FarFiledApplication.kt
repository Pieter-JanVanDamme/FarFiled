package be.pjvandamme.farfiled

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class FarFiledApplication: Application(){

    // TODO: Material design
    // TODO: integratietesten
    // TODO: documentatie
    // TODO: Repository + werken online/offline
    // TODO: (optioneel) - worker
    override fun onCreate(){
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(DebugTree())
        }
    }
}