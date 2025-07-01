package io.github.mdraihan27.mmh.dining.controllers.dining_token;

import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.services.dining_token.GetUserTokenInfoService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/user/dining-token")
public class GetUserTokenInfoController {

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private GetUserTokenInfoService getUserTokenInfoService;

    @GetMapping("token-list")
    public ResponseEntity getUserTokenList() {
        try{
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();
            return getUserTokenInfoService.getUserTokenList(authenticatedUser);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while getting the user token list."));
        }
    }
}
