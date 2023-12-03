project GitHub url : https://github.com/IENFI/software.git

1. 프로젝트 다운 받아서 intellij로 build gradle 눌러서 project open
2. java 21 다운로드 하기
3. settings>gradle (검색)
- Build and run using > intellij IDEA
- Run tests using > intellij IDEA
- gradle jvm > java 21
	로 전부 변경하기
4. project structure>sdk>java 21로 바꾸기
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
- 관리자1 생성하는 예시
use dms;
INSERT INTO member (member_dept, member_email, member_id, member_interest, member_name, member_password, member_phone_number, member_role)
VALUES ('관리자1', 'admin1@admin1', 'admin1', '관리자1', '관리자1', 'admin1', '010', 1);