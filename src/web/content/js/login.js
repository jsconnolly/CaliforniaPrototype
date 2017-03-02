$(document).ready(function(){

    var LoginData = {
        email: "",
        password: ""
    };

    var LoginAdminData = {
        email: "",
        password: ""
    };


   var LoginResponse = { token:"", id:""};
    $('#loginbtn').click(function(e){
        var password = $('#password').val();
        var username = $('#username').val();
        if(password.length > 0 && username.length > 0)
        {
            var email = username.toLowerCase();
            LoginData.email = email;
            LoginData.password = password;
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
                setCookie("phone",result.phone,1);
                setCookie("email",result.email,1);
                //localStorage.setItem("locations", JSON.stringify(result.locations));
                //setCookie("locations",JSON.stringify(result.locations),1);
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



    $('#btnAdminLogin').click(function(e){
        var password = $('#adminpassword').val();
        var username = $('#adminusername').val();
        //admin@admin.com / password1
        if(password.length > 0 && username.length > 0)
        {
            var email = username.toLowerCase();
            LoginAdminData.email = email;
            LoginAdminData.password = password;
            //console.log("Request JSON" + JSON.stringify(LoginData));
            $.ajax({
            type: "POST",
            url: APIURL + "users/signin",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(LoginAdminData),
            }).done(function (result) {
            console.log(result);
            if(result.token !== undefined)
            {

                if (typeof(Storage) !== "undefined") {
                        // Store
                        sessionStorage.setItem("admintoken", result.token);
                        sessionStorage.setItem("adminid", result.id);

                    }

                setCookie("adminid",result.id,1);
                setCookie("admintoken",result.token,1);
                //LoginResponse.token = result.token;
                //LoginResponse.id = result.id;
                //LoginResponse.email = result.token;
                //LoginToken.id=result.id;
                //console.log("Token returned " + LoginToken.token);
                $('#password').val("");
                $('#username').val("");
                window.location.href = "landing.html";
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
