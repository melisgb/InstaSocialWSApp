<?php

//To call service : 
//http://localhost:8000/register.php?user_name=Lovie&user_email=cjtoribio@gmail.com&user_password=12345&user_profile_url=images/photo.png

require("DBInfo.inc");

$query = "insert into instasocial.user(user_name, user_email, user_password, user_profile_url) values ('".$_GET['user_name']."', '".$_GET['user_email']."', '".$_GET['user_password']."', '".$_GET['user_profile_url']."');" ;

$result = mysqli_query($connect, $query);

if(!$result){
	$output = "{ 'msg': 'Register Failed' }";
}
else {
	$output = "{ 'msg': 'Register User is added'}";
}

// print("{'ok': '$query'}");
print($output);

// /* determinar el id de nuestro hilo */
// $thread_id = mysqli_thread_id($connect);

// /* Poner fin a la conexión */
// mysqli_kill($connect, $thread_id);

// mysqli_close($connect);


?>
