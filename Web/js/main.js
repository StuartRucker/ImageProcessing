var ref = new Firebase("https://opencv.firebaseio.com/");
var aviSpeed = "aviquick"

var dataRef = ref.child("data");
dataRef.child("average").once("value", function(snapshot){
    $(".avgTort").text(snapshot.val().tortuosity.val);
    $(".avglen").text(snapshot.val().length.val);
    $(".avgCNBD").text(snapshot.val().CNBD.val);
    $(".avgDens").text(snapshot.val().density.val);
    $(".avgArea").text(snapshot.val().imagearea.val);
});
dataRef.child("uploads").once("value", function(snapshot){
    $(".numUsers").text(snapshot.val());
});
ref.child("serverstatus").on("value", function(snapshot){
    $(".server-status").text(snapshot.val());
});


function addImage(link, type){

    var tasksRef = ref.child("tasks");
    var newMessageRef = tasksRef.push({
        'src': link,
        'type': type
    });
    var id = String(newMessageRef.key());
    var completionRef = ref.child("completion");

    //updateScreen(478521,185379,15,"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")


    completionRef.on("child_changed", function(snapshot) {
        if(snapshot.key() == id){
            updateScreen(
                snapshot.child("length").val(),
                snapshot.child("density").val(),
                snapshot.child("tortuosity").val(),
                snapshot.child("src").val(),
                snapshot.child("CNBD").val(),
                snapshot.child("imagearea").val(),
                snapshot.child("time").val()
            );
            setTimeout(function(){
                tasksRef.child(id).remove();
            }, 3000);

        }
    });
}


function updateScreen(length,density,tortuosity,b64pix, CNBD, imageArea, time){



    if(user != "loggedOut" && b64pix != null && b64pix != "oops"){
        $(".userTort").text(tortuosity);
        $(".userlen").text(length);
        $(".userCNBD").text(CNBD);
        $(".userDens").text(density);
        $(".userArea").text(imageArea);


        console.log(b64pix);

        console.log(user)
        var userRef = new Firebase("https://opencv.firebaseio.com/users/" + user + "/pictures");
        var deleteDef = userRef.child("defaultPic");
        deleteDef.remove();
        userRef.push({
            density: density,
            length: length,
            src: b64pix,
            tortuosity: tortuosity,
            CNBD: CNBD,
            imagearea: imageArea,
            time: time,
        });
    }

    $(".upload-camera").hide();
    $(".fp__btn").hide();
    $(".stitched-image").attr("src", "data:image/jpg;base64," + b64pix);
    $(".stitched-image").css({"width" : "100%"});
}


$(".filepickerimg").click(showFilePickImg);
$(".filepickervid").click(showFilePickVid);


function showFilePickImg(){
    filepicker.pickMultiple(
        {
            mimetype: 'image/*',
            maxFiles: 100,
        },
        function(Blobs){
            var images = [];
            for(var i = 0; i < Blobs.length; i++){
                images.push(Blobs[i].url);
            }
            addImage(images, "jpg");
        },
        function(error){
            console.log(JSON.stringify(error));
        }

    );
}


function showFilePickVid(){
    filepicker.pick(
        {
            mimetype: 'video/*'
        },
        function(Blob){
            var videos = [Blob.url];
            addImage(videos, aviSpeed);
        },
        function(FPError){
            console.log(FPError.toString());
        }
    );
}

function changeCheckVal(elem){
    if(elem.checked){
        $(".avi-type").text("AVI Slow");
        aviSpeed = "avislow"
    }else{
        $(".avi-type").text("AVI Quick");
        aviSpeed = "aviquick"
    }
}

window.addEventListener("beforeunload", function (e) {
    var confirmationMessage = 'The server is currently processing your image. ';
    confirmationMessage += 'If you leave before it finishes, your image will be deleted.';

    (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    return confirmationMessage; //Gecko + Webkit, Safari, Chrome etc.
});

setTimeout(function(){
    $(".fp__btn").hide();
},100
);
