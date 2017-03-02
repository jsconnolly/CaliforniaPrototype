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
    
    
});


var date_sort_desc = function (a, b) {
  if (a.date > b.date) return -1;
  if (a.date < b.date) return 1;
  return 0;
};