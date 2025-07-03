package com.verdict.verdict.global.exception;

import com.verdict.verdict.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BusinessExceptionHandler implements MessageSender {
    private final Environment env;
//    private final SlackService slackService;

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonResponse> globalBusinessExceptionHandler(BusinessException e) {
        log.error("[ERROR]" + e.getMessage(), e);
        sendMessage(e);

        return ResponseEntity.badRequest().body(
                new CommonResponse(e.getStatus(), e.getMessage())
        );
    }

    @Override
    public void sendMessage(Exception exception) {
//        exception.printStackTrace();
        log.error("[ERROR]" + exception.getMessage(), exception);
    }

}