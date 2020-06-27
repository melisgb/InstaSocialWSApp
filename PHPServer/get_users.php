<?php

//To call service : 
//http://localhost:8000/get_users.php?user_id=60

require("DBInfo.inc");
include("logger.php");

$query = "select u.*, f.user_id is not null as 'is_followed'
		    from instasocial.user u
		    left join instasocial.follower f 
			  on u.user_id = f.user_id and f.following_user_id = {$_GET['user_id']}
		   where u.user_id not in (1, {$_GET['user_id']}) ;";


wh_log($query);
$result = mysqli_query($connect, $query);

if(!$result){
	die('Error running query');
}

$usersInfo = array();

while( $row = mysqli_fetch_assoc($result)){
	$usersInfo[] = $row;
}

if($usersInfo){
	print("{ 'msg' : 'Loading users successful', 'usersInfo': '". json_encode($usersInfo)."' }");

} else {
	print("{ 'msg' : 'Loading users - no users' }");
}

mysqli_free_result($result);
// mysqli_close($connect);

?>



