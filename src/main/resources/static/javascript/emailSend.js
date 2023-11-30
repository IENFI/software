let returnCode;
let check;
function sendCode() {
    let code = new XMLHttpRequest();
    code.open('GET', '/emailSend?memberEmail=' + encodeURIComponent(document.getElementById('memberEmail2').value), true);

    code.onload = function () {
        if(code.status === 200) {
            returnCode = code.responseText;
        }
    }
    code.send();
}

function equalCode() {
    let code = document.getElementById('memberEmailNum').value;
    if(code == returnCode) {
        check = 'true';
        alert("이메일 인증에 성공했습니다.");
        document.getElementById('check').value = check
    }
    else {
        check='false';
        alert("이메일 인증에 실패했습니다.");
        document.getElementById('check').value = check;
    }
}

function findPW() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/member/findPW?memberID='+encodeURIComponent(document.getElementById('memberID').value)
        +'&memberName='+encodeURIComponent(document.getElementById('memberName2').value)
        +'&memberEmail='+encodeURIComponent(document.getElementById('memberEmail2').value)
        +'&check='+encodeURIComponent(document.getElementById('check').value), true);

    xhr.onload = function () {
        if (xhr.status === 200) {
            let responseData = xhr.responseText;
            if (responseData == 'null') {
                alert('가입된 아이디가 없습니다.');
            } else if (responseData == 'incorrect') {
                alert("인증 정보를 다시 확인해주세요.");
            } else {
                alert("비밀번호 변경 가능합니다.");
                changePWOpen();
            }
        }
    };
    xhr.send();
}