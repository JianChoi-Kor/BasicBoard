function signin_action() {

    var email_elem = document.querySelector('#floatingInput');
    var password_elem = document.querySelector('#floatingPassword');

    if (email_elem.value === '') {
        email_elem.focus();
        alert('이메일을 입력해주세요.');
    } else if (password_elem.value === '') {
        password_elem.focus();
        alert('비밀번호를 입력해주세요.');
    }

    var params = {
        email : email_elem.value,
        password : password_elem.value
    }

    $.ajax({
        url: '/auth/signin',
        method: 'POST',
        data: JSON.stringify(params),
        dateType: 'json',
        contentType: 'application/json;charset=utf-8'
    })
    .done(function(result) {
        //실패
        if (result.state == 400) {
            if (result.error.length > 0) {
                var errors = result.error;
                if (errors[0].field === 'email') {
                    email_elem.focus();
                }
                if (errors[0].field === 'password') {
                    password_elem.focus();
                }
                alert(result.error[0].message);
            }
        }
        //성공
        else {
            var accessToken = result.data.grantType + ' ' + result.data.accessToken;
            var refreshToken = result.data.grantType + ' ' + result.data.refreshToken;
            var refreshTokenExpirationTime = result.data.refreshTokenExpirationTime;

            console.log(accessToken);
            console.log(refreshToken);
        }
    })
    .fail(function() {
        alert('요청에 실패했습니다.');
    })
}