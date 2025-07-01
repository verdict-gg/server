package com.verdict.verdict.global.controller;

import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.user.dto.LoginRequest;
import com.verdict.verdict.global.user.facade.UserFacade;
import com.verdict.verdict.global.dto.AuthResponse;
import com.verdict.verdict.global.dto.GuestResponse;
import com.verdict.verdict.global.dto.TokenRequest;
import com.verdict.verdict.global.service.JwtFacade;
import com.verdict.verdict.global.util.response.CommonResponse;
import com.verdict.verdict.global.util.response.SingleResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.verdict.verdict.global.util.SuccessCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final UserFacade userFacade;
    private final JwtFacade jwtFacade;

    @PostMapping("/auth")
    public ResponseEntity<SingleResponse<AuthResponse>> generateToken(HttpServletResponse response,
                                                                      @RequestBody TokenRequest tokenRequest) {

        User authUser = userFacade.getAuthUser(tokenRequest.identifier());

        jwtFacade.generateAccessToken(response, authUser);
        jwtFacade.generateRefreshToken(response, authUser);
        jwtFacade.setReissuedHeader(response);

        AuthResponse authResponse = userFacade.getUserAuthInfo(authUser.getIdentifier());

        return ResponseEntity.ok().body(
                new SingleResponse<>(SUCCESS.getStatus(), SUCCESS.getMessage(), authResponse)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout( User user, HttpServletResponse response) {
        jwtFacade.logout(response, user.getIdentifier());

        return ResponseEntity.ok().body(
                new CommonResponse(SUCCESS.getStatus(), SUCCESS.getMessage())
        );
    }

    @GetMapping("/auth/health-check")
    public String healthCheck() {
        return "health-check-ok";
    }

    @PostMapping("/auth/guest")
    public ResponseEntity<SingleResponse<GuestResponse>> loginWithGuest(HttpServletResponse response,
                                                                        @RequestBody LoginRequest loginRequest) {
        User authUser = userFacade.getGuestUser(loginRequest);

        jwtFacade.generateAccessToken(response, authUser);
        jwtFacade.generateRefreshToken(response, authUser);
        jwtFacade.setReissuedHeader(response);

        AuthResponse authResponse = userFacade.getUserAuthInfo(authUser.getIdentifier());
        GuestResponse guestResponse = GuestResponse.from(authResponse, authUser.getIdentifier());

        return ResponseEntity.ok().body(
                new SingleResponse<>(SUCCESS.getStatus(), SUCCESS.getMessage(), guestResponse)
        );
    }
}