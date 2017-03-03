$(document).ready(function(){

    var RegisterData = {
        email: "",
        password: ""
    };


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
                        alert("You have successfully registered.");
                            if (typeof(Storage) !== "undefined") {
                            // Store
                            sessionStorage.setItem("token", result.token);
                            sessionStorage.setItem("id", result.id);
                            }
                            setCookie("id",result.id,1);
                            setCookie("token",result.token,1);
                            if(result.phone !== undefined)
                            {
                              setCookie("phone",result.phone,1);
                            }
                            else {setCookie("phone","",1);}
                            setCookie("email",result.email,1);
                       window.location.href = "user/index.html";
                        //window.location.href = "login.html";
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


function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

