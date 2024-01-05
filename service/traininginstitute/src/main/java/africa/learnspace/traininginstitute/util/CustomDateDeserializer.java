package africa.learnspace.traininginstitute.util;


import africa.learnspace.traininginstitute.exception.InstituteProgramException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomDateDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final LocalTime defaultTime = LocalTime.now();

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            return LocalDateTime.parse(jsonParser.getText() + "T" + defaultTime.format(timeFormatter), dateFormatter);
        } catch (Exception exception) {
           throw new InstituteProgramException("Invalid Date Format Kindly enter date in yyyy-MM-dd");
        }
    }
}