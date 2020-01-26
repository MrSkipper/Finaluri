package com.example.loginui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.view.*

class Main3Activity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance().reference.child("Users")
    private val user = FirebaseAuth.getInstance().currentUser!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        readAboutMe()
        publish.setOnClickListener { saveAboutMe() }
        delete.setOnClickListener { deleteAboutMe() }
        gback3.setOnClickListener { startActivity(Intent(this, Main2Activity::class.java)); finish() }
    }

    private fun readAboutMe()
    {
        db.child(user.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            // ამ ფუნქციაში ვიღებთ ინფორმაციას ბაზიდან Key-Value-ს პრინციპით
            // და მიღებულ ინფორმაციას ვინახავთ შესაბამის ცვლადებში
            override fun onDataChange(p0: DataSnapshot) {
                val name = p0.child("name").value.toString()
                val email = p0.child("email").value.toString()
                val about = p0.child("about").value.toString()

                // საბოლოოდ კი About Me-ში გამოჩნდება მიღებული ინფორმაცია
                textView3.setText(about)
            }
        })
    }

    private fun saveAboutMe()
    {
        // მონაცემებს ვწერთ HashMap-ში [Key : Value] პრინციპით
        // Key-ში ვუთითებთ ბაზაში ჩასაწერ სახელს, Value-დ კი
        // ვანიჭებთ შესაბამის მონაცემს მომხმარებლის ინფოდან
        // ან პირდაპირ EditText-ში მის მიერ შეყვანილი ტექსტიდან
        val hm = HashMap<String, String>()
        hm["name"] = user.displayName.toString()
        hm["email"] = user.email.toString()
        hm["about"] = textView3.text.toString()

        // საბოლოოდ მიღებული HashMap ობიექტი გაგვაქვს მონაცემთა ბაზაში
        db.child(user.uid).setValue(hm)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun deleteAboutMe()
    {
        // ამგვარად იშლება მონაცემები ბაზიდან
        db.child(user.uid).child("about").removeValue()
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show()
    }
}
