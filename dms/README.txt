project GitHub url : https://github.com/IENFI/software.git
website : http://aien0118.cafe24.com

<로컬 실행>
1. 프로젝트 다운 받아서 intellij로 build gradle 눌러서 project open
2. java 17 다운로드 하기
3. settings>gradle (검색)
- Build and run using > intellij IDEA
- Run tests using > intellij IDEA
- gradle jvm > java 17
	로 전부 변경하기
4. project structure>sdk>java 17로 바꾸기
5. Lombok 다운하기 (settings>plugins>Marketplace>Lombok 검색>install)
6. 프로젝트의 resources 폴더에 있는 application.properties 수정하기
- file.dir = 웹사이트의 파일을 다운로드할 경로 지정
- spring.datasource.username=root (mysql 유저 이름 입력)
- spring.datasource.password=0000 (mysql 접근비밀번호 입력)
7. mysql에서 schema dms라는 이름으로 생성하기 (혹은 첨부된 스키마 다운해서 쓰기!)
8. spring.mail.password = 과제란에 적혀있는 비밀번호 입력하기

<웹사이트 예쁘게 보이는 화면세팅>
디스플레이 해상도 : 2560 x 1600
배율 : 150%

<관리자 계정은 DB 단에서 생성 : mySql app> 
- 관리자 : 누리지기, 공부꾼 생성하는 예시
use dms;
INSERT INTO member (member_dept, member_email, member_id, member_interest, member_name, member_password, member_phone_number, member_role)
VALUES ('관리자', 'admin@admin', 'admin', '관리자', '누리지기', 'admin', '010', 1);
INSERT INTO member (member_dept, member_email, member_id, member_interest, member_name, member_password, member_phone_number, member_role)
VALUES ('관리자1', 'admin1@admin1', 'admin1', '관리자', '공부꾼', 'admin1', '010', 1);

<주의사항>
1. 회원 가입 창에서 메일 입력 시, 인증 번호 전송 알림이 오는데 시간이 좀 걸린다.
2. 첫 사이트 접속시 로그인 할 때 일시적인 오류 존재. 새로고침 하면 된다.
3. 파일 업로드 시 첫 글자가 한글일 때, 인코딩 오류가 존재한다.
4. 사이트 접속시 https:// 로 접속하면 접속이 불가능하다.