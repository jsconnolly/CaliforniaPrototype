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

});

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}
