package io.github.mdraihan27.mmh.dining.entities.dining_token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dining-token")
@Getter
@Setter
@AllArgsConstructor
public class DiningTokenEntity {

    @Id
    private String tokenId;

    private String tokenOwnerEmail;

    private long tokenGenerationTIme;

    private long tokenExpirationTime;

    private String mealTime;

    private String diningTokenType;

    private long mealPrice;



}