## Movie Reservation Backend Page

Effective service to find and reserve movies. Runs on JAVA 17 and implements MariaDB, JPA and SpringBoot 3.2. 

(1) SignIn, Login, Logout
Using JWT, user can sign in, login and logout of the service. When the user logs out, the user’s JWT is blacklisted. 

(2) Main Page
In main page, the user can find movies that are shown on cinema with pageable. The user can retrieve information about the movie poster, movie title, ticket sales, release date, scores given by user and the d-day of the movie. The ticket sales are calculated with the number of reservations made on the movie out of total seats. Scores are made from the average score of users who made reviews of this movie with a score ranging from 1 to 10. The d-day is retrieved by the date of today until the release date. If the movie is already released, the d-day will be set to 0. 

(3) Reservation Page
In reservation Page, the user can find possible reservations with the movie title, cinema name, date and time that the user wishes to make the reservation. The user can make reservations with requested movie title, cinema name, cinema type, date and time. Reservation updates can be made for movie times only. If the movie time or reservation time is in the past, the user will encounter an exception. Deleting a reservation that was made by the user is also possible. 

(4) My Page
In my page, the user can find information about the reservations that the user made and the information about the user himself. The user can also update his phone number or password. 
There is an additional service for registering reviews. The user can make reviews with a score ranging from 1 to 10 and a short one-sentence comment. After, the user can find all the reviews that the user registered, along with updating and deleting his reviews. 

## ERD
The ERD is efficiently organised to retrieve the data fast and effectively to provide for the facilitation of the service. The user table holds information about the user and is connected to the user_role table with the role table. Then the user table is also connected to the review table and reservation table, with a one to many relationship. 
The movie table is more complex. First, it is connected to the actor table with a connection to movie_actor table, as one movie can have several actors and an actor can have several movies as well. Then, the movie table is connected to the schedule table, which is also connected to the reservation table. Thus, the reservation table connects between the user table and the schedule table. 
Now, the schedule table connects the movie table and the cinema_type table. In the cinema type table, cinema type data such as first floor 2D, 3D are registered. Then, the cinema type table is connected to the cinema table, where data such as cinema name, address and phone_number is registered. 
This formation of entity makes retrieving and saving data possible in every way. The user can make reservations with movie title, cinema name, date and time and the information will be saved on the tables accordingly. Also, it makes sure data is not duplicated thus effectively saving up memory space and each table has only information about itself. 


### MainPage 기획

#### MainPage 
- 일반적인 영화관 사이트처럼 사이트에 접속하면 볼 수 있는 영화 정보들을 받아서 불러올 수 있다.
- 영화 정보에는 영화 제목, 예매율, 평점 평균, 영화 포스터이미지, 개봉일 등을 확인할 수 있다.
-영화 정보는 예매율순과 평점 평균 순으로 나열해서 확인 가능하다.
-페이지네이션 구현으로 영화 목록을 원하는 개수에 맞추어 나타낼 수 있다.

#### DetailPage
-기본적인 영화 정보와 함께 배우명, 감독명, 장르, 줄거리 등 영화에 대해 좀 더 세부적인 내용을 받아와 나타낼 수 있다.

### MainPage API

#### MainPage

