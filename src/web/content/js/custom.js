$(document).ready(function(){
    $('#phoneNotifications .btn').click(function(e){
        $('#phoneNotifications').addClass('hidden');
        $('#addContact').removeClass('hidden');
    });
    $('#addContact .btn').click(function(e){
        $('#addContact').addClass('hidden');
        $('#phoneNotifications').removeClass('hidden');
    });

    $('.phone').mask('000-000-0000');

    $('#unsubscribe').click(function() {
        $('#modal').on('show.bs.modal', function (event) {
          var modal = $(this);
          modal.find('.modal-title').text('Confirm Unsubscribe');
          modal.find('.modal-body').text('Are you sure you want to unsubscribe?');
          modal.find('.modal-footer').html('<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button> <button type="button" class="btn btn-danger" id="confirmunsubscribe">Unsubscribe</button>');
      });

  });




/*    $("#deletelocation(" + result.locations[i].id + ")").click(function() {
        $("#modal").on('show.bs.modal', function (event) {
          var modal = $(this);
          modal.find(".modal-title").text("Confirm Delete Location");
          modal.find(".modal-body").text("Are you sure you want to delete this location?");
          modal.find(".modal-footer").html("<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button> <button type='button' class='btn btn-danger' id='confirmdelete(" + result.locations[i].id + ")'>Delete</button>");
      });
  });*/

});

  $('#btnlogout').click(function() {
      //console.log("in logout button");
      sessionStorage.clear();

});
});

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}
