package com.example.instasocialwsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

class UsersActivity : AppCompatActivity() {

    var usersList = ArrayList<User>()
    var usersAdapter : UserAdapter? = null
    var myUserID : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        var bundle = intent.extras
        myUserID = bundle!!.getString("userID")

        usersAdapter = UserAdapter(this, usersList, myUserID!!)
        val listViewUsers = findViewById<ListView>(R.id.usersListView)
        listViewUsers.adapter = usersAdapter

        usersList.clear()
        loadUsers(myUserID!!)

    }

    fun loadUsers(currentUser : String){
        //Search into DB
        val url = Uri.parse("http://10.0.2.2:8000/get_users.php?")
            .buildUpon()
            .appendQueryParameter("user_id", myUserID)
            .build()
            .toString()

        MyAsyncTask(
            onFail = {
//                Toast.makeText(applicationContext, "Retrieving users failed", Toast.LENGTH_SHORT).show()
                usersList.clear()
                usersAdapter!!.notifyDataSetChanged()
            },
            onSuccess = { listOfUsers ->
//                Toast.makeText(applicationContext, "Loading users", Toast.LENGTH_SHORT).show()
                usersList.clear()
                usersList.addAll(listOfUsers as ArrayList<User>)
                usersAdapter!!.notifyDataSetChanged()
            }
        ).execute(url)

    }
}