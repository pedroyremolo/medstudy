package br.com.ia.medstudy

import android.app.Application
import io.paperdb.Paper

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */
class MedStudyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }

}