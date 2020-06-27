package com.example.instasocialwsapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URLEncoder
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var postsList = ArrayList<Post>()
    var postsAdapter : PostAdapter? = null
    var userID : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val savedSettings = SavedSettings(this)
        savedSettings.loadUserSettings()
        userID = savedSettings.sharedRef!!.getString("userID", "0").toString()

        postsAdapter = PostAdapter(this, postsList)
        val listViewPost = findViewById<ListView>(R.id.postsListView)
        listViewPost.adapter = postsAdapter

        postsList.clear()
        loadPosts("%", 0)

        val factButtonGoToUsers  = findViewById<FloatingActionButton>(R.id.gotoUsersFloatingActBtn)
        factButtonGoToUsers.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }


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
                loadPosts("%$query%", 0)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.goToHome -> {
                loadPosts("%", 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun loadPosts(keyword : String, startFrom : Int){
        //Search into DB based on keyword and updates the List
        val content = URLEncoder.encode(keyword, "utf-8")
        val url = "http://10.0.2.2:8000/get_posts.php?case=3&keyword=${content}&user_id=${userID}&startFrom=${startFrom}"
        MyAsyncTask(
            onFail = {
//                Toast.makeText(applicationContext, "Retrieving posts failed", Toast.LENGTH_SHORT).show()
                postsList.clear()
                postsList.add(
                    Post(
                        "1",
                        "",
                        "url",
                        "2019-12-03",
                        "1",
                        "add",
                        "profImg" ))

                postsAdapter!!.notifyDataSetChanged()
            },
            onSuccess = { listOfPosts ->
//                Toast.makeText(applicationContext, "Loading posts", Toast.LENGTH_SHORT).show()
                postsList.clear()
                postsList.add(
                    Post(
                        "1",
                        "",
                        "url",
                        "2019-12-03",
                        "1",
                        "add",
                        "profImg" ))
                postsList.addAll(listOfPosts as ArrayList<Post>)
                postsAdapter!!.notifyDataSetChanged()
            }
        ).execute(url)
    }

}