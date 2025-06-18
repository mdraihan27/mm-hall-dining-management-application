package io.github.mdraihan27.mmh.dining.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
public class UserEntity {

    @Id
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String name;

    private boolean isVerified;
    private boolean isAccountEnabled;
    private long userCreationTime;

    private String userVerificationEntityId;

    private String userSettingsEntityId;

    private ArrayList<String> userTokensId;

    private ArrayList<String> roles;


}