package io.github.mdraihan27.mmh.dining.controllers.user;

import io.github.mdraihan27.mmh.dining.entities.user.UserVerificationEntity;
import io.github.mdraihan27.mmh.dining.repositories.UserVerificationRepository;
import io.github.mdraihan27.mmh.dining.services.user.ForgotPasswordService;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.UserRepository;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @GetMapping("user")
    public ResponseEntity getLoggedInUserInfo() {
        try {
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(createResponseUtil.createResponseBody(true, "User found", "userInfo", createResponseUtil.createUserInfoMap(authenticatedUser)));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while fetching user"));
        }
    }

    @Transactional
    @DeleteMapping("user/delete")
    public ResponseEntity deleteLoggedInUser() {
        try {
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            String userVerificationEntityId = authenticatedUser.getUserVerificationEntityId();
            String email =authenticatedUser.getEmail();

            userVerificationRepository.deleteById(userVerificationEntityId);
            userRepository.deleteById(authenticatedUser.getEmail());
            UserEntity userByEmail = userRepository.findByEmail(email).orElse(null);

            UserVerificationEntity userVerificationEntity = userVerificationRepository.findById(userVerificationEntityId).orElse(null);

            if (userByEmail == null && userVerificationEntity == null) {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(createResponseUtil.createResponseBody(true, "User is successfully deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createResponseUtil.createResponseBody(false, "User deletion has concluded partial or unsuccessful"));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while deleting user"));
        }
    }

    @PutMapping("user/password-reset")
    public ResponseEntity resetPasswordWithPreviousPassword(@RequestBody Map<String, Object> requestBody) {
        try {
            String oldPassword = (String) requestBody.get("oldPassword");
            String newPassword = (String) requestBody.get("newPassword");

            return forgotPasswordService.resetPasswordWithPreviousPassword(oldPassword, newPassword);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while resetting password"));
        }
    }


}
