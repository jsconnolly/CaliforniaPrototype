$(document).ready(function(){

    if(sessionStorage.getItem("admintoken") != null)
    {
            $.ajax({
                type: "POST",
                url: APIURL + "admin/alerts/search",
                headers: {
                'token': sessionStorage.getItem("admintoken"),
                'Content-Type':'application/json'
             },
                }).done(function (result) {
                  //console.log(result);
                result.sort(date_sort_desc);
                //console.log(descendingOrder);
                 for(i = 0; i<result.length;i++)
                 {
                     if( i <= 4)
                    {
                        var d = new Date(result[i].date);
                        var appendedval = "<a href='#' class='admin-api-alert'><div class='well'><h5>" + result[i].name +"<span class='pull-right'>" + d.toDateString() +"</span></h5><p>"+ result[i].type  +"</p></div></a>";

                            if(document.getElementById("alerts") != null)
                            {
                                document.getElementById("alerts").innerHTML += appendedval;
                            }
                    }
                 }

                })
                .fail(function (data, textStatus, xhr) {
                 alert(data.responseJSON.Error);
                });
    }


    if(sessionStorage.getItem("admintoken") != null)
    {
            $.ajax({
                type: "POST",
                url: APIURL + "admin/alerts/search",
                headers: {
                'token': sessionStorage.getItem("admintoken"),
                'Content-Type':'application/json'
             },
                }).done(function (result) {
                  //console.log(result);
                result.sort(date_sort_desc);
                //console.log(descendingOrder);
                 for(i = 0; i<result.length;i++)
                 {
                     if( i <= 4)
                    {
                        var d = new Date(result[i].date);
                        var appendedval = "<a href='#' class='admin-current-alert'><div class='well'><div class='row'><div class='col-md-2'>"+ result[i].name +"</div><div class='col-md-2'>" + d.toDateString() +"</div><div class='col-md-8'>"+ result[i].location  +"</div></div></div></a>";

                            if(document.getElementById("manualalerts") != null)
                            {
                                document.getElementById("manualalerts").innerHTML += appendedval;
                            }
                    }
                 }

                })
                .fail(function (data, textStatus, xhr) {
                 alert(data.responseJSON.Error);
                });
    }



    $('#btnSaveAlert').click(function(e){
        var addalert = {
          "name":"",
          "type": "",
          "date":"",
          "loc":[],
          "location":"",
          "description":"",
          "internaldescription":""
       }

        var alerttitle = $('#alerttitle').val();
        var alerttype = $('#alerttype').val();
        var alertlocation = $('#alertcityzip').val();
        var interagencyinfo = $('#alertinteragencyinfo').val();
        var publicannouncement = $('#alertpublicannouncement').val();
        var d = new Date();
        var n = d.toISOString();
        addalert.date = n;

        if(alertlocation.length > 0)
        {


            addalert.loc[0] = getLatLng(alertlocation).lng;
            addalert.loc[1] = getLatLng(alertlocation).lat;
            addalert.name = alerttitle;
            addalert.type = alerttype;
            addalert.location = alertlocation;
            addalert.description = publicannouncement;
            addalert.internaldescription = interagencyinfo;
            console.log("Request JSON Add Alert" + JSON.stringify(addalert));
            console.log("Admin Token " + sessionStorage.getItem("admintoken"));


                           $.ajax({
                            type: "POST",
                            url: APIURL + "admin/alerts",
                            headers: {
                            'token': sessionStorage.getItem("admintoken"),
                            'Content-Type':'application/json'
                            },
                            dataType: "json",
                            data: JSON.stringify(addalert),
                            })
                            .done(function (result) {
                                 console.log(result);
                                if(result.id !== undefined)
                                {
                                    alert("Alert added successfully.");
                                    $('#alerttitle').val("");
                                    $('#alerttype').val("");
                                    $('#alertcityzip').val("");
                                    $('#alertinteragencyinfo').val("");
                                    $('#alertpublicannouncement').val("");
                                    location.reload();
                                }
                            })
                            .fail(function (data, textStatus, xhr) {
                             alert("There seems to be a problem with adding alert.");
                             return;
                            });




        }







    });



    
});


var date_sort_desc = function (a, b) {
  if (a.date > b.date) return -1;
  if (a.date < b.date) return 1;
  return 0;
};
