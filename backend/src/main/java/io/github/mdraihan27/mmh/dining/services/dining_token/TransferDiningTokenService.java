package io.github.mdraihan27.mmh.dining.services.dining_token;

import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningTokenRepository;
import io.github.mdraihan27.mmh.dining.repositories.UserRepository;
import io.github.mdraihan27.mmh.dining.services.user.UserService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransferDiningTokenService {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DiningTokenRepository diningTokenRepository;

    public ResponseEntity transferDiningToken(String tokenId, String newOwnerEmail, UserEntity oldOwner) throws Exception {
       try{
           UserEntity newOwner = userService.findUser(newOwnerEmail, "email").getBody();
           if(newOwner == null) {
               return ResponseEntity
                       .badRequest()
                       .body(createResponseUtil.createResponseBody(false, "New owner email is invalid"));
           }

           Optional<DiningTokenEntity> diningToken = diningTokenRepository.findById(tokenId);

           if(!diningToken.isPresent()) {
               return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "This token does not exist"));
           }

           if(!oldOwner.getUserTokensId().contains(tokenId)) {
               return ResponseEntity
                       .badRequest()
                       .body(createResponseUtil.createResponseBody(false, "This user does not own this token"));
           }

           oldOwner.getUserTokensId().remove(tokenId);
           userRepository.save(oldOwner);

           newOwner.getUserTokensId().add(tokenId);
           userRepository.save(newOwner);

           return ResponseEntity
                   .ok(createResponseUtil.createResponseBody(true, "Token successfully transferred"));
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
