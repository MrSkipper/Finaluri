package com.example.loginui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main4.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            startActivity(Intent (this,Main4Activity::class.java))
            finish()
        }

        btn_login.setOnClickListener {
            doLogin()
        }

        btn_forgot_password.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _,  _->
                forgotPassword(username)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { _,  _->  })
            builder.show()
        }
    }

    private fun forgotPassword(username : EditText) {
        if(username.text.toString().isEmpty()){
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                }
            }


    }

    /* ცვლილებები რედაქტირებულ ლოგიკაში */
    /* ძირითადი ცვლილებები, ხაზი #35, 36, 37, 41, 42, 43, 47, 48, 49, 53 */

    private fun doLogin() {
        if(editText.text.toString().isEmpty()){  // ვამოწმებ EditText ობიექტს იდენტიფიკატორით editText (როგორც ეს შესაბამისი აქტივობის ლეიაუთ ფაილშია განსაზღვრული) რადგან ეს ველი მიმღებია მომხმარებლის მონაცემებისა (ამ შემთხვევაში ელ.ფოსტა) ხოლო პირობა პასუხისმგებელია მისი ვალიდურობის დადგენაში.
            editText.error = "Please enter email"
            editText.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
            editText.error = "Please enter valid email"
            editText.requestFocus()
            return
        }

        if(editText2.text.toString().isEmpty()){  // პაროლის ველისთვის (აქტივობის შესაბამის ლეიაუთში) განსაზღვრულია იდენტიფიკატორი editText2, ამიტომ პაროის ვალიდურობის დასადგენად ვიღებთ მასში მომხმარებლის მიერ შეტანილ მონაცემებს და გადავცემთ პირობას
            editText2.error = "Please enter password"
            editText2.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(editText.text.toString(), editText2.text.toString()) // Firebase ავტორიზაციის მეთოდს ორ პარამეტრად გადავცემთ მომხმარებლის მიერ შეტანილ მონაცემებს (ამ შემთხვევაში ელ.ფოსტა და პაროლი)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser : FirebaseUser?) {
        startActivity(Intent(this, Main2Activity::class.java))
        finish()
    }

}

