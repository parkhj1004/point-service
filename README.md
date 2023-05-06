# 요구사항 구현 여부
1. 회원의 잔여 포인트를 조회하는 API <br>
   => TestCase > 잔여포인트조회.http <br>
2. 회원의 포인트 적립/사용 내역을 조회하는 API <br>
    페이징 처리가 되어야 합니다. <br>
    사용 취소된 내역은 조회되지 않아야 합니다. <br>
   => TestCase > 포인트리스트.http <br>
3. 회원의 포인트를 적립하는 API <br>
    적립하는 포인트의 사용가능 기간은 1 년입니다.  <br>
    => TestCase > 포인트적립.http <br>
    => resource > application-local.yml (application-prod.yml) 파일에 point.policy.point-usage-period 설정 값을 가져오도록 함.
    => active profile 없을 경우 기본 값 : local
4. 회원의 포인트를 사용하는 API <br>
    => TestCase > 포인트사용.http <br>
    => response 에 pointIds 값을 5번 사용취소 처리 시 필요 함. <br>
5. 회원의 포인트 사용을 취소하는 API <br>
    포인트 사용 api 호출하는 쪽에서 rollback 처리를 위한 용도입니다. <br>
    => TestCase > 포인트취소.http <br>
    => 주문 실패로 인하여 사용 포인트 취소 목적임으로 주문번호, 사용처리 시 전달한 pointIds 를 전달해야 함. <br>
     잘못 전달 시 취소 처리 실패 함. <br>
6. 적립,사용,취소 요청 시 포인트를 마이너스로 호출 할 경우 오류 <br>

# 구현 방법
1. java, gradle, spring boot, jpa, queryDsl, h2 사용하여 처리 함.
2. DB 는 h2 메모리 디비를 사용 하였음으로 구동 시 매번 데이타가 초기화 됩니다. 
3. mockito 를 이용하여 test case 작성하여 기본 로직 확인
4. TestCase 폴더 하위에 단위 테스트 케이스 작성
5. 상세 구현 내용
   1. point , pointLog 2개의 테이블 존재 함.
   2. point : pointLog 는 1:N 구조
   3. 적립, 사용 시 point 테이블 각각 저장, pointLog 테이블에 사용 정보 저장
   4. 사용 시 pointLog 에는 사용한 포인트 정보를 나눠서 저장된다.
   즉) 5000원 사용 시 , 포인트 1번에서 1000원, 포인트 2번에서 4000원
   또한. 유효기간이 적게 남은 순으로 포인트를 사용 한다. 
   5. 취소 할 경우 point, pointLog 테이블에 업데이트 처리하여 유효기간을 그대로 사용한다.
   또한, 취소 처리 시 포인트 사용시 전달 한 포인트 아이디를 다시 전달해야 한다. 
   잘못 전달 될 경우 취소처리 실패 한다.
   6. yml 을 local, prod 로 구성하여 나누었음. 실제로 운영 시에는 각 yml 설정을 다르게 하나
   해당 프로젝트에는 같은 값으로 설정 함.

# 검증
1. *.http 실행 시 *.json 파일에서 데이타 확인 가능 함. 
2. http://localhost:8080/ swagger 에서도 확인 및 테스트 가능 함.