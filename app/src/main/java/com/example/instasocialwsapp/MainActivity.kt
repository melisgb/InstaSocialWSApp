package com.example.instasocialwsapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    open var postsList = ArrayList<Post>()
    var postsAdapter : PostAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val savedSettings = SavedSettings(this)
        savedSettings.loadUserSettings()

        postsAdapter = PostAdapter(this, postsList)
        val listViewPost = findViewById<ListView>(R.id.postsListView)
        listViewPost.adapter = postsAdapter

        postsList.clear()
        //Dummy data
        postsList.add(Post("1", "him", "url", "2019-12-03", "add" ))
        postsList.add(Post("120", "Hola primer post", "url", "2019/06/24", "minus"))
        postsList.add(Post("123", "Hola 2do post, Demo for sure", "https://firebasestorage.googleapis.com/v0/b/instasocialwsapp.appspot.com/o/profileImgs%2F1234240620_044051.jpg?alt=media&token=9bb20e82-68ce-4649-8f76-a9e111fe3ba2", "2019/06/24", "minus"))
        postsAdapter!!.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        postsAdapter?.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu!!.findItem(R.id.search_note_ic).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                loadPosts("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.add_note_ic -> {
                    //TODO: Go to home
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadPosts(title : String){
        //TODO: Search into DB based on title, updateList, notifyadapter
    }
    fun getPosts(){
        //TODO: Read from DB and load into the list
    }

}