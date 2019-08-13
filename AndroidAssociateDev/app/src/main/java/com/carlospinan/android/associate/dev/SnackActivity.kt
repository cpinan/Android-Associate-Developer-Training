package com.carlospinan.android.associate.dev

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class SnackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack)

        val coordinatorLayout = findViewById<View>(R.id.coordinatorLayout)

        val duration = Snackbar.LENGTH_LONG

        val snackbar = Snackbar.make(
            coordinatorLayout,
            "THIS IS A SNACKBAR",
            duration
        )

        snackbar.setAction(
            "ITEM 1"
        ) {
            println("Item 1 action")
        }

        snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        snackbar.show()

    }

}