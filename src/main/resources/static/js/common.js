
var memberName;

function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function auth_info() {
    var accessToken = get_cookie("accessToken");
    if (accessToken == null) {
        document.getElementById('header_member_name_a').hidden = true;
        document.getElementById('footer_member_name_a').hidden = true;
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
        memberName = result.data;
        if (memberName != null) {
            document.getElementById('header_signin_li').hidden = true;
            document.getElementById('footer_signin_li').hidden = true;

            var headerMemberName = document.getElementById('header_member_name_a');
            var footerMemberName = document.getElementById('footer_member_name_a');

            headerMemberName.hidden = false;
            headerMemberName.innerText = memberName;
            footerMemberName.hidden = false;
            footerMemberName.innerText = memberName;
        }
    })
    .fail(function() {
        console.log("auth_info fail");
    })
}

auth_info();