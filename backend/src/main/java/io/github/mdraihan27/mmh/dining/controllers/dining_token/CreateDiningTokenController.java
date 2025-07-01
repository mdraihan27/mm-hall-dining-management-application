package io.github.mdraihan27.mmh.dining.controllers.dining_token;

import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.services.dining_token.CreateAndValidateDiningTokenService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/dining-token")
@Slf4j
public class CreateDiningTokenController {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private CreateAndValidateDiningTokenService createAndValidateDiningTokenService;

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @PostMapping("create")
    public ResponseEntity createDiningToken(@RequestBody DiningTokenEntity diningToken) {
        try{
            if(diningToken.getDiningTokenType().isEmpty() || diningToken.getDiningTokenType()==null){
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Token type is empty"));
            }else if(diningToken.getMealTime().isEmpty() || diningToken.getMealTime()==null){
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Meal time is empty"));
            }

            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            return createAndValidateDiningTokenService.createNewToken(authenticatedUser, diningToken);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while creating dining token"));
        }

    }
}
