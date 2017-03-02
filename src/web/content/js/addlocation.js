


function DeleteLocation(locationId)
{
    $.ajax({
       url : APIURL + "users/" + sessionStorage.getItem("id") + "/locations/" + locationId,
       headers: {
                'token': sessionStorage.getItem("token"),
                'Content-Type':'application/json'
             },
       method: "DELETE",
       async:false,
       success:function(data){
             alert("location deleted successfully");
             location.reload();
       }
    });
    
    //console.log("delete");

}




function getLatLng(cityzip)
{
    var coordinates=  { "lat": "", "lng": ""};

    $.ajax({
       url : "http://maps.googleapis.com/maps/api/geocode/json?address="+cityzip+"&sensor=false",
       method: "POST",
       async:false,
       success:function(data){
           latitude = data.results[0].geometry.location.lat;
           longitude= data.results[0].geometry.location.lng;
           //alert("Lat = "+latitude+"- Long = "+longitude);
           coordinates.lat = latitude;
           coordinates.lng = longitude;
       }

    });

    return coordinates;

}






$(document).ready(function(){

    // Get Added Locations
     if(sessionStorage.getItem("id") != null)
    {
            $.ajax({
                type: "GET",
                url: APIURL + "users/" +sessionStorage.getItem("id"),
                headers: {
                'token': sessionStorage.getItem("token"),
                'Content-Type':'application/json'
             },
                }).done(function (result) {
                //console.log(result);
                if(result.locations !== undefined)
                {
                    //alert("location added successfully");
                    //window.location.href = "/user/index.html";
                    for(i=0;i<result.locations.length;i++)
                    {
<<<<<<< HEAD


                        var appendedval = "<div class='user-block col-sm-3 col-xs-6'><span class='glyphicon glyphicon-map-marker'></span><span class='added-location'>" + result.locations[i].displayName + "</span> <a href='#' class='btn btn-primary  btn-block-half' onclick='EditLocation(" + result.locations[i].id + ")' data-toggle='modal' data-target='#editlocation'>Edit</a> <a href='#' class='btn btn-danger btn-block-half' id='deletelocation(" + result.locations[i].id + ")'>Delete</a> </div>";
=======
                        var appendedval = "<div class='user-block col-sm-3 col-xs-6'><span class='glyphicon glyphicon-map-marker'></span><span class='added-location'>" + result.locations[i].displayName + "</span> <a href='#' class='btn btn-primary  btn-block-half' onclick=\"EditLocation('" + result.locations[i].id + "')\" data-toggle='modal' data-target='#editlocation'>Edit</a> <a href='#' class='btn btn-danger btn-block-half' onclick=\"DeleteLocation('" + result.locations[i].id + "')\">Delete</a> </div>";
>>>>>>> 210367f508298b0dbc5f25d742a62c1701a75dbb
                        //console.log(appendedval);
                        if(document.getElementById("locationsrow") != null)
                            {
                                document.getElementById("locationsrow").innerHTML += appendedval;
                            }
                        }
                    }
                }
            )

            .fail(function (data, textStatus, xhr) {
             //console.log(data.responseJSON.Error);
             alert(data.responseJSON.Error);
             /*console.log("error", data.status);
             console.log("STATUS: "+xhr); */
            });
    }

        $('#addlocation').click(function(e){
        var addlocation = {
          "displayName":"",
          "coordinates": "",
          "alertRadius":"100",
          "enablePushNotifications":true,
          "enableSMS":true,
          "enableEmail":true
       }
        var cityzip = $('#cityzip').val();
        if(cityzip.length > 0)
        {
            // Get lat and lng from google maps api
            //console.log("Latitude" + getLatLng(cityzip).lat);
            //console.log("Longitude" + getLatLng(cityzip).lng);

            addlocation.coordinates = { "lat": getLatLng(cityzip).lat, "lng": getLatLng(cityzip).lng};
            addlocation.displayName = $("#locationname").val();
            console.log("Request JSON" + JSON.stringify(addlocation));


            // add ajax code to add a location
                            if (typeof(Storage) !== "undefined") {
                        // Store
                        //console.log(localStorage.getItem("token"));
                        //console.log(localStorage.getItem("id"));

                            $.ajax({
                            type: "PUT",
                            url: APIURL + "users/" +sessionStorage.getItem("id") + "/locations",
                            headers: {
                            'token': sessionStorage.getItem("token"),
                            'Content-Type':'application/json'
                         },
                            dataType: "json",
                            data: JSON.stringify(addlocation),
                            }).done(function (result) {
                            console.log(result);
                            if(result.token !== undefined)
                            {
                                alert("Location added successfully.");
                                window.location.href = "/user/index.html";

                            }
                            })
                            .fail(function (data, textStatus, xhr) {
                             //console.log(data.responseJSON.Error);
                             alert(data.responseJSON.Error);
                             /*console.log("error", data.status);
                             console.log("STATUS: "+xhr); */
                            });

                    }

            console.log("Latitude" + getLatLng(cityzip).lat);
            console.log("Longitude" + getLatLng(cityzip).lng);

            addlocation.coordinates = { "lat": getLatLng(cityzip).lat, "lng": getLatLng(cityzip).lng};
            addlocation.displayName = "location" + Math.random();
            console.log("Request JSON" + JSON.stringify(addlocation));


            // add ajax code to add a location
                            if (typeof(Storage) !== "undefined") {
                        // Store
                        //console.log(localStorage.getItem("token"));
                        //console.log(localStorage.getItem("id"));

                            $.ajax({
                            type: "PUT",
                            url: APIURL + "users/" +sessionStorage.getItem("id") + "/locations",
                            headers: {
                            'token': sessionStorage.getItem("token"),
                            'Content-Type':'application/json'
                         },
                            dataType: "json",
                            data: JSON.stringify(addlocation),
                            }).done(function (result) {
                            console.log(result);
                            if(result.token !== undefined)
                            {
                                alert("Location added successfully.");
                                window.location.href = "/user/index.html";

                            }
                            })
                            .fail(function (data, textStatus, xhr) {
                             //console.log(data.responseJSON.Error);
                             alert(data.responseJSON.Error);
                             /*console.log("error", data.status);
                             console.log("STATUS: "+xhr); */
                            });

                    }

        }
        else
        {
            alert("Please enter city or zip.");
            return;
        }
    });
})


function EditLocation(locationId)
{



}


    });


});


function getLatLng(cityzip)
{
    var coordinates=  { "lat": "", "lng": ""};
    $.ajax({
       url : "http://maps.googleapis.com/maps/api/geocode/json?address="+cityzip+"&sensor=false",
       method: "POST",
       async:false,
       success:function(data){
           latitude = data.results[0].geometry.location.lat;
           longitude= data.results[0].geometry.location.lng;
           //alert("Lat = "+latitude+"- Long = "+longitude);
           coordinates.lat = latitude;
           coordinates.lng = longitude;
       }

    });

    return coordinates;

}


});
