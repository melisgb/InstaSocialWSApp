<?php

//To call service : 
//http://localhost:8000/add_post.php?post_user_id=1&post_content=Hola,soyYo&post_image_url=home/tester.png

require("DBInfo.inc");

$query = "insert into instasocial.post(post_user_id, post_content, post_image_url) ".
		 "values (".
		 	$_GET['post_user_id'].
		 	", ".
		 	"'".$_GET['post_content']."'".
		 	", ".
		 	"'".$_GET['post_image_url']."'".
		 ");";

$result = mysqli_query($connect, $query);

if(!$result){
	print("{ 'msg' : 'Post failed to save'} ");
}
else{
	print("{ 'msg' : 'Post saved'} ");
}

// mysqli_close($connect);

?>

