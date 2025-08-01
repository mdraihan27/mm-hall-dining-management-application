package io.github.mdraihan27.mmh.dining.utilities;

import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CreateResponseUtil {

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    public Map createResponseBody(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return response;
    }

//    public Map createResponseBody(boolean success, String message, String email){
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        response.put("message", message);
//        response.put("email", email);
//        return response;
//    }

//    public Map createResponseBody(boolean success, String message, String error){
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        response.put("message", message);
//        response.put("error", error);
//        return response;
//    }

//    public Map createResponseBody(boolean success, String message, Map data){
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        response.put("message", message);
//        response.put("data", data);
//        return response;
//    }

//    public Map createResponseBody(boolean success, String message , Map userinfo){
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        response.put("message", message);
//        response.put("data", data);
//        return response;
//    }

//    public Map createResponseBody(boolean success, String message, String email, Map data){
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        response.put("message", message);
//        response.put("email", email);
//        response.put("data", data);
//        return response;
//    }

    public Map createResponseBody(boolean success, String message, String dataName, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put(dataName, data);
        return response;
    }

    public Map createResponseBody(boolean success, String message, String dataName1, Object data1, String dataName2, Object data2, String dataName3, Object data3) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put(dataName1, data1);
        response.put(dataName2, data2);
        response.put(dataName3, data3);
        return response;
    }

    public Map createMap(String dataName, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put(dataName, data);
        return response;
    }

    public Map createMap(String dataName, ArrayList data) {
        Map<String, Object> response = new HashMap<>();
        response.put(dataName, data);
        return response;
    }

//    public Map createMap(String dataName1, Object data1, String dataName2, Object data2){
//        Map<String, Object> response = new HashMap<>();
//        response.put(dataName1, data1);
//        response.put(dataName2, data2);
//        return response;
//    }

    public Map createUserInfoMap(UserEntity user) {
        try {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("email", user.getEmail());
            userMap.put("name", user.getName());
            userMap.put("isVerified", user.isVerified());
            userMap.put("isAccountEnabled", user.isAccountEnabled());
            userMap.put("balance", user.getBalance());
            userMap.put("roles", user.getRoles());
            return userMap;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> createDiningTokenInfoMap(DiningTokenEntity token) {
        try {
            Map<String, Object> diningTokenMap = new HashMap<>();
            diningTokenMap.put("tokenId", token.getTokenId());
            diningTokenMap.put("tokenOwnerEmail", token.getTokenOwnerEmail());
            diningTokenMap.put("tokenExpirationTime", token.getTokenExpirationTime());
            diningTokenMap.put("tokenType", token.getDiningTokenType());
            diningTokenMap.put("mealTime", token.getMealTime());

            return diningTokenMap;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> createTokenListMap(List<DiningTokenEntity> tokenList) {
        try {

            Map<String, Object> tokenListMap = new HashMap<>();

            for (DiningTokenEntity token : tokenList) {
                Map<String, Object> diningTokenMap = new HashMap<>();
                diningTokenMap.put("tokenId", token.getTokenId());
                diningTokenMap.put("tokenExpirationTime", token.getTokenExpirationTime());
                diningTokenMap.put("tokenType", token.getDiningTokenType());
                diningTokenMap.put("mealTime", token.getMealTime());

                tokenListMap.put(token.getTokenId(), diningTokenMap);
            }



            return tokenListMap;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


}
