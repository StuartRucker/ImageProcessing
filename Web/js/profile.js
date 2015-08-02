var userPicRef = new Firebase("https://opencv.firebaseio.com/users/"+ user +"/pictures");
var zip = new JSZip();
$(".img-download").hide();
waitForAuth();

function waitForAuth(){
    if(user != "loggedOut"){
        displayImages();
    }
    else{
        setTimeout(function(){
            waitForAuth();
        },250);
    }
}

function displayImages(){
    var i = 0;
    userPicRef.on("value", function(snapshot) {
        snapshot.forEach(function(childSnapshot){

            var pics = childSnapshot.val().src;
            //console.log(pics);
            for(var x = 0; x < pics.length; x++){
                zip.file("pic" + i +".jpg", pics[x], {base64: true});
                console.log(pics[x]);
                $(".imgs-row").append(
                    '<li>'+
                        '<a href="data:image/jpg;base64,' + pics[x] +'">'+
                            '<img src="data:image/jpg;base64,'+ pics[x] +'" alt="" title="" />'+
                        '</a>'+
                    '</li>'
                );

                i++;
            }
        });
    });
    $(".img-download").show();
}

function downloadImages(){
    var content = zip.generate({type:"blob"});
    saveAs(content, "iStitchPics.zip");
}



    //
    // userPicRef.on("value", function(snapshot) {
    //     var i = 0;
    //     snapshot.forEach(function(childSnapshot){
    //         if(i % 2 == 0){
    //             $(".profile-cont").append(
    //             '<div class="row" >'+
    //                 '<div class="col-md-6 left">'+
    //                     '<div class="card">' +
    //                         '<center>' +
    //                             '<img style="height: 100%;" src="data:image/gif;base64,'+ childSnapshot.val().pic +'"></img> ' +
    //                         '</center>' +
    //                     '</div>'+
    //                 '</div>'+
    //                 '<div class="col-md-6 right" id="' + i + '">'+
    //                 '</div>'+
    //             '</div>'
    //             );
    //         }else{
    //             var elem = i - 1;
    //             $("#" + elem).append(
    //                 '<center>' +
    //                     '<div class="card">' +
    //                         '<img style="height: 100%" src="data:image/gif;base64,'+ childSnapshot.val().pic +'"></img> ' +
    //                     '</div>' +
    //                 '</center>'
    //             )
    //         }
    //         i++;
    //     });
    // });
