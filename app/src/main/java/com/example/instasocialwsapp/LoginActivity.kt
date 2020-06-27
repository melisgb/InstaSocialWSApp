package com.example.instasocialwsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = "Login"
        btnLogin.setOnClickListener{
            loginUser()
        }

        btnGotoRegister.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    fun loginUser(){
        //to save in localDatabase
        val url = "http://10.0.2.2:8000/login.php?user_email=${etEmailLogin.text}&user_password=${etPasswordLogin.text}"
        MyAsyncTask(
            onFail = {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                btnLogin.isEnabled = true
            },
            onSuccess = { result ->
                //TODO: Return from MyAsync the user_id
                val user_id = result as String
                val savedSettings = SavedSettings(applicationContext)
                savedSettings.saveUserSettings(user_id)
                Toast.makeText(this, "User logged", Toast.LENGTH_SHORT).show()
                finish()
            }
        ).execute(url)



    }
}