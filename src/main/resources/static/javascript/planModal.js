//일정 쓰기 열기 함수
function planOpen(){ // 일정 추가 화면 띄우기
    document.getElementById('planWrite').style.display = 'block';
    document.getElementById('formWrite').style.display = 'block';
    document.getElementById('formContent').style.display = 'none'; // 수정 위한 폼 숨기기
}

function planContent(id, title, date) { // 일정 상세보기 화면 띄우기
    document.getElementById('planWrite').style.display = 'block';
    document.getElementById('formWrite').style.display = 'none'; // 추가 위한 폼 숨기기
    document.getElementById('formContent').style.display = 'block';

    document.getElementById('plan_Id').value = id;
    document.getElementById('plan_content1').value = title;
    document.getElementById('date_select1').value = date;
}

//일정 쓰기 닫기 함수
function planClose(){
    document.getElementById('planWrite').style.display = 'none';

    // var date = document.getElementById('date_select');
    // var plan = document.getElementById('plan_content');
    //
    // date.value = '';
    // plan.value = ''; // 큰 문제는 없는데 콘솔창에 오류가 뜨길래 주석처리 했어요
}

//일정 추가 함수
function addPlan(){
    var date = document.getElementById('date_select').value;
    var plan = document.getElementById('plan_content').value;
    console.log(date);
    console.log(plan);
    if(date && plan){
        alert('일정이 추가되었습니다!\n날짜:' + date + '\n일정:' + plan);

        planClose();

    }
    else{
        alert('날짜와 일정을 모두 추가해주세요.');
    }
}

function updatePlan(){
    var id = document.getElementById('plan_Id').value;
    var memberId = document.getElementById('plan_memberId1').value;
    var date = document.getElementById('date_select1').value;
    var plan = document.getElementById('plan_content1').value;

    console.log(date);
    console.log(plan);
    if(date && plan){
        alert('일정이 수정되었습니다!\n날짜:' + date + '\n일정:' + plan);

        planClose();

    }
    else{
        alert('날짜와 일정을 모두 추가해주세요.');
    }
}
