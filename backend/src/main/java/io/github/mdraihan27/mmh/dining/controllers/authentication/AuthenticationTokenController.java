package io.github.mdraihan27.mmh.dining.controllers.authentication;

import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationTokenController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @GetMapping("refresh")
    public ResponseEntity<Map> refreshToken(@RequestParam String refreshToken) {

        try{

            if(jwtUtil.validateToken(refreshToken, true)){
                String email = jwtUtil.extractEmail(refreshToken, true);
                String newJwt = jwtUtil.generateToken(email, false);

                return ResponseEntity.ok(createResponseUtil.createResponseBody(true, "New JWT generated", "jwt", newJwt));

            }else{

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponseUtil.createResponseBody(false, "Refresh token is invalid or expired"));
            }
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while refreshing JWT"));
        }
    }

    @PostMapping("token-verification")
    public ResponseEntity<Map> verifyToken(@RequestBody Map<String, Object> requestBody) {

        try{
            String token = (String) requestBody.get("token");


            if(jwtUtil.validateToken(token, false)){

                return ResponseEntity.ok(createResponseUtil.createResponseBody(true, "This a valid JWT"));

            }else if(jwtUtil.validateToken(token, true)){

                return ResponseEntity.ok(createResponseUtil.createResponseBody(true, "This a valid Refresh Token"));

            }else{

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponseUtil.createResponseBody(false, "This token is invalid or expired"));
            }
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while verifying the token"));
        }
    }
}
