function deleteMessage(){
    var list = document.getElementById('message_content');
    var items = list.getElementsByClassName('message_list');

    /*for(var j = Items.length - 1; j >= 0; j--){
        var checkBox = Items[j].querySelector('input[type="checkbox"]');
        if(checkBox.checked){

            var ok = confirm("선택한 메세지를 삭제하시겠습니까?");
            if(ok == true){
                for(var i = items.length - 1; i >= 0; i--){
                    var checkbox = items[i].querySelector('input[type="checkbox"]');
                    if(checkbox.checked){
                        list.removeChild(items[i]);
                    }
                }
                alert("메세지를 삭제했습니다.");
                break;
            }
            else if(ok == false){
                alert("메세지 삭제를 취소합니다.");
                break;
            }
        }
        else{
            alert("삭제할 메세지를 선택해주세요.");
            break;
        }
    }*/
    var count = 0;
    for(var j = items.length - 1; j >= 0; j--){
        var checkbox = items[j].querySelector('input[type="checkbox"]');
        if(checkbox.checked){
            count += 1;
        }
    }
    if(count >= 1){
        var ok = confirm("선택한 메세지를 삭제하시겠습니까?");
        if(ok == true){
            for(var i = items.length - 1; i >= 0; i--){
                var checkbox = items[i].querySelector('input[type="checkbox"]');
                if(checkbox.checked){
                    list.removeChild(items[i]);
                }
            }
            alert("메세지를 삭제했습니다.");
            messageIndex();
        }
        else if(ok == false){
            alert("메세지 삭제를 취소합니다.");
        }
    }
    else{
        alert("삭제할 메세지를 선택해주세요.");
    }
}

function messageIndex(){
    var messageList = document.getElementById("message_content");
    var messages = messageList.getElementsByClassName("list_number");

    for(var i = 0; i < messages.length; i++){
        var index = i + 1;
        messages[i].innerText = index;
    }
}