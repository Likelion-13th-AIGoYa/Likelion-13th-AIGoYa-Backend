package kr.elroy.aigoya.store.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekSetConverter implements AttributeConverter<Set<DayOfWeek>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<DayOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(DayOfWeek::name)
                .sorted()
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }
}