```
  GET /api/find
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `titleKorean` | `String` |  영화 제목(한국어) |
| `titleEnglish` | `String` |  영화 제목(영어) |
| `ticketSales`      | `Double` | 예매율 
| `releaseDate` | `LocalDateTime` |  개봉일 |
| `dDay` | `Integer` |  디데이 |
| `poster` | `String` |  영화 포스터 |
| `scoreAvg` | `Double` |  평점 평균 |

#### DetailPage

```
  GET /api/check/{movieId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `titleKorean` | `String` |  영화 제목(한국어) |
| `titleEnglish` | `String` |  영화 제목(영어) |
| `ticketSales`      | `Double` | 예매율 
| `releaseDate` | `LocalDateTime` |  개봉일 |
| `scoreAvg` | `Double` |  평점 평균 |
| `dDay` | `Integer` |  디데이 |
| `poster` | `String` |  영화 포스터 |
| `ageLimit` | `Integer` |  연령 제한 |
| `screenTime` | `Integer` |  상영 시간 |
| `country` | `String` |  개봉 국가 |
| `director` | `String` |  감독명 |
| `genre` | `String` |  장르 |
| `status` | `String` |  상영 상태 |
| `summary` | `String` |  줄거리 |
| `actorName` | `String` |  배우 이름 |
| `birthDay` | `LocalDateTime` |  배우 생년월일 |
| `nationality` | `String` |  배우 국적 |
| `job` | `String` |  직업 |
| `height` | `Integer` |  배우 신장 |


### Reservation 기획

#### 영화 리스트 조회
- 예매 페이지 프론트단에서 영화 이름, 영화 포스터, 영화관 이름, 상영관, 예매 날짜, 예매 시간을 
선택할 수 있게 영화스케줄 리스트를 넘겨주는 api입니다.
- 로그인 하지 않아도 예매화면에서 리스트를 볼 수 있습니다.

#### 예매
- 영화 이름,영화관 이름, 상영관, 예매 날짜, 예매 시간을 받아 해당하는 스케줄을 예매하는 api
- 로그인이 되지 않은 유저면 예매하지 못하게 진행
- 예매하려는 영화가 상영종료된 영화라면 예매 못하게 진행
- 가입한 유저의 나이가 영화 시청연령에 해당하지 않으면 예매 못하게 진행
- 예매하려는 날짜가 당일 기준으로 지난 날짜이면 예매 못하게 진행
- (예매완료된날짜)-(예매완료된날짜)-(상영관Id+영화관Id)-(Random을 이용한 Random번호)를 조합하여 예매 번호 생성
  ex.2024-0501-0004-9876
- 예매하려는 스케줄의 좌석이 없을경우 예매 못하게 진행
- 영화 이름,영화관 이름, 상영관, 예매 날짜,예매 시간에 해당하는 스케줄이 있다면 스케줄 번호와 예매 완료된 날짜, 예매 번호, 유저 아이디를 DB에 저장하여 예매 진행
- 예매 성공하면 해당 스케줄의 좌석 1개 제거하도록 진행

#### 예매 변경
- 다른 예매 시간을 받아 해당하는 스케줄로 예매 변경하는 api
- 예매 번호로 티켓을 조회했을 때 해당 ticket이 당일 기준으로 지난 날짜이면 예매 변경 못하게 진행
- 예매 변경은 영화관 상영관은 변경 못하며 날짜만 바꾸게 진행
- 변경하려는 시간이 동일하면 예매 변경 못하게 진행
- 예매 변경이 성공하면 예약 아이디는 그대로 가며 예약완료된 시간과 변경된 스케줄 번호로 다시 저장하도록 진행
- 변경하기 전 기존 스케줄의 좌석은 1개 더해주고 변경된 스케줄의 좌석은 1개 제거하도록 진행

#### 예매 취소
- 예약했던 예매내역을 삭제하는 api입니다.
- 로그인이 되지 않은 유저면 취소하지 못합니다.
- 예약 번호로 예약 내역을 조회할 수 있습니다.
- 취소할 예약 내역의 상영 날짜, 시간과 현재 날짜, 시간을 타임스탬프로 변경 후 비교한 값이 10분 전이면
취소가 불가능합니다.
- 취소할 예약 내역이 10분 전이 아니라면 예약 취소가 되고, 좌석이 +1 됩니다.

### Reservation API

#### Reservation

```
  POST /reservation/add
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `customUserDetails`      | `@AuthenticationPrincipal CustomUserDetails` | **Required**. 유저토큰 |
| `movieName`      | `String` | **Required**. 영화이름 |
| `cinemaName`      | `String` | **Required**. 영화관 이름 |
| `cinemaType`      | `String` | **Required**. 상영관 이름 |
| `reserveDate`      | `LocalDate` | **Required**. 예약 날짜 |
| `reserveTime`      | `LocalTime` | **Required**. 예약 시간 |



#### Reservation change

```
  PUT /reservation/update?reservation-id=?

```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `customUserDetails`      | `@AuthenticationPrincipal CustomUserDetails` | **Required**. 유저토큰 |
| `reservationId` | `@RequestParam Integer` | **Required**. 예약 번호 |
| `changeTime` | `LocalTime` | **Required**. 변경할 시간 |

### Mypage 기획

(1) 예매 내역 조회
- 예매내역 조회: 토큰을 받아 예약 아이디,예약번호,한국어제목,영어제목,극장이름,예약시간 조회
- 아이디로 예약을 찾아서 반환
-> 아이디 조회 실패 시 : "아이디를 찾을 수 없습니다."
-> 예매 내역이 없을 시 :"예매 정보를 찾을 수 없습니다."
- 페이징 처리한 결과물 반환

(2) 회원정보 조회 및 수정
- 회원정보 조회 : 토큰을 받아 이름,아이디,생년월일,전화번호 조회하는 API
- 회원정보 수정: 비밀번호와 전화번호 변경 가능
- 회원정보 변경 API에서 비밀번호와 전화번호가 하나라도 null값인 경우 예외 던지기

(3) 리뷰 작성/조회/변경/삭제
- 토큰을 받아 예매내역을 확인하고 리뷰를 작성/조회/변경/삭제하는 API
- 로그인이 되지 않은 상태에서 리뷰 작성 불가능
- 한 영화ID당 하나의 리뷰 작성 가능
- 최근 작성 또는 수정 된 리뷰로 업데이트
- 1~10까지의 평점 제출 가능
- 작성 시 영화ID, 평점, 내용, 작성날짜를 DB에 저장

### ERD
리뷰 테이블은 사용자가 영화에 대한 리뷰를 작성하고 저장하는 데 사용됩니다.
각 리뷰는 특정 사용자에 의해 작성되었으며, 특정 영화를 대상으로 합니다.
그러기 위해 user 테이블과 연결하여 사용자 정보 조회하고, movie 테이블과
연결하여 영화 정보를 조회하고 있습니다.
리뷰 테이블은 사용자가 부여한 영화에 대한 점수(score)와 함께 내용(content)
을 저장합니다. 이를 통해 사용자들은 자신의 경험을 토대로 영화에 대한 점수를
매기고 리뷰를 작성할 수 있습니다. 또한 리뷰가 작성된 일시(created_at)를
기록하여 언제 작성되었는지 추적할 수 있습니다.
