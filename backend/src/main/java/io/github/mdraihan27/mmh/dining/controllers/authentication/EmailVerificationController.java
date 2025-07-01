package io.github.mdraihan27.mmh.dining.controllers.authentication;

import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.services.user.UserVerificationService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class EmailVerificationController {

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Transactional
    @PostMapping("email-verification/code")
    public ResponseEntity sendEmailVerificationCode() {
        try{
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();
            System.out.println("Mail sent to: " + authenticatedUser.getEmail());

            if(authenticatedUser.isVerified()){

                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(createResponseUtil.createResponseBody(false, "This user is already verified"));
            }
            return userVerificationService.sendVerificationCodeEmail(authenticatedUser,
                    "Your email verification code",
                    "Use this code to verify your email in MM Hall Dining Management Application",
                    "Email verification code sent");


        }catch(Exception e){

            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while sending verification code"));
        }
    }

    @Transactional
    @PostMapping("email-verification/verify")
    public ResponseEntity verifyEmailVerificationCode(@RequestBody Map<String, Object> requestBody) {

        try{
            String verificationCode = (String) requestBody.get("verificationCode");
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            return userVerificationService.verifyVerificationCode(authenticatedUser, verificationCode, false, "Email verification code verified. User is now verified");


        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while verifying verification code"));

        }
    }
}
