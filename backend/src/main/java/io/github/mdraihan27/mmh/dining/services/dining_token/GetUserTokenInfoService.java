package io.github.mdraihan27.mmh.dining.services.dining_token;

import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningTokenRepository;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class GetUserTokenInfoService {

    @Autowired
    private DiningTokenRepository diningTokenRepository;
    @Autowired
    private CreateResponseUtil createResponseUtil;

    public ResponseEntity getUserTokenList(UserEntity user) throws Exception {
        try{
            ArrayList<String> userTokenList = user.getUserTokensId();
            ArrayList<Map> userTokenMapList = new ArrayList<>();
            for(String diningTokenId : userTokenList){
               DiningTokenEntity diningToken = diningTokenRepository.findById(diningTokenId).orElse(null);
               if(diningToken != null){
                   userTokenMapList.add(createResponseUtil.createDiningTokenInfoMap(diningToken));
               }

            }

            return ResponseEntity.ok(createResponseUtil
                    .createResponseBody(true, "Token List found", "tokenList", userTokenMapList));
        }catch(Exception e){
            throw new Exception(e);
        }
    }
}
