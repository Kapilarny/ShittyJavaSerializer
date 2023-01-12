/*
 * Copyright @Kapilarny 2023.
 * Do whatever you want with this lol.
 */

package gg.kapilarny.testserializer;

public class DeserializedStaticObject {
    private Object object;
    private String className;
    private String fieldName;

    public DeserializedStaticObject(Object object, String className, String fieldName) {
        this.object = object;
        this.className = className;
        this.fieldName = fieldName;
    }

    public Object getObject() {
        return object;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }
}
