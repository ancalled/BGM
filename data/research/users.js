$('#username_go_btn').click(function(event){
    event.preventDefault();
    $('.error_msg').remove();
    $.post('/dampinterop/userLogin.php', {email:$('#username').val()}, function(resp){
        if(resp.ok) {
            if(resp.reload)
                document.location.reload();
            else {
                $('body').append(resp.content);
                $.getScript(resp.script);
            }
        }
        else {
            $("form[name='damplogin']").append("<p class='error_msg'>"+resp.msg+"</p>")
        }
    });
});