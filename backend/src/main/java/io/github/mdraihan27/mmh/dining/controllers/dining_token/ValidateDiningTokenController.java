package io.github.mdraihan27.mmh.dining.controllers.dining_token;

import io.github.mdraihan27.mmh.dining.services.dining_token.CreateAndValidateDiningTokenService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dining-token")
@Slf4j
public class ValidateDiningTokenController {
    @Autowired
    private CreateAndValidateDiningTokenService createAndValidateDiningTokenService;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @GetMapping
    public ResponseEntity getDiningToken(@RequestParam String tokenId) {
        try{
            if(tokenId.isEmpty() || tokenId==null ){
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Token id is empty"));
            }

            return createAndValidateDiningTokenService.validateAndGetDiningToken(tokenId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error creating dining token"));

        }
    }
}
