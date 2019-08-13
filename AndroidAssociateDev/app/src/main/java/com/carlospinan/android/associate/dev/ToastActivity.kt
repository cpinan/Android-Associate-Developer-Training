package com.carlospinan.android.associate.dev

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ToastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)
        customToast()
    }

    private fun customToast() {
        val inflater = layoutInflater
        val layout: ViewGroup = inflater.inflate(R.layout.custom_toast, null) as ViewGroup
        val text: TextView = layout.findViewById(R.id.text)
        text.text = "This is a custom toast"

        with(Toast(applicationContext)) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }

    }

    private fun normalToast() {
        val text = "Kausha la Come"
        val duration = Toast.LENGTH_LONG

        val toast = Toast.makeText(
            baseContext,
            text,
            duration
        )

        toast.setGravity(
            Gravity.TOP or Gravity.LEFT,
            0,
            0
        )

        toast.show()
    }

}