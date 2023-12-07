const confirmPW = document.getElementById('PW_clear');
const firstPW = document.getElementById('PW');
const caution_div = document.getElementById('caution');

confirmPW.addEventListener("input", function(){
if(firstPW.value === confirmPW.value){
caution.style.color = 'green';
caution_div.innerHTML = "비밀번호가 일치합니다.";
caution.style.display = 'block';
}
else{
caution.style.color = 'red';

caution.style.display = 'block';
caution_div.innerHTML = "비밀번호가 일치하지 않습니다.";
}
})