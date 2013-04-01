
function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

jQuery(function ($) {
//	$('#confirm-dialog input.confirm, #confirm-dialog a.confirm').click(function (e) {
//		e.preventDefault();

    // example of calling the confirm function
    // you must use a callback function to perform the "yes" action

    var x = readCookie('satv[cookies_accepted]');
    if (x != 1 && document.URL.indexOf("cookiepolicyeu") == -1) {
        confirm("SonyATV.com would like to place cookies on your computer to help make this website better and enable it to provide certain functions To find out more about cookies, see our <a href='#' class='cookie-privacy-notice-link'>cookie policy</a>.<br /><br/> I agree to accept cookies from this site", function () {
            createCookie('satv[cookies_accepted]',1,1095);
            if (document.URL.indexOf("cookiepolicy.php") == -1)
            {
                window.location.reload();
            }
            else
            {
                window.location.href = 'https://' + location.host + $("#confirm .yes").attr("referer");
            }
        });
    }

    //});
});

function callCookiePolicy() {
    window.location.href = 'https://' + location.host + '/cookiepolicyeu.php';
}

function confirm(message, callback) {

    $('#confirm').modal({
//		closeHTML: "<a href='#' title='Close' class='modal-close'>x</a>",
        position: ["14%",],
        overlayId: 'confirm-overlay',
        containerId: 'confirm-container',
        close: false,


        onShow: function (dialog) {
            var modal = this;

            $('.message', dialog.data[0]).append(message);

            $('.cookie-privacy-notice-link').click(function(){
                modal.close();
                callCookiePolicy();
            });

            // if the user clicks "yes"
            $('.yes', dialog.data[0]).click(function () {
                // call the callback
                if ($.isFunction(callback)) {
                    callback.apply();
                }
                // close the dialog
                modal.close(); // or $.modal.close();
            });

            // if the user clicks "no"
            $('.no', dialog.data[0]).click(function () {
                window.location.href = 'https://' + location.host + '/cookiepolicyeu.php';
            })
        }
    });
}