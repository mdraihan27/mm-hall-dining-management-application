package io.github.mdraihan27.mmh.dining.services.user;

import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.UserRepository;
import io.github.mdraihan27.mmh.dining.utilities.GenerateAndValidateStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private GenerateAndValidateStringUtil generateAndValidateStringUtil;

    public ResponseEntity<UserEntity> findUser(String userinfo, String infoType) throws Exception {

        try{

            Optional<UserEntity> user;

            if(infoType.equals("email")){
                user = userRepository.findByEmail(userinfo);
            }else if(infoType.equals("id")){
                user = userRepository.findById(userinfo);
            }else{
                throw new Exception("User info type is not valid");
            }

            if(user.isPresent()){
                return ResponseEntity.ok(user.get());
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }



    }

    @Transactional
    public UserEntity createUser(UserEntity userEntity, Boolean isVerified) throws Exception{

        try{
            userEntity.setAccountEnabled(true);
            userEntity.setVerified(isVerified);
            userEntity.setUserVerificationEntityId(userVerificationService.createUserVerificationEntity(userEntity.getEmail()).getId());
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(new ArrayList<>(Arrays.asList("USER")));
            userEntity.setUserCreationTime(Instant.now().getEpochSecond());
            userEntity.setUserTokensId(new ArrayList<>());
            userEntity.setBalance(0);
            UserEntity createdUser = userRepository.save(userEntity);

            if(createdUser != null){
                return createdUser;
            }else{
                return null;
            }
        }catch(Exception e){
            log.error("Error creating user", e);
            throw new Exception("Error creating user");
        }

    }



}
