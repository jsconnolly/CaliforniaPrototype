$(document).ready(function(){

    var RegisterData = {
    email: "",
    password: "",
    name:"",
    phone:""
};




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
                    RegisterData.phone = Math.floor(100000000 + Math.random() * 900000000); // generates a random phone no -- REMOVE AFTER Register endpoint is fixed
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
                        /*$('#popupAlert').on('show.bs.modal', function (event) {
                          var modal = $(this);
                          modal.find('#alertTitle').text('Successfully Registered');
                          modal.find('#alertBody').text('You have successfully registered. Please log in.');
                          modal.find('#alertFooter').html('<a href="/login.html" class="btn btn-default">Log In</a>')
                      });*/
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
            //alert("Please enter email and password.");
            return;
        }



    });
})
