package com.bigbigdw.manavarasetting.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bigbigdw.manavarasetting.main.screen.ScreenSplash
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivitySpalsh : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenSplash()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this@ActivitySpalsh, ActivityMain::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@ActivitySpalsh, ActivityLogin::class.java)
            startActivity(intent)
        }
    }
}