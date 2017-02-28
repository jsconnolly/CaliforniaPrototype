$(document).ready(function(){

    var LoginData = {
    email: "",
    password: ""
};


   var LoginResponse = { token:"", id:""};


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
            alert("Please enter email and password.");
            return;
        }



    });
})



/*function getUserId(email,logintoken)
{

    var userId;
    $.ajax({
       url : "http://ec2-54-241-144-61.us-west-1.compute.amazonaws.com/users/email/" + email,
       headers: {
            'token':logintoken,
            'Content-Type':'application/json'
         },
       method: "GET",
       async:false,
       success:function(data){
           console.log("Response from getUserId " + data);
           userId = data.id;
       }

    });

    return userId;

}*/
