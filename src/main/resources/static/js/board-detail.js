
var memberIdx;
var memberName;

function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function auth_info() {
    var accessToken = get_cookie("access_token");
    if (accessToken == null) {
        document.getElementById('header_member_name_a').hidden = true;
        document.getElementById('header_logout_li').hidden = true;
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
        memberIdx = result.data.memberIdx;
        memberName = result.data.memberName;

        if (memberName != null) {
            document.getElementById('header_signin_li').hidden = true;
            document.getElementById('footer_signin_li').hidden = true;

            var headerMemberName = document.getElementById('header_member_name_a');
            headerMemberName.hidden = false;
            headerMemberName.innerText = '"' + memberName + '" 접속 중' ;

            document.getElementById('header_logout_li').hidden = false;
        }
    })
    .fail(function() {
        console.log("auth_info fail");
    })
}

auth_info();

function logout_action() {
    if (window.confirm('로그아웃 하시겠습니까?')) {
        var accessToken = get_cookie("access_token");
        if (accessToken == null) {
            return null;
        }

        $.ajaxSetup({
            headers: { 'Authorization': accessToken }
        });

        $.ajax({
                url: '/auth/logout',
                method: 'POST'
            })
            .done(function(result) {
                //실패
                if (result.state == 400) {
                    alert(result.message);
                    return;
                }
                //성공
                else {
                    set_cookie("access_token", null, 0);
                    set_cookie("refresh_token", null, 0)

                    location.href="/main";
                }
            })
            .fail(function() {
                console.log("logout fail");
            })
    }
    else {
        return;
    }
}

function detail_page() {
    if (memberIdx != null) {
        document.getElementById('login_member_idx').value = memberIdx;
    }
}

function modify_action() {

}