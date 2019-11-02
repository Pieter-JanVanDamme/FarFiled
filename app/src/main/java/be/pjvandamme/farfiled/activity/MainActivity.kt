package be.pjvandamme.farfiled.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.pjvandamme.farfiled.R

/**
 * The main activity for FarFiled allows users to manage their personal relationsips.
 * The initial fragment represents a list of their 'relations' and lets them view/edit one of
 * them OR create a new one in a second fragment.
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // TODO: decide whether to add support for an Up button
    // TODO: decide whether to add an options menu
    // TODO: decide whether to add a navigation drawer
}
