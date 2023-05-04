package org.point.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.point.meta.Code.E_1000;
import static org.point.meta.Code.F_1000;
import static org.point.meta.Code.F_2000;
import static org.point.meta.Code.F_2100;
import static org.point.meta.Code.F_3100;
import static org.point.meta.Code.S_1000;
import static org.point.meta.Code.S_2000;

@RequiredArgsConstructor
public enum ResultCode {
    SUCCESS_SAVED_POINT(S_1000, "포인트가 적립 되었습니다."),
    FAIL_SAVED_POINT(F_1000, "포인트 적립에 실패 하였습니다."),
    SUCCESS_USED_POINT(S_2000, "포인트 사용처리 되었습니다."),
    FAIL_USED_POINT(F_2000, "포인트 사용처리 실패 하였습니다."),
    LACK_OF_POINT(F_2100,"포인트가 부족합니다."),
    CANCEL_NON_VALID(F_3100, "취소 처리에 필요한 데이타가 부족 합니다."),
    SERVER_ERROR(E_1000,"서버오류")
    ;

    @Getter
    private final Code code;
    @Getter
    private final String message;
}
