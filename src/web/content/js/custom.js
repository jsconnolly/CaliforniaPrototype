$(document).ready(function(){
    $('#phoneNotifications .btn').click(function(e){
        $('#phoneNotifications').addClass('hidden');
        $('#addContact').removeClass('hidden');
    });
    $('#addContact .btn').click(function(e){
        $('#addContact').addClass('hidden');
        $('#phoneNotifications').removeClass('hidden');
    });

    var email = $('input.email').val();

    if (email !== '') {  // If something was entered
        if (!isValidEmailAddress(email)) {
            $('label.email-error'.show(); //error message
            $('input.email').focus();   //focus on email field
            return false;
        }
    }

    function isValidEmailAddress(emailAddress) {
        var pattern = new RegExp(/^(('[\w-+\s]+')|([\w-+]+(?:\.[\w-+]+)*)|('[\w-+\s]+')([\w-+]+(?:\.[\w-+]+)*))(@((?:[\w-+]+\.)*\w[\w-+]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][\d]\.|1[\d]{2}\.|[\d]{1,2}\.))((25[0-5]|2[0-4][\d]|1[\d]{2}|[\d]{1,2})\.){2}(25[0-5]|2[0-4][\d]|1[\d]{2}|[\d]{1,2})\]?$)/i);
        return pattern.test(emailAddress);
    };

});

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}
