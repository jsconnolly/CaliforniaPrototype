$(document).ready(function(){

    
    $('#btnconfirmphone').click(function(e){

        console.log("I am here");
        
        var txtphonecode1 = $('#txtphonecode1').val();
        var txtphonecode2 = $('#txtphonecode2').val();
        var txtphonecode3 = $('#txtphonecode3').val();
        var txtphonecode4 = $('#txtphonecode4').val();
        var txtphonecode5 = $('#txtphonecode5').val();
        var txtphonecode6 = $('#txtphonecode6').val();
        
        
        
        
        if(txtphonecode1.length > 0 && txtphonecode2.length > 0 && txtphonecode3.length > 0 && txtphonecode4.length > 0 && txtphonecode5.length > 0 && txtphonecode6.length > 0)
        {
            
             var UpdatePhone = {
                name:"",
                email: "",
                phone: ""
            };
            var phonecode = txtphonecode1 + txtphonecode2 + txtphonecode3+ txtphonecode4+txtphonecode5+txtphonecode6;

            var rphonecode = getCookie("phonecode");
            var phone = getCookie("phone");
            UpdatePhone.phone = phone
            
            console.log("Phone Code"  + getCookie("phonecode"));
            console.log("Phone"  + getCookie("phone"));
            console.log("id"  + getCookie("id"));
            console.log("token"  + getCookie("token"));
            console.log("JSON"  + JSON.stringify(UpdatePhone));
            

            
            if(rphonecode !== null)
            {
                 if(phonecode === rphonecode)
                  {
                    
                      $.ajax({
                                type: "PUT",
                                url: APIURL + "users/" + getCookie("id"),
                                 headers: {
                                'token': getCookie("token"),
                                'Content-Type':'application/json'
                         },
                                dataType: "json",
                                contentType: "application/json",
                                data: JSON.stringify(UpdatePhone),
                            }).done(function (result) {
                              //console.log(result);
                              location.href = "index.html";
                            })
                            .fail(function (data, textStatus, xhr) {
                             //console.log(data.responseJSON.Error);
                             alert(data.responseJSON.Error);
                             /*console.log("error", data.status);
                             console.log("STATUS: "+xhr); */
                            });
                            
                     
                  }
             }
            
            

        }




    });

    

        

});



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



