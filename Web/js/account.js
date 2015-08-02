var ref = new Firebase('https://opencv.firebaseio.com');

var user = 'loggedOut'
var token = localStorage.getItem('token');
if(token == null){
  token = "No Token"
}


//createUser('rohan@techlabeducation.com', 'jenga') //this should be called from html when a button is pressed or something

ref.authWithCustomToken(token, function(error, result) {
  if (error) {
    console.log("No pre-existing token found");
  } else {
    console.log("Pre-existing token found");
    var len = result.uid.length;
    user = parseInt(result.uid.substring(12, len));
    userPicRef = new Firebase("https://opencv.firebaseio.com/users/"+ user +"/pictures");
  }
});

function createUser(email, password){
    var newEmail = replacePeriods(email)
    ref.createUser({
        email    : email,
        password : password
    }, function(error, userData) {
        var len = userData.uid.length;
        var accountId = parseInt(userData.uid.substring(12, len));
        if (error) {
            console.log("Error creating user:", error);
        } else {
            console.log("Successfully created user account with uid:", userData.uid);
            ref.child('users').child(accountId).set({
                pictures: {
                    defaultPic: {
                        density: 'empty',
                        length: 'empty',
                        pic: 'empty',
                        tortuosity: 'empty',
                    },
                } ,
                settings: {
                    email: email,
                    uid: accountId,
                }
            })
        }
    });
}


function replacePeriods(email){
    return email.replace(/\./g,'*')
}

function login(email, password){
    ref.authWithPassword({
        email    : email,
        password : password
    }, function authHandler(error, authData) {
        if (error) {
            console.log("Login Failed!", error);
        } else {
            console.log("Authenticated successfully with payload:", authData);
            token = authData.token;
            localStorage.setItem('token', token);
            var len = authData.uid.length
            user = parseInt(authData.uid.substring(12, len));
        }
    });
}

function logout(){
    ref.unauth();
    localStorage.removeItem('token');
    user = 'loggedOut'
}

//checks any changes in user authentication
ref.onAuth(function(){
    if(ref.getAuth() == null){
        $(".auth-status-title").text("Logged Out");
        $(".auth-status-color").removeClass("panel-success").addClass("panel-danger");
        $(".auth-status").css({"cursor": "pointer"});
        $(".navbar-login").text("Login");
    }else{
        $(".auth-status-title").text("Logged In");
        $(".auth-status-color").removeClass("panel-danger").addClass("panel-success");
        $(".auth-status").css({"cursor": "auto"});
        $(".navbar-login").text("Logout");
        $(".navbar-login").attr("href", "");
        $(".navbar-login").click(function(){
            logout();
        });
        if(window.location.href.includes("login")){
            window.location.href = "index.html";
        }
    }
});


// Debug code
function deleteUser(email, password){
    ref.removeUser({
        email: email,
        password: password
    }, function(error) {
        if (error) {
            switch (error.code) {
                case "INVALID_USER":
                    console.log("The specified user account does not exist.");
                    break;
                case "INVALID_PASSWORD":
                    console.log("The specified user account password is incorrect.");
                    break;
                default:
                    console.log("Error removing user:", error);
            }
        } else {
            ref.child("users").child(replacePeriods(email)).remove();
            console.log("User account deleted successfully!");
        }
    });
}

setTimeout(accountAuthStatus, 500);

function accountAuthStatus(){
    $(".auth-status").animate({
        bottom: "0%"
    },500);
}

$(".auth-status").click(function(){
    if(user == "loggedOut"){
        window.location.href="login.html"
    }
});

$(".login-form").submit(function(e) {
    e.preventDefault();
    var username = $("#inputEmail").val();
    var password = $("#inputPassword").val();
    login(username, password);
});

$(".alert").hide();

$(".signup-form").submit(function(e) {
    e.preventDefault();
    var username = $("#signEmail").val();
    var password = $("#signPassword").val();
    var repPass = $("#repSignPassword").val()
    var email = $("#signEmail").val();
    if(checkIfEmailInString(email)){
        if(password == repPass){
            if(password.length >= 6){
                $(".alert").hide();
                createUser(username, password);
                login(username, password);
            }else{
                $(".login-error").text("Your password must have 6 or more characters!");
                $(".alert").show();
            }
        }else{
            $(".login-error").text("Passwords do not match!");
            $(".alert").show();
        }
    }else{
        $(".login-error").text("That is not a valid email address!");
        $(".alert").show();
    }
});


function checkIfEmailInString(text) {
    var re = /(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))/;
    return re.test(text);
}
