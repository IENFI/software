document.addEventListener("DOMContentLoaded", function() {
    var messageList = document.getElementById("message_content");
    var messages = messageList.getElementsByClassName("list_number");

    for (var i = 0; i < messages.length; i++) {
        var index = i + 1;
        messages[i].innerText =  index;
    }
});

