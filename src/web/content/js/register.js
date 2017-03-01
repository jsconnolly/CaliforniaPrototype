$(document).ready(function(){

    var RegisterData = {
    email: "",
    password: "",
    name:"",
    phone:""
};
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06




=======




>>>>>>> body content changes, removed phone registration, added settings
     $('#btnaddphone').click(function(e){

        var phoneno = $('#txtcellno').val();

<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
=======




     $('#btnaddphone').click(function(e){

        var phoneno = $('#txtcellno').val();

>>>>>>> GMaps location markers
=======
>>>>>>> body content changes, removed phone registration, added settings
        if(phoneno.length > 0)
        {
            console.log("1" + phoneno.replace("-", "").replace("-", ""));
                      var phoneres = {
                     phone: ""
                   };
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06
                    phoneres.phone = "1" + phoneno.replace("-", "").replace("-", "");
=======
                    phoneres.phone = "1" + phoneno.replace("-", "").replace("-", "");
>>>>>>> GMaps location markers
=======
                    phoneres.phone = "1" + phoneno.replace("-", "").replace("-", "");
>>>>>>> body content changes, removed phone registration, added settings
                    //console.log("Request JSON" + JSON.stringify(RegisterData));
                    $.ajax({
                    type: "POST",
                    url: APIURL + "users/phoneCode",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(phoneres),
                }).done(function (result) {
                    //console.log(result);
                    if (typeof(Storage) !== "undefined") {
                        // Store
                        sessionStorage.setItem("phone", phoneres.phone);
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06
                    }
=======
                    }
>>>>>>> GMaps location markers
=======
                    }
>>>>>>> body content changes, removed phone registration, added settings

                     window.location.href = "confirmphone.html";
                    /*if(result.id !== undefined)
                    {
                        //LoginToken.token = result.token;
                        //console.log("Token returned " + LoginToken.token);
                        $('#txtcellno').val("");
                        //alert("You have successfully registered. Please login");
                        window.location.href = "confirmphone.html";
                    }*/
               })
                .fail(function (data, textStatus, xhr) {
                     //console.log(data.responseJSON.Error);
                     alert(data.responseJSON.Error);
                     /*console.log("error", data.status);
                     console.log("STATUS: "+xhr); */
                });
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06

=======

>>>>>>> GMaps location markers
=======

>>>>>>> body content changes, removed phone registration, added settings
        }
        else
        {
            alert("Please enter phone no.");
            return;
        }
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06



    });

    $('#btnconfirmphone').click(function(e){

        var phonecode = $('#txtphonecode').val();


=======



    });

=======



    });

>>>>>>> body content changes, removed phone registration, added settings
    $('#btnconfirmphone').click(function(e){

        var phonecode = $('#txtphonecode').val();


<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
>>>>>>> GMaps location markers
=======
>>>>>>> body content changes, removed phone registration, added settings
        if(phonecode.length > 0)
        {

         var phonesignin = {
                     phone: sessionStorage.getItem("phone"),
                     password:phonecode
                   };
<<<<<<< a1437eaeec00e2b2e7d87d7c2b9466aa9b297467
<<<<<<< 2061c287f104acf26a255617776b1d1bcbfa9f06

=======

>>>>>>> GMaps location markers
=======

>>>>>>> body content changes, removed phone registration, added settings
                    //console.log("Request JSON" + JSON.stringify(RegisterData));
                    $.ajax({
                    type: "POST",
                    url: APIURL + "users/phoneSignin",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(phonesignin),
                }).done(function (result) {
                    console.log(result);
                    if(result.id !== undefined)
                    {
                        //LoginToken.token = result.token;
                        //console.log("Token returned " + LoginToken.token);
                        //$('#txtcellno').val("");
                        //alert("You have successfully registered. Please login");
                        if (typeof(Storage) !== "undefined") {
                        // Store
                        sessionStorage.setItem("token", result.token);
                        sessionStorage.setItem("id", result.id);
                    }
                        window.location.href = "user/index.html";
                    }
               })
                .fail(function (data, textStatus, xhr) {
                     //console.log(data.responseJSON.Error);
                     alert(data.responseJSON.Error);
                     /*console.log("error", data.status);
                     console.log("STATUS: "+xhr); */
                });
        }
        else
        {
            alert("Please enter phone no.");
            return;
        }
    });
    $('#subscribe').click(function(e){
        var password = $('#password').val();
        var confirmpassword = $('#confirmpassword').val();
        var email = $('#email').val();
        if(password.length > 0 && confirmpassword.length > 0 && email.length > 0)
        {
            if(password === confirmpassword)
            {
                    RegisterData.email = $('#email').val();
                    RegisterData.password = $('#password').val();
                    RegisterData.name = $("#email").val();
                    //console.log("Request JSON" + JSON.stringify(RegisterData));
                    $.ajax({
                    type: "POST",
                    url: APIURL + "users",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(RegisterData),
                }).done(function (result) {
                    console.log(result);
                    if(result.id !== undefined)
                    {
                        //LoginToken.token = result.token;
                        //console.log("Token returned " + LoginToken.token);
                        $('#email').val("");
                        $('#password').val("");
                        $('#confirmpassword').val("");
                        alert("You have successfully registered. Please log in.");
                        window.location.href = "login.html";
                    }
               })
                .fail(function (data, textStatus, xhr) {
                     //console.log(data.responseJSON.Error);
                     alert(data.responseJSON.Error);
                     /*console.log("error", data.status);
                     console.log("STATUS: "+xhr); */
                });
            }
            else
            {
                alert("Passwords much match");
                 $('#password').val("");
                 $('#confirmpassword').val("");
                return;
            }
        }
        else
        {
            alert("Please enter email and password.");
            return;
        }
    });
})
