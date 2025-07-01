package com.verdict.verdict.global.user;


import com.verdict.verdict.global.user.dto.SignupRequest;
import com.verdict.verdict.global.user.facade.UserFacade;
import com.verdict.verdict.global.dto.SignupResponse;
import com.verdict.verdict.global.util.response.CommonResponse;
import com.verdict.verdict.global.util.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.verdict.verdict.global.util.SuccessCode.CREATED;
import static com.verdict.verdict.global.util.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserFacade userFacade;

    @GetMapping("/auth/check-nickname")
    public ResponseEntity<CommonResponse> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        userFacade.isNicknameDuplicate(nickname);
        return ResponseEntity.ok().body(
                new CommonResponse(SUCCESS.getStatus(), SUCCESS.getMessage())
        );
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<SingleResponse<SignupResponse>> signup(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = userFacade.signup(signupRequest);

        return ResponseEntity.ok().body(
                new SingleResponse<>(CREATED.getStatus(), CREATED.getMessage(), signupResponse)
        );
    }
}