$(document).ready(function(){

    var RegisterData = {
        email: "",
        password: ""
    };

     $('#btnaddphone').click(function(e){

        var phoneno = $('#txtcellno').val();
        if(phoneno.length > 0)
        {
            console.log("1" + phoneno.replace("-", "").replace("-", ""));
                      var phoneres = {
                     phone: ""
                   };
                    phoneres.phone = "1" + phoneno.replace("-", "").replace("-", "");
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
                    }

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

        }
        else
        {
            alert("Please enter phone no.");
            return;
        }




    });

    $('#btnconfirmphone').click(function(e){

        var phonecode = $('#txtphonecode').val();



        if(phonecode.length > 0)
        {

         var phonesignin = {
                     phone: sessionStorage.getItem("phone"),
                     password:phonecode
                   };

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
                    var email = email.toLowerCase();
                    RegisterData.email = email;
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
                //alert("Passwords much match");
                 $('#password').val("");
                 $('#confirmpassword').val("");

                //alert("Passwords much match");
                 //$('#password').val("");
                 //$('#confirmpassword').val("");
                return;
            }
        }
        else
        {
            return;
        }

    });
});
