$(document).ready(function(){


    $('#lblcurrentEmail').text(getCookie("email"));
    $('#lblcurrentPhone').text(getCookie("phone"));

     $('#btnSaveSettings').click(function(e){

        var oldpassword = $('#oldpassword').val();
        var newpassword = $('#newpassword').val();
        var confirmpassword = $('#confirmpassword').val();
        var newemail = $('#email').val();


        if(oldpassword.length > 0 && newpassword.length > 0 && confirmpassword.length > 0)
        {

                    var changepassword = {
                         id: sessionStorage.getItem("id"),
                         password:newpassword
                   };
                    $.ajax({
                    type: "POST",
                    cache:false,
                    url: APIURL + "users/changePassword",
                    headers: {
                            'token': sessionStorage.getItem("token"),
                            'Content-Type':'application/json'
                         },
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(changepassword),
                }).done(function (result) {
                   // console.log(result);
                    alert("Password Changed successfully");
                    $('#oldpassword').val("");
                    $('#newpassword').val("");
                    $('#confirmpassword').val("");

               })
                .fail(function (data, textStatus, xhr) {
                     //console.log(data.responseJSON.Error);
                     alert("There seems to be an issue with password change. Please try again");
                     /*console.log("error", data.status);
                     console.log("STATUS: "+xhr); */
                });

        }



            if(newemail.length > 0)
            {


                        var changeemail = {
                             email:newemail,
                       };
                        $.ajax({
                        type: "PUT",
                        url: APIURL + "users/" +sessionStorage.getItem("id") ,
                        headers: {
                                'token': sessionStorage.getItem("token"),
                                'Content-Type':'application/json'
                             },
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(changeemail),
                    }).done(function (result) {
                       console.log(result);
                        //alert("Password Changed successfully");
                   })
                    .fail(function (data, textStatus, xhr) {
                         //console.log(data.responseJSON.Error);
                         alert("There seems to be an issue with email change. Please try again");
                         /*console.log("error", data.status);
                         console.log("STATUS: "+xhr); */
                    });

            }

             if(phone.length > 0)
            {

                        var changephone = {
                             phone:phone,
                       };
                        $.ajax({
                        type: "PUT",
                        url: APIURL + "users/" +sessionStorage.getItem("id") ,
                        headers: {
                                'token': sessionStorage.getItem("token"),
                                'Content-Type':'application/json'
                             },
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(changephone),
                    }).done(function (result) {
                       console.log(result);
                        //alert("Password Changed successfully");
                   })
                    .fail(function (data, textStatus, xhr) {
                         //console.log(data.responseJSON.Error);
                         alert("There seems to be an issue with phone update. Please try again");
                         /*console.log("error", data.status);
                         console.log("STATUS: "+xhr); */
                    });

            }

    });

     $('#btnUnsubscribe').click(function(e){

        {
            console.log(sessionStorage.getItem("id"));
            console.log(sessionStorage.getItem("token"));
            $.ajax({
               url : APIURL + "users/" + sessionStorage.getItem("id"),
               headers: {
                        'token': sessionStorage.getItem("token"),
                        'Content-Type':'application/json'
                     },
               method: "DELETE",
               cache:false,
               async:false,
               success:function(data){
                     alert("User unsubcribed successfully.");
                     location.href = "/index.html";
               }

                          });
        }

    });


});


function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
