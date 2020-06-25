package com.example.instasocialwsapp

class Post {
    var postID : String? = null
    var postContent : String? = null
    var postImageUrl : String? = null
    var dateCreated : String? = null
    var postPersonName : String? = null
    var postProfImage : String? = null

    constructor(postID : String, postContent : String, postImageUrl : String, dateCreated: String,
                postPersonName : String, postProfImage: String){
        this.postID = postID
        this.postContent = postContent
        this.postImageUrl = postImageUrl
        this.dateCreated = dateCreated
        this.postPersonName = postPersonName
        this.postProfImage = postProfImage
    }


}