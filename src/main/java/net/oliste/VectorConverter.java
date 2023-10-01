package net.oliste;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class VectorConverter implements AttributeConverter<List<Double>, String> {

  @Override
  public String convertToDatabaseColumn(List<Double> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "'[]'";
    }

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");

    for (int i = 0; i < attribute.size(); i++) {
      stringBuilder.append(attribute.get(i));
      if (i < attribute.size() - 1) {
        stringBuilder.append(",");
      }
    }

    stringBuilder.append("]");
    return stringBuilder.toString();
  }

  public List<Double> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return new ArrayList<>();
    }

    String[] values = dbData.substring(1, dbData.length() - 1).split(",");

    List<Double> embeddingList = new ArrayList<>();
    for (String value : values) {
      embeddingList.add(Double.valueOf(value.trim()));
    }

    return embeddingList;
  }
}