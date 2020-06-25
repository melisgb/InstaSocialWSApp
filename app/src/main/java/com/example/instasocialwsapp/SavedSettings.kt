package com.example.instasocialwsapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class SavedSettings {
    var context : Context? = null
    var sharedRef : SharedPreferences? = null

    companion object{
        var userID = ""
    }

    constructor(context: Context){
        this.context = context
        sharedRef = context.getSharedPreferences("myRef", Context.MODE_PRIVATE)
    }

    fun saveUserSettings(userID : String){
        val editor = sharedRef!!.edit()
        editor.putString("userID", userID)
        editor.commit()
        loadUserSettings()
    }

    fun loadUserSettings(){
        userID = sharedRef!!.getString("userID", "0").toString()

        if(userID == "0"){
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //to start intent from different places
            context!!.startActivity(intent)
        }
    }



}