$(document).ready(function(){
    $('#phoneNotifications .btn').click(function(e){
        $('#phoneNotifications').addClass('hidden');
        $('#addContact').removeClass('hidden');
    });
    $('#addContact .btn').click(function(e){
        $('#addContact').addClass('hidden');
        $('#phoneNotifications').removeClass('hidden');
    });
})
