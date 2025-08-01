package io.github.mdraihan27.mmh.dining.controllers.dining_record;

import io.github.mdraihan27.mmh.dining.entities.dining_record.DiningRecordEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningRecordRepository;
import io.github.mdraihan27.mmh.dining.services.dining_record.DiningRecordService;
import io.github.mdraihan27.mmh.dining.services.user.UserService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import io.github.mdraihan27.mmh.dining.utilities.GetAuthenticatedUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dining-record")
public class GetDiningRecordController {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private DiningRecordRepository diningRecordRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GetAuthenticatedUserUtil  getAuthenticatedUserUtil;


    @GetMapping("info")
    public ResponseEntity getDiningRecordByDate(@RequestParam String date) {
        try{

            if (date == null || date.isEmpty()) {
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Date cannot be empty"));
            }

            Optional<DiningRecordEntity> diningRecord = diningRecordRepository.findById(date);
            if (diningRecord.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(createResponseUtil.createResponseBody(true, "Balance updated", "diningRecord", diningRecord.get()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(createResponseUtil.createResponseBody(false, "Something went wrong"));
        }
    }
}
