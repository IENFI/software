function openGetMessage(){
    var windowFeatures = 'width=750,height=550,resizable=yes';

    var newWindow = window.open('receiveMessage.html', 'NoteListWindow', windowFeatures);
    if (newWindow === null) {
        alert('팝업이 차단되었습니다. 팝업 차단을 해제하고 다시 시도하세요.');
    }
}