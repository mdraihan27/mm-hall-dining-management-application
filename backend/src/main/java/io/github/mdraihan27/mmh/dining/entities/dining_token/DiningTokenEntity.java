package io.github.mdraihan27.mmh.dining.entities.dining_token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
public class DiningTokenEntity {

    @Id
    private String tokenId;

    @NonNull
    private String tokenOwnerEmail;

    @NonNull
    private long tokenGenerationTIme;

    @NonNull
    private long tokenExpirationTime;

    @NonNull
    private String mealTime;

    @NonNull
    private String tokenType;


}