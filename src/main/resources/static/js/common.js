
function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function auth_info() {
    var accessToken = get_cookie("accessToken");
    if (accessToken == null) {
        return null;
    }

    $.ajaxSetup({
        headers: { 'Authorization': accessToken }
    });

    $.ajax({
        url: '/auth',
        method: 'GET'
    })
    .done(function(result) {
        console.log(result);
    })
    .fail(function() {
        console.log("auth_info fail");
    })
}

auth_info();