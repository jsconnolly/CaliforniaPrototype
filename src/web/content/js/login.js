$(document).ready(function(){

    var LoginData = {
    email: "",
    password: ""
};



    $('#loginbtn').click(function(e){
        var password = $('#password').val();
        var username = $('#username').val();
        if(password.length > 0 && username.length > 0)
        {
            LoginData.email = $('#username').val();
            LoginData.password = $('#password').val();
            //console.log("Request JSON" + JSON.stringify(LoginData));
            $.ajax({
            type: "POST",
            url: APIURL + "users/signin",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(LoginData),
            }).done(function (result) {
            console.log(result);
            if(result.token !== undefined)
            {

                if (typeof(Storage) !== "undefined") {
                        // Store
                        sessionStorage.setItem("token", result.token);
                        sessionStorage.setItem("id", result.id);

                    }
                
                setCookie("id",result.id,1);
setCookie("token",result.token,1);
                //LoginResponse.token = result.token;
                //LoginResponse.id = result.id;
                //LoginResponse.email = result.token;
                //LoginToken.id=result.id;
                //console.log("Token returned " + LoginToken.token);
                $('#password').val("");
                $('#username').val("");
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

            //alert("Please enter email and password.");
            return;
        }

    });
})



function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
} 
