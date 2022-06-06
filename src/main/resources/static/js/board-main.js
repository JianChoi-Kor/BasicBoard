
function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function write_page() {
    console.log('write_page()');
    var accessToken = get_cookie("access_token");
    if (accessToken == null) {
        return null;
    }

    fetch('/board/write', {
        method: 'GET'
    }).then()
}