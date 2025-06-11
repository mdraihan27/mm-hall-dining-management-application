package io.github.mdraihan27.mmh.dining.utilities;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatchTextPatternUtil {

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@student\\.just\\.edu\\.bd$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
