package com.example.demo.dto;

import com.example.demo.enums.ExceptionEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Data
@Schema(implementation = ResponseDto.class)
public class ResponseDto<T> {

    @Schema(description = "성공 여부",example = "true",requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean success;
    @Schema(description = "응답 코드",example = "200",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    @Schema(description = "응답 메세지",example = "string",requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    @Schema(description = "응답 바디",example = "object",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Nullable
    private T result;

    @Builder(builderMethodName = "successBuilder", builderClassName = "successBuilder")
    public ResponseDto(String message, T result) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.message = message;
        this.result = result;
    }

    @Builder(builderMethodName = "failBuilder", builderClassName = "failBuilder")
    public ResponseDto(Integer code, HttpStatus httpStatus, String message, T result) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Builder(builderMethodName = "failApiBuilder", builderClassName = "failApiBuilder")
    public ResponseDto(ExceptionEnum e) {
        this.success = false;
        this.code = e.getErrorCode();
        this.message = e.getMessage();
        this.result = null;
    }
}
