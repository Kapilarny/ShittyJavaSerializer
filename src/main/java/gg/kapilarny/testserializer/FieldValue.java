/*
 * Copyright @Kapilarny 2023.
 * Do whatever you want with this lol.
 */

package gg.kapilarny.testserializer;

public class FieldValue {
    private String fieldName;
    private String fieldValue;

    public FieldValue(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

}
