//달력 생성 함수
function generateCalendar(year, month){

    const today = new Date();
    const dayInMonth = new Date(year, month, 0).getDate(); //달의 일수
    const firstDay = new Date(year, month - 1, 1).getDay(); //달의 첫 요일


    let calendarHTML = '<table>';
    calendarHTML += '<tr id="calendarDate"><th class="Sun">일요일</th><th>월요일</th><th>화요일</th><th>수요일</th><th>목요일</th><th>금요일</th><th class="Sat">토요일</th></tr>'
    let dayCounter = 1;



    for(let i = 0; i < 6; i++) {

        calendarHTML += '<tr>';

        for(let j = 0; j < 7; j++) {
            const holiday = new Date(year, month - 1, dayCounter).getDay();
            if(i == 0 && j < firstDay){
                calendarHTML += '<td></td>';
            }
            else if(dayCounter > dayInMonth){
                calendarHTML += '<td></td>';
            }
            else {
                if(dayCounter == today.getDate() && year == today.getFullYear() && month == today.getMonth() + 1){
                    //calendarHTML += `<td id="today">${dayCounter}<div class="plan_div" id="${dayCounter}"></div></td>`;
                    if(holiday == 0){
                        calendarHTML += `<td><div class="Sun">${dayCounter}</div><div class="plan_div" id="${dayCounter}"></div></td>`;
                    }
                    else if(holiday == 6){
                        calendarHTML += `<td id="today"><div class="Sat">${dayCounter}</div><div class="plan_div" id="${dayCounter}"></div></td>`;
                    }
                    else{
                        calendarHTML += `<td id="today">${dayCounter}<div class="plan_div" id="${dayCounter}"></div></td>`;
                    }
                }
                else if(holiday == 0){
                    calendarHTML += `<td><div class="Sun">${dayCounter}</div><div class="plan_div" id="${dayCounter}"></div></td>`;
                }
                else if(holiday == 6){
                    calendarHTML += `<td><div class="Sat">${dayCounter}</div><div class="plan_div" id="${dayCounter}"></div></td>`;
                }

                else{
                    calendarHTML += `<td>${dayCounter}<div class="plan_div" id="${dayCounter}"></div></td>`;
                }
                dayCounter++;
            }

        }

        calendarHTML += '</tr>';
    }

    calendarHTML += '</table>';

    document.getElementById('Calendar_Container').innerHTML = calendarHTML;

    if(month == 0){
        document.getElementById('currentYearMonth').innerHTML = `${year - 1}년 ${month + 12}월`;
    }
    else{
        document.getElementById('currentYearMonth').innerHTML = `${year}년 ${month}월`;
    }

}


const pos = new Date();
pos.setMonth(pos.getMonth() + 1);

//달력 보여주는 함수
function showCalendar(change){
    if(change == -1){
        prevCalendar(change);
    }
    else{
        nextCalendar(change);
    }
}

//다음 월 버튼
function prevCalendar(change){
    pos.setMonth(pos.getMonth() + change);
    generateCalendar(pos.getFullYear(), pos.getMonth());
    pos += -1;

}

//이전 월 버튼
function nextCalendar(change){
    pos.setMonth(pos.getMonth() + change);
    generateCalendar(pos.getFullYear(), pos.getMonth());
    pos += 1;
}

generateCalendar(pos.getFullYear(), pos.getMonth());