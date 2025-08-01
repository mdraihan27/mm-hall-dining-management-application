package io.github.mdraihan27.mmh.dining.controllers.user;

import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.UserRepository;
import io.github.mdraihan27.mmh.dining.services.user.UserService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/balance")
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateResponseUtil  createResponseUtil;

    @Autowired
    private GetAuthenticatedUserUtil  getAuthenticatedUserUtil;

    @PostMapping("topup")
    public ResponseEntity topupUsersBalance(@RequestParam String amount) {
        try{
            if(amount == null || amount.isEmpty()){
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Amount cannot be empty"));
            }else if(amount.equalsIgnoreCase("0")){
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Amount cannot be zero"));
            }

            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            if(authenticatedUser == null){
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "User not found"));
            }

            authenticatedUser.setBalance(authenticatedUser.getBalance()+Long.parseLong(amount));
            userRepository.save(authenticatedUser);
            return ResponseEntity.ok(createResponseUtil.createResponseBody(true, "Balance updated"));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(createResponseUtil.createResponseBody(false, "Something went wrong"));
        }
    }
}
