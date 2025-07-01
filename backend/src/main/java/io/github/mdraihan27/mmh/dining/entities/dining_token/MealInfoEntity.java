package io.github.mdraihan27.mmh.dining.entities.dining_token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "meal-info")
@Getter
@Setter
@AllArgsConstructor
public class MealInfoEntity {

    @Id
    private String mealInfoId;

    private String lunchMealType;

    private String dinnerMealType;

    private long dinnerMealPrice;

    private long lunchMealPrice;

}