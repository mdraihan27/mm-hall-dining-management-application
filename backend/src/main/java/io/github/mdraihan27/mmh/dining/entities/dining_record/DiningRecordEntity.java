package io.github.mdraihan27.mmh.dining.entities.dining_record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dining-record")
@Getter
@Setter
@AllArgsConstructor
public class DiningRecordEntity {

    @Id
    private String diningRecordDay;

    private long tokensSold;

    private long totalSales;

    private long totalExpenses;

}