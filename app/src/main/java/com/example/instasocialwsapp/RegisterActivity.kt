package com.example.instasocialwsapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var mAuth : FirebaseAuth? = null
    //the constant that will be associated to the permission for reading external storage.
    val READ_IMAGE = 100
    // To save users in Firebase
    private var mStorageRef : StorageReference? = null
    private var database = FirebaseDatabase.getInstance()
        private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        signInAnonymously()
        btnRegister.setOnClickListener {
            saveImageInFirebase()
        }

        ivUserImage.setOnClickListener {
            checkPermissions()
        }

    }

    fun signInAnonymously(){
        mAuth!!.signInAnonymously().addOnCompleteListener (this) {
                task ->
            Log.d("LoggingInfo", task.isSuccessful.toString())
        }
    }



    fun saveImageInFirebase(){
        val currUser = mAuth!!.currentUser
//        val email =  currUser!!.email.toString()
        val email = etEmail.text.toString()
        val storage = FirebaseStorage.getInstance()
        //replace with the url to storage Firebase
        val storageRef = storage.getReferenceFromUrl("gs://instasocialwsapp.appspot.com/")

        val df = SimpleDateFormat("ddMMyy_hhmmss")
        val currentDate = df.format(Date())
        val imagePath = email.subSequence(0,4).toString() + currentDate + ".jpg"
        val imageRef = storageRef.child("profileImgs/"+ imagePath)

        ivUserImage.isDrawingCacheEnabled = true
        ivUserImage.buildDrawingCache()

        val drawable = ivUserImage.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray)   //this is to reduce the file size from the phone's image
        val data = byteArray.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        var a : Task<Int>? = null
        a.toString()
        uploadTask
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed image upload", Toast.LENGTH_SHORT).show()
                Log.e("Upload Error", uploadTask.exception.toString())
            }
            .continueWithTask { imageRef.downloadUrl }
            .addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Log.d("DOWNLOAD URL: ", downloadUrl)

                //save into FirebaseDB. It will save the username as index and the userID as value.
                myRef.child("users").child(currUser!!.uid).child("email").setValue(email)
                myRef.child("users").child(currUser!!.uid).child("profileImg").setValue(downloadUrl)

                //to save in localDatabase
                val url = "http://10.0.2.2:8000/register.php?user_name=${etUsername.text}&user_email=${etEmail.text}&user_password=${etPassword.text}&user_profile_url=$downloadUrl"
//                val url = "http://10.0.2.2:8000/register.php?user_name=Elena&user_email=isabel&user_password=12345&user_profile_url=https://firebasestorage.googleapis.com/v0/b/instasocialwsapp.appspot.com/o/profileImgs%2Fisab230620_103650.jpg?alt=media&token=c8856b9e-d0c3-49d8-81a4-6d4901f8d798"
                MyAsyncTask().execute(url)
                loadMainActivity()

            }
    }

    //Functions to load an image from phone into Firebase.
    fun checkPermissions(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //the predetermined requestPermissions will call onRequestPermissionsResult, but we will override function to manage independently.
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READ_IMAGE )
            return
        }
        //when the permission is granted it will proceed to load image.
        //loadImage()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        when (requestCode) {
            READ_IMAGE -> {
                if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    Log.e("Image Permission", grantResults.toString())
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    val PICK_IMG_CODE = 123 //this is used as a code for requesting images from device.
    fun loadImage(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
        startActivityForResult(intent, PICK_IMG_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMG_CODE && data != null && resultCode == RESULT_OK){
            val selectedImage = data.data
            //this will be set to a low size file.
            ivUserImage.setImageURI(selectedImage)

        }
    }

    fun loadMainActivity(){
        val currUser = mAuth!!.currentUser
        if(currUser!=null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }


}