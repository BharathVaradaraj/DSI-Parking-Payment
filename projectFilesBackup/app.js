  // Initialize Firebase
const config = {
    apiKey: "AIzaSyDGeEXaLdVWANogPIQePFyJ2qUkQQ2BUD0",
    authDomain: "parking-project-41065.firebaseapp.com",
    databaseURL: "https://parking-project-41065.firebaseio.com",
    storageBucket: "parking-project-41065.appspot.com"
	
};
firebase.initializeApp(config);
   
var rootref = firebase.database().ref().child('users');
var test = document.getElementById("demo");
var usrid;

function login(){
	var email = document.getElementById("email").value;
	var pwd = document.getElementById("pwd").value;
	
	console.log(email);
	console.log(pwd);
	firebase.auth().signInWithEmailAndPassword(email, pwd).catch(function(error) {
          // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
          // [START_EXCLUDE]
        if (errorCode === 'auth/wrong-password') {
            alert('Wrong password.');
        } 
		else {
            alert(errorMessage);
        }
        console.log(error);
    });

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
	var uid = user.uid;
	usrid = uid;
	console.log(usrid);
  } else {
    // No user is signed in.
	return;
  }
});

}

function recharge(){
	console.log(usrid);
	var usn = document.getElementById("usn").value;
	var amt = document.getElementById("amount").value;
	usn = usn.toUpperCase();
	console.log(usn);
    rootref.once('value', function(snap){
	    var data =snap.val();
	    var i;
	    for(i in data){
		    console.log(data[i]['ID']);
		    if(usn.length == 10 && data[i]['ID'] == usn){
				
		        var x =data[i]['Amount'];
				x = Number(x);
				amt = Number(amt);
				amt = amt + x;
				amt = String(amt);
				console.log(amt);
			    rootref.child(i).update({Amount: amt});
			    console.log('success');
			  
		    }
			
	    }
    });
}


function register(){
if(usrid){
	var uid = document.getElementById("uid").value;
	var rfid = document.getElementById("rfid").value;
	console.log(rfid);
	uid = uid.toUpperCase();
    rootref.once('value', function(snap){
	    var data =snap.val();
	    var i;
	    for(i in data){
		    console.log(data[i]['ID']);
		    if(uid.length == 10 && data[i]['ID'] == uid){
				rootref.child(i).update({RFID: rfid});	
			}
		}
	});	
}
else{
	alert('User Not Logged In');
	window.location.href="index.html";
	return;
}
}

function sendPasswordReset() {
    var email = document.getElementById('email').value;
      // [START sendpasswordemail]
    firebase.auth().sendPasswordResetEmail(email).then(function() {
        // Password Reset Email Sent!
        // [START_EXCLUDE]
        alert('Password Reset Email Sent!');
        // [END_EXCLUDE]
    }).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        // [START_EXCLUDE]
        if (errorCode == 'auth/invalid-email') {
            alert(errorMessage);
        } else if (errorCode == 'auth/user-not-found') {
            alert(errorMessage);
        }
        console.log(error);
        // [END_EXCLUDE]
    });
      // [END sendpasswordemail];
}

function logout(){
	firebase.auth().signOut().then(function() {
       // Sign-out successful.
	   window.location.href="index.html";
	   document.getElementById("log").style.visibility = "hidden";
    }, function(error) {
          // An error happened.
		  console.log(error);
        });
}

function data(){
	if(usrid){
		window.location.href="userdata.html";
	}
	else
		window.location.href="index.html";
}

function list(){
	rootref.once('value', function(snap){
	    var data =snap.val();
	    var i;
		var temp =document.getElementById("users");
		var list ="";
	    for(i in data){
		    console.log(data[i]['name']);
			list +="<li>"+data[i]['name']+"</li><br>";
		}
		document.getElementById("users").innerHTML=list;
	});
}	

function info(){
	document.getElementById("tmp").style.visibility = "visible";
}

function display(){
	
	var usn = document.getElementById("ui").value;
	usn = usn.toUpperCase();
	console.log(usn);
    rootref.once('value', function(snap){
	    var data =snap.val();
	    var i;
	    for(i in data){
		    if(usn.length == 10 && data[i]['ID'] == usn){
				
				document.getElementById("nam").innerHTML = data[i]['name'];
				document.getElementById("id").innerHTML = data[i]['ID'];
				document.getElementById("rf").innerHTML = data[i]['RFID'];
				document.getElementById("amt").innerHTML = data[i]['Amount'];
				document.getElementById("mob").innerHTML = data[i]['phone'];
			}
		}
	});
}