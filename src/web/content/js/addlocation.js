$(document).ready(function(){
    
    

    
    $('#addlocation').click(function(e){
                    var addlocation = { 
          "displayName":"Location10",
          "coordinates": "",
          "alertRadius":"100",
          "enablePushNotifications":true,
          "enableSMS":false,
          "enableEmail":true
       }
        
        var cityzip = $('#cityzip').val();
        if(cityzip.length > 0)
        {
            // Get lat and lng from google maps api
            console.log("Latitude" + getLatLng(cityzip).lat);
            console.log("Longitude" + getLatLng(cityzip).lng);        
            
            addlocation.coordinates = { "lat": getLatLng(cityzip).lat, "lng": getLatLng(cityzip).lng};
        
            console.log("Request JSON" + JSON.stringify(addlocation));
            
            
            // add ajax code to add a location
        
        }
        else
        {
            alert("Please enter city or zip.");
            return;
        }
        

        
    });
})


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