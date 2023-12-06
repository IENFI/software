function changePWOpen(){ // 일정 추가 화면 띄우기
    document.getElementById('changePWModal').style.display = 'block';

    let id = document.getElementById('memberID').value;
    document.getElementById('changePW_memberID').value = id;

}

function changePWClose(){ // 일정 추가 화면 띄우기
    document.getElementById('changePWModal').style.display = 'none';
}

function changePW(){

    let pw = document.getElementById('memberPW').value;
    let pwCheck = document.getElementById('memberPWCheck').value;
    if(pw) {
        if (pw == pwCheck) {
            alert('비밀번호가 변경되었습니다.');
            planClose();
            return true;
        } else {
            alert('비밀번호를 확인해주세요.');
            return false;
        }
    }
    else {
        alert('변경할 비밀번호를 입력해주세요.')
    }
}