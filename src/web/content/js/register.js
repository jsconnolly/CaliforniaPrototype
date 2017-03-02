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
