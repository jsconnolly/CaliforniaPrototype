$(document).ready(function(){
    var AccountRecovery = {
        email: ""
    };
    var ResetPassword = {
        email: "",
        password:""
    };
    
    $('#btnAccountRecovery').click(function(e){
        
        var email = $('#recoveryemail').val();

        if(email.length > 0)
        {
            
            var email = email.toLowerCase();
            AccountRecovery.email = email;

            $.ajax({
            url: APIURL + "users/forgotPassword",
            method: "POST",
            dataType: "json",
            cache:false,
            async:false,
            contentType: "application/json",
            data: JSON.stringify(AccountRecovery),
            success:function(result){
                  console.log(result);
                  //localStorage.setItem("recoveremail",email);
                  //location.href = "accountrecovery-sent.html";
                  //$("#recoveryemail").val("");
                 }

             });

        }
        else
        {
            alert("Please enter email.");
            return;
        }
        
           
        
    });
    
    
    
        $('#resetpassword').click(function(e){
        var password = $('#recoverpassword').val();
        var confirmpassword = $('#recoverconfirmpassword').val();
        var email = getUrlParameter('email');
        var token = getUrlParameter('token');
            
        ResetPassword.email = email;
        ResetPassword.password = confirmpassword;

        if(password.length > 0 && confirmpassword.length > 0)
        {
            $.ajax({
            type: "POST",
            url: APIURL + "users/resetPassword",
            headers:
            {
                'token':token,
                'Content-Type':'application/json'
            },
            dataType: "json",
            cache:false,
            data: JSON.stringify(ResetPassword),
            }).done(function (result) {
              console.log(result);
              location.href = "login.html";
              $("#recoverpassword").val("");
              $("#recoverconfirmpassword").val("");
            })
            .fail(function (data, textStatus, xhr) {
             alert(data.responseJSON.Error);
            });
        }
        else
        {
            alert("Please enter New Password and Re-Type Password.");
            return;
        }
        
           
        
    });
    
   
});



function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};