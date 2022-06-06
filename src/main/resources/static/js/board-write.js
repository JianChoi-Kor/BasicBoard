
function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function auth_check() {
    var accessToken = get_cookie("access_token");
    if (accessToken == null) {
        alert('로그인이 필요한 서비스입니다.');
        history.back();
        return null;
    }
}

auth_check();

function write_action() {

    var accessToken = get_cookie("access_token");
    if (accessToken == null) {
        return null;
    }

    var title_elem = document.querySelector('#title');
    var contents_elem = document.querySelector('#contents');

    if (title_elem.value === '') {
        title_elem.focus();
        alert('제목을 입력해주세요.');
        return;
    }
    if (contents_elem.value === '') {
        contents_elem.focus();
        alert('내용을 입력해주세요.');
        return;
    }

    var params = {
        title : title_elem.value,
        contents : contents_elem.value
    }

    $.ajaxSetup({
        headers: { 'Authorization': accessToken }
    });

    $.ajax({
        url: '/board/write',
        method: 'POST',
        data: JSON.stringify(params),
        dataType: 'json',
        contentType: 'application/json;charset=utf-8'
    })
    .done(function(result) {
        //실패
        if (result.state == 400) {
            if (result.error.length > 0) {
                var errors = result.error;
                if (errors[0].field === 'title') {
                    title_elem.focus();
                }
                if (errors[0].field === 'contents') {
                    contents_elem.focus();
                }
                alert(result.error[0].message);
                result;
            }
        }
        //성공
        else {
            alert(result.message);
            location.href="/board/main";
            return;
        }
    })
    .fail(function() {
        alert('요청에 실패했습니다.');
        return;
    })
}