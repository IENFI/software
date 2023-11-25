var todayYear = new Date().getFullYear();
var todayMonth = new Date().getMonth() + 1;
var todayDate = new Date().getDate();

//오늘의 년,월,일 출력
if(todayMonth == 0){
    document.getElementById('todayYear_month').innerHTML = `${todayYear - 1}년 ${todayMonth + 12}월`;
    document.getElementById('todayDate').innerHTML = `${todayDate}`;
}
else{
    document.getElementById('todayYear_month').innerHTML = `${todayYear}년 ${todayMonth}월`;
    document.getElementById('todayDate').innerHTML = `${todayDate}`;
}