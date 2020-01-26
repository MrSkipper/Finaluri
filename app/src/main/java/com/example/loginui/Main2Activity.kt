package com.example.loginui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var myAuth = FirebaseAuth.getInstance()
    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        button_plus.setOnClickListener {
            startActivity(Intent(this,Main3Activity::class.java))
        }

        parolish_tirua.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java))
        }

        backButton1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btn = findViewById(R.id.logout)
        btn.setOnClickListener{ _->
            Toast.makeText(this, "Logging out...", Toast.LENGTH_LONG).show()
                           signOut()
        }

        myAuth.addAuthStateListener {
            if(myAuth.currentUser==null){
                this.finish()
            }
        }


    }

    private fun signOut(){
        startActivity(Intent(this, MainActivity::class.java))
        myAuth.signOut()
        finish()
    }


}
