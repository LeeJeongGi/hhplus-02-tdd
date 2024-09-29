# 특강 신청 서비스

## 명세

### Description
- `특강 신청 서비스`를 구현해 봅니다.
- 항해 플러스 토요일 특강을 신청할 수 있는 서비스를 개발합니다.
- 특강 신청 및 신청자 목록 관리를 RDBMS를 이용해 관리할 방법을 고민합니다.

### Requirements
- 아래 2가지 API 를 구현합니다.
    - 특강 신청 API
    - 특강 신청 여부 조회 API
- 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려 하여 구현합니다.

### API Specs
1️⃣ **(핵심)** 특강 신청 **API**

- 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
- 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있습니다.
- 특강은 선착순 30명만 신청 가능합니다.
- 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다.

2️⃣ 특강 선택 API

- 날짜별로 현재 신청 가능한 특강 목록을 조회하는 API 를 작성합니다.
- 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기전 목록을 조회해볼 수 있어야 합니다.

3️⃣ 특강 신청 완료 목록 조회 API

- 특정 userId 로 신청 완료된 특강 목록을 조회하는 API 를 작성합니다.
- 각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 합니다.

---
## 기능 명세: 특강 신청 서비스

1. 유저(User) 도메인 관련 기능
   2. 유저 생성
      - [ ] 사용자가 신규 유저로 등록될 수 있다. 각 유저는 고유한 userId를 가진다.
   3. 유저 조회
      - [ ] 유저의 정보를 userId를 통해 조회할 수 있다. 유저가 신청한 특강 목록을 조회할 수 있다.

2. 특강(Lecture) 도메인 관련 기능
   3. 특강 생성
      - [ ] 특강은 강연자, 강의명, 정원(30명) 등의 정보를 포함한다. 각 특강은 고유한 lectureId를 가진다.
      - [ ] 강의명과 강연자 이름은 필수 입력 값이다.
      - [ ] 동일한 이름의 특강을 중복으로 생성할 수 없다.
   4. 특강 조회
      - [ ] 날짜별로 신청 가능한 특강 목록을 조회할 수 있다. 각 특강의 신청 가능 여부(정원 초과 여부)를 확인할 수 있다.
      - [ ] 각 특강의 신청 가능 여부(정원 초과 여부)를 확인할 수 있다.
      - [ ] 존재하지 않는 날짜에 대한 특강 목록 조회 시도는 빈 목록이 반환된다.

3. 특강 신청(Lecture Registration) 기능
   4. 특강 신청
      - [ ] 유저는 userId를 사용해 특정 lectureId의 특강을 신청할 수 있다.
      - [ ] 동일한 유저는 동일 특강에 대해 한 번만 신청할 수 있다.
      - [ ] 신청 인원이 30명을 초과하면 신청이 실패한다.
      - [ ] 존재하지 않는 특강에 대한 신청 시도는 실패한다.
      - [ ] 존재하지 않는 유저가 특강을 신청하려 하면 신청이 실패한다.
   4. 신청자 목록 조회
      - [ ] 특정 특강에 대한 신청자 목록을 조회할 수 있다.
      - [ ] 신청자 목록은 userId, 신청 시간 등의 정보를 포함한다.
      - [ ] 신청자 목록이 없는 특강에 대해 조회할 경우 빈 목록이 반환된다.
      - [ ] 존재하지 않는 특강에 대한 신청자 목록 조회 시도는 실패한다. 
