var messageList = document.querySelector('.message_list');
    var emptyTextDiv = document.getElementById('message_content');

    if (!messageList || messageList.children.length === 0) {
        console.log("메세지가 없습니다.");
        emptyTextDiv.innerHTML += `<div id="emptyText">메세지가 없습니다.</div>`;
    }

    function submitForm() {
            document.getElementById("deleteForm").submit();
        }

        document.addEventListener('DOMContentLoaded', function () {

            const masterCheckbox = document.getElementById('masterCheckbox');


            masterCheckbox.addEventListener('change', function () {
            const childCheckboxes = document.querySelectorAll('.childCheckbox');


            childCheckboxes.forEach(function (checkbox) {
                checkbox.checked = masterCheckbox.checked;
                });
            });
        });