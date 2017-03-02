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
                 //console.log(data.responseJSON.Error);
                 alert(data.responseJSON.Error);
                 /*console.log("error", data.status);
                 console.log("STATUS: "+xhr); */
                });
    }
    
    
});


var date_sort_desc = function (date1, date2) {
  if (date1 > date2) return -1;
  if (date1 < date2) return 1;
  return 0;
};