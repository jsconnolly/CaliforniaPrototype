$(document).ready(function(){
    
     $('#btnAddPhoneNumber').click(function() {

         if(sessionStorage.getItem("id") !== null)
        {
                       
            var phoneno = "1" + $("#phoneno").val().replace("-","").replace("-","");
             var addphone = {
                             phone:phoneno
                       };
            
            console.log(addphone.phone);
            console.log(getCookie("token"));
                        $.ajax({
                        type: "POST",
                        url: APIURL + "users/verifyPhone" ,
                        headers: {
                                'token': getCookie("token"),
                                'Content-Type':'application/json'
                             },
                        async:false,
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(addphone),
                    }).done(function (result) {
                       console.log(result.code);  
                        console.log(phoneno); 
                       setCookie("phonecode",result.code,1);
                       setCookie("phone",phoneno,1);
                       location.href = "/user/confirmphone.html";
                   })
                    .fail(function (data, textStatus, xhr) {
                         //console.log(data.responseJSON.Error);
                         
                         alert("There seems to be an issue with adding a phone. Please try again." + data.responseJSON.Error);
                        //location.href = "/user/index.html";
                         /*console.log("error", data.status);
                         console.log("STATUS: "+xhr); */
                    });
        
        }

    });
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
})




function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
} 


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