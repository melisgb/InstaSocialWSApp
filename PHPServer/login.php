<?php

//To call service : 
//http://localhost:8000/login.php?user_email=cjtoribio@gmail.com&user_password=12345

require("DBInfo.inc");

$query = "select * from instasocial.user where user_email = '".$_GET['user_email'].
"' and user_password = '".$_GET['user_password']."';" ;

$result = mysqli_query($connect, $query);

if(!$result){
	die('Error running query');
}

$userInfo = array();

while( $row = mysqli_fetch_assoc($result)){
	$userInfo[] = $row;
	break; //to limit only one result
}

if($userInfo){
	print("{ 'msg' : 'success login', 'info': '". json_encode($userInfo)."' }");

} else {
	print("{ 'msg' : 'cannot login' }");
}

mysqli_free_result($result);
// mysqli_close($connect);

?>
