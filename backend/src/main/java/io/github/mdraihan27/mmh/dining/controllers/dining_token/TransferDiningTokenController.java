package io.github.mdraihan27.mmh.dining.controllers.dining_token;

import io.github.mdraihan27.mmh.dining.services.dining_token.TransferDiningTokenService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/dining-token")
@Slf4j
public class TransferDiningTokenController {
    @Autowired
    private TransferDiningTokenService transferDiningTokenService;

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @PutMapping("transfer")
    public ResponseEntity transferDiningToken(@RequestBody Map<String, Object> requestBody) {
        try{
            String tokenId = (String) requestBody.get("tokenId");
            String newOwnerEmail = (String) requestBody.get("newOwnerEmail");
            if(tokenId == null || tokenId.isEmpty()){
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Token id is empty"));
            }

            if(newOwnerEmail == null || newOwnerEmail.isEmpty()){
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "New owner email is empty"));
            }

            return transferDiningTokenService.transferDiningToken(tokenId, newOwnerEmail, getAuthenticatedUserUtil.getAuthenticatedUser());


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while transferring dining token"));
        }

    }
}
