/*
 * Copyright @Kapilarny 2023.
 * Do whatever you want with this lol.
 */

package gg.kapilarny.testserializer;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectSerializer {

    private static final Objenesis objenesis = new ObjenesisStd();

    private String serializeObject(Object object, boolean isNested) {
        Class<?> clazz = object.getClass();

        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        if(isNested) {
            sb.append("(");
        } else {
            sb.append("{");
        }

        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if(field.get(object) == null) continue;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if(field.getType().isArray()) {
                try {
                    sb.append(field.getName()).append(":").append("[");

                    if(field.getType().getComponentType().isPrimitive()) {
                        if(field.getType().getComponentType().equals(int.class)) {
                            int[] array = (int[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(boolean.class)) {
                            boolean[] array = (boolean[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(double.class)) {
                            double[] array = (double[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(float.class)) {
                            float[] array = (float[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(long.class)) {
                            long[] array = (long[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(short.class)) {
                            short[] array = (short[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(byte.class)) {
                            byte[] array = (byte[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        } else if(field.getType().getComponentType().equals(char.class)) {
                            char[] array = (char[]) field.get(object);
                            for (int i = 0; i < array.length; i++) {
                                sb.append(array[i]);
                                if (i != array.length - 1) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else {
                        Object[] array = (Object[]) field.get(object);
                        for (Object o : array) {
                            sb.append(serializeObject(o, true)).append(",");
                        }
                    }

                    sb.append("]");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if(field.getType().isPrimitive()) {
                try {
                    if(field.get(object) == null) {
                        sb.append(field.getName()).append(":").append("null");
                    } else {
                        sb.append(field.getName()).append(":").append(field.get(object).toString());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Object nestedObject = field.get(object);
//                    if(Modifier.isStatic(nestedObject.getClass().getModifiers())) continue;

                    if(nestedObject != null) {
                        sb.append(field.getName()).append(":").append(serializeObject(nestedObject, true));
                    } else {
                        sb.append(field.getName()).append(":").append("null").append(";");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }

            sb.append(";");

            field.setAccessible(false);
        }

        if(isNested) {
            sb.append(")");
        } else {
            sb.append("}");
        }

        return sb.toString();
    }

    public String serialize(Object object) {
        return serializeObject(object, false);
    }

    private Object parseObject(String fieldString, List<DeserializedStaticObject> staticObjects) {
        Object resultObject = null;
        Class<?> clazz = null;
        Class<?> objClazz = null;

        System.out.println("Parsing: " + fieldString);

        if(fieldString.equals("[]")) return null;

        int currentChar = 0;
        StringBuilder className = new StringBuilder();
        while(true) {
            if (currentChar >= fieldString.length()) {
                break;
            }

            char c = fieldString.charAt(currentChar);

            if (c == '(') {
                currentChar++; // Skip the '{' character
                break;
            }

            currentChar++;
            className.append(c);
        }

        try {
            clazz = Class.forName(className.toString());
//            resultObject = clazz.newInstance();
            ObjectInstantiator instantiator = objenesis.getInstantiatorOf(clazz);
            resultObject = instantiator.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<FieldValue> fieldValues = new ArrayList<>();
        StringBuilder fieldName = new StringBuilder();
        StringBuilder fieldValue = new StringBuilder();
        int bracketCount = 0;
        while(true) {
            if(currentChar >= fieldString.length()) {
                break;
            }

            char c = fieldString.charAt(currentChar);

            if(c == '(') {
                bracketCount++;
            } else if(c == ')') {
                bracketCount--;
            }

            if(c == ':' && bracketCount == 0) {
                fieldName.append(fieldValue);
                fieldValue = new StringBuilder(); // Reset the field value
                currentChar++;
                continue;
            }

            if(c == ';' && bracketCount == 0) {
                fieldValues.add(new FieldValue(fieldName.toString(), fieldValue.toString()));
                fieldName = new StringBuilder();
                fieldValue = new StringBuilder();
                currentChar++;
                continue;
            }

            currentChar++;
            fieldValue.append(c);
        }

        Map<String, Object> fieldMap = new HashMap<>();
        for(FieldValue field : fieldValues) {
            System.out.println("Field: " + field.getFieldName() + " Value: " + field.getFieldValue());

            try {
                Field f = clazz.getDeclaredField(field.getFieldName());
                f.setAccessible(true);

                if (f.getType().isArray()) {
                    if (f.getType().getComponentType().isPrimitive()) {
                        if (f.getType().getComponentType().equals(int.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            int[] array = new int[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Integer.parseInt(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(double.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            double[] array = new double[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Double.parseDouble(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(boolean.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            boolean[] array = new boolean[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Boolean.parseBoolean(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(float.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            float[] array = new float[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Float.parseFloat(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(long.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            long[] array = new long[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Long.parseLong(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(short.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            short[] array = new short[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Short.parseShort(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(byte.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            byte[] array = new byte[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = Byte.parseByte(values[i]);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        } else if (f.getType().getComponentType().equals(char.class)) {
                            String converted = field.getFieldValue().replace("[", "");
                            converted = converted.replace("]", "");
                            String[] values = converted.split(",");
                            char[] array = new char[values.length];
                            for (int i = 0; i < values.length; i++) {
                                array[i] = values[i].charAt(0);
                            }
                            fieldMap.put(field.getFieldName(), array);
                        }
                    } else {
                        // If the field is an array of objects, we need to parse the array
                        String[] array = field.getFieldValue().split(",");
                        Object[] resultArray = new Object[array.length];

                        for(int i = 0; i < array.length; i++) {
                            resultArray[i] = parseObject(array[i], staticObjects);
                        }

                        fieldMap.put(field.getFieldName(), resultArray);
                    }
                } else if (f.getType().isPrimitive()) {
                    if (f.getType().equals(int.class)) {
                        fieldMap.put(field.getFieldName(), Integer.parseInt(field.getFieldValue()));
                    } else if (f.getType().equals(double.class)) {
                        fieldMap.put(field.getFieldName(), Double.parseDouble(field.getFieldValue()));
                    } else if (f.getType().equals(boolean.class)) {
                        fieldMap.put(field.getFieldName(), Boolean.parseBoolean(field.getFieldValue()));
                    } else if (f.getType().equals(float.class)) {
                        fieldMap.put(field.getFieldName(), Float.parseFloat(field.getFieldValue()));
                    } else if (f.getType().equals(long.class)) {
                        fieldMap.put(field.getFieldName(), Long.parseLong(field.getFieldValue()));
                    } else if (f.getType().equals(short.class)) {
                        fieldMap.put(field.getFieldName(), Short.parseShort(field.getFieldValue()));
                    } else if (f.getType().equals(byte.class)) {
                        fieldMap.put(field.getFieldName(), Byte.parseByte(field.getFieldValue()));
                    } else if (f.getType().equals(char.class)) {
                        fieldMap.put(field.getFieldName(), field.getFieldValue().charAt(0));
                    }
                } else {
                    fieldMap.put(field.getFieldName(), parseObject(field.getFieldValue(), staticObjects));
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        for(Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            try {
                Field f = clazz.getDeclaredField(entry.getKey());
                if(Modifier.isStatic(f.getModifiers())) {
                    staticObjects.add(new DeserializedStaticObject(entry.getValue(), clazz.getName(), entry.getKey()));
                    continue;
                }
                f.setAccessible(true);
                f.set(resultObject, entry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return resultObject;
    }

    private void processFieldValue(Object object, FieldValue fieldValue, Class objClass, List<DeserializedStaticObject> staticObjects) {
        Object resultObject = null;
        Class<?> clazz = null;

        // Get the class of the field
        try {
            clazz = object.getClass().getDeclaredField(fieldValue.getFieldName()).getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        if(clazz.isPrimitive()) {
            // If the field is primitive, we can just set the value
            if(clazz.equals(int.class)) {
                resultObject = Integer.parseInt(fieldValue.getFieldValue());
            } else if(clazz.equals(boolean.class)) {
                resultObject = Boolean.parseBoolean(fieldValue.getFieldValue());
            } else if(clazz.equals(double.class)) {
                resultObject = Double.parseDouble(fieldValue.getFieldValue());
            } else if(clazz.equals(float.class)) {
                resultObject = Float.parseFloat(fieldValue.getFieldValue());
            } else if(clazz.equals(long.class)) {
                resultObject = Long.parseLong(fieldValue.getFieldValue());
            } else if(clazz.equals(short.class)) {
                resultObject = Short.parseShort(fieldValue.getFieldValue());
            } else if(clazz.equals(byte.class)) {
                resultObject = Byte.parseByte(fieldValue.getFieldValue());
            } else if(clazz.equals(char.class)) {
                resultObject = fieldValue.getFieldValue().charAt(0);
            }
        } else if(clazz.isArray()) {
            // If the field is an array, we need to parse the array
            if(clazz.getComponentType().isPrimitive()) {
                // If the array is primitive, we can just parse the array
                if (clazz.getComponentType().equals(int.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    int[] resultArray = new int[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Integer.parseInt(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(boolean.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    boolean[] resultArray = new boolean[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Boolean.parseBoolean(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(double.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    double[] resultArray = new double[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Double.parseDouble(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(float.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    float[] resultArray = new float[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Float.parseFloat(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(long.class)) {
                    String[] array = fieldValue.getFieldValue().split(",");
                    long[] resultArray = new long[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Long.parseLong(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(short.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    short[] resultArray = new short[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Short.parseShort(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(byte.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    byte[] resultArray = new byte[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = Byte.parseByte(array[i]);
                    }
                    resultObject = resultArray;
                } else if (clazz.getComponentType().equals(char.class)) {
                    String converted = fieldValue.getFieldValue().replace("[", "");
                    converted = converted.replace("]", "");
                    String[] array = converted.split(",");
                    char[] resultArray = new char[array.length];
                    for (int i = 0; i < array.length; i++) {
                        resultArray[i] = array[i].charAt(0);
                    }
                    resultObject = resultArray;
                }
            } else {
                // If the field is an array of objects, we need to parse the array
                String[] array = fieldValue.getFieldValue().split(",");
                Object[] resultArray = new Object[array.length];

                // Get the class of the array objects
                Class<?> objectsClazz = null;
                try {
                    objectsClazz = Class.forName(clazz.getComponentType().getName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                // Parse each object in the array
                for (int i = 0; i < array.length; i++) {
                    resultArray[i] = parseObject(array[i], staticObjects);
                }
            }
        } else {
            // Parse the field
            resultObject = parseObject(fieldValue.getFieldValue(), staticObjects);
        }

        // Set the field value
        try {
            System.out.println("Setting field " + fieldValue.getFieldName() + " in class " + objClass.getSimpleName() + " to " + resultObject);
            Field field = object.getClass().getDeclaredField(fieldValue.getFieldName());
            if(Modifier.isStatic(field.getModifiers())) {
                System.out.println("Field is static, adding to static fields");
                staticObjects.add(new DeserializedStaticObject(resultObject, field.getDeclaringClass().getName(), field.getName()));
            } else {
                field.setAccessible(true); // Make the field accessible
                field.set(object, resultObject); // Set the field value
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public DeserializedResult deserializeWithStatics(String objectString) {
        return deserializeObject(objectString, true);
    }

    public Object deserialize(String objectString) {
        return deserializeObject(objectString, false).getObject();
    }

    private DeserializedResult deserializeObject(String objectString, boolean doesParseStatics) {
        Object resultObject = null;
        Class<?> clazz = null;

        int currentChar = 0;
        StringBuilder className = new StringBuilder();
        while(true) {
            if(currentChar > objectString.length()) {
                throw new RuntimeException("Invalid object string");
            }

            char c = objectString.charAt(currentChar);

            if(c == '{') {
                currentChar++; // Skip the '{' character
                break;
            }

            currentChar++;
            className.append(c);
        }

        try {
            clazz = Class.forName(className.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ObjectInstantiator instantiator = objenesis.getInstantiatorOf(clazz);
        resultObject = instantiator.newInstance();

        List<FieldValue> fieldValues = new ArrayList<>();
        StringBuilder fieldName = new StringBuilder();
        StringBuilder fieldValue = new StringBuilder();
        int bracketCount = 0;
        while(true) {
            if(currentChar > objectString.length()) {
                throw new RuntimeException("Invalid object string");
            }

            char c = objectString.charAt(currentChar);

            if(c == '}') { // The string has ended.
                break;
            }

            if(c == '(') {
                bracketCount++;
            } else if(c == ')') {
                bracketCount--;
            }

            if(c == ':' && bracketCount == 0) {
                fieldName.append(fieldValue);
                fieldValue = new StringBuilder(); // Reset the field value
                currentChar++;
                continue;
            }

            if(c == ';' && bracketCount == 0) {
                fieldValues.add(new FieldValue(fieldName.toString(), fieldValue.toString()));
                fieldName = new StringBuilder();
                fieldValue = new StringBuilder();
                currentChar++;
                continue;
            }

            currentChar++;
            fieldValue.append(c);
        }

        List<DeserializedStaticObject> staticObjects = new ArrayList<>();
        for(FieldValue field : fieldValues) {
            processFieldValue(resultObject, field, clazz, staticObjects);
        }

        for(DeserializedStaticObject staticObject : staticObjects) {
            System.out.println("Static field: " + staticObject.getFieldName());
        }

        return new DeserializedResult(resultObject, staticObjects);
    }
}
