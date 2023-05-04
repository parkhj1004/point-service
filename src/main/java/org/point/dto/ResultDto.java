package org.point.dto;

import lombok.Getter;
import lombok.Setter;
import org.point.meta.Code;
import org.point.meta.ResultCode;

import java.util.List;

@Getter
@Setter
public class ResultDto {
    private ResultCode resultCode;
    private String resultMessage;
    private Code errorCode;
    private Long orderId;
    private List<Long> pointIds;

    public static ResultDto of(ResultCode resultCode, Long orderId, List<Long> pointIds) {
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(resultCode);
        resultDto.setResultMessage(resultCode.getMessage());
        resultDto.setErrorCode(resultCode.getCode());
        resultDto.setOrderId(orderId);
        resultDto.setPointIds(pointIds);

        return resultDto;
    }
}
