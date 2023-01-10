package gg.kapilarny.testserializer;

import java.lang.reflect.Field;

public class ObjectSerializer {

    private static String serializeObject(Object object, boolean isNested) {
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
                    if(field.get(object) != null) {
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

    public static String serialize(Object object) {
        return serializeObject(object, false);
    }

    // Field Format: fieldName:value;
    private static Object readField(Object mainObj, String fieldString) {
        Class<?> clazz = null;
        Object resultObject = null;

        StringBuilder fieldName = new StringBuilder();
        char[] chars = fieldString.toCharArray();
        int currentCharacter = 0;
        while (true) {
            if (currentCharacter > chars.length) break;
            char c = chars[currentCharacter];

            if (c == ':') {
                currentCharacter++; // Skip the ':' character
                break;
            }

            fieldName.append(c);
            currentCharacter++;
        }

        try {
            Field field = mainObj.getClass().getDeclaredField(fieldName.toString());
            field.setAccessible(true);
            clazz = field.getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        if(clazz.isPrimitive()) {
            if(clazz.equals(int.class)) {
                resultObject = Integer.parseInt(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(boolean.class)) {
                resultObject = Boolean.parseBoolean(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(double.class)) {
                resultObject = Double.parseDouble(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(float.class)) {
                resultObject = Float.parseFloat(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(long.class)) {
                resultObject = Long.parseLong(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(short.class)) {
                resultObject = Short.parseShort(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(byte.class)) {
                resultObject = Byte.parseByte(fieldString.substring(fieldName.length() + 1, fieldString.length() - 1));
            } else if(clazz.equals(char.class)) {
                resultObject = fieldString.substring(fieldName.length() + 1, fieldString.length() - 1).charAt(0);
            }
        } else {
            try {
                resultObject = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String currentField = "";
        String currentFieldName = "";
        int openedStatements = 0;
        boolean hadOpenStatements = false;
        while (true) {
            if (currentCharacter > chars.length) break;
            char c = chars[currentCharacter];

            if (c == '(') {
                openedStatements++;
                hadOpenStatements = true;
            } else if (c == ')') {
                openedStatements--;
                if(openedStatements == 0) {
                    break;
                }
            }

            if (c == ':' && currentFieldName.equals("")) {
                currentFieldName = currentField;
                currentField = "";
                currentCharacter++;
                continue;
            }

            if (c == ';' && openedStatements == 0 && hadOpenStatements) {
                try {
                    Object obj = readField(resultObject, currentField);
                    Field field = obj.getClass().getDeclaredField(currentFieldName);
                    field.setAccessible(true);
                    field.set(resultObject, obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                currentField = "";
                currentFieldName = "";
                hadOpenStatements = false;
            } else if(c == ';') {
                try {
                    Field field = clazz.getDeclaredField(currentFieldName);
                    Object obj = null;
                    field.setAccessible(true);

                    if(!currentField.equals("null")) {
                        if(field.getType().equals(int.class)) {
                            obj = Integer.parseInt(currentField);
                        } else if(field.getType().equals(boolean.class)) {
                            obj = Boolean.parseBoolean(currentField);
                        } else if(field.getType().equals(double.class)) {
                            obj = Double.parseDouble(currentField);
                        } else if(field.getType().equals(float.class)) {
                            obj = Float.parseFloat(currentField);
                        } else if(field.getType().equals(long.class)) {
                            obj = Long.parseLong(currentField);
                        } else if(field.getType().equals(short.class)) {
                            obj = Short.parseShort(currentField);
                        } else if(field.getType().equals(byte.class)) {
                            obj = Byte.parseByte(currentField);
                        } else if(field.getType().equals(char.class)) {
                            obj = currentField.charAt(0);
                        }
                    }

                    field.set(resultObject, obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            currentField += c;
            currentCharacter++;
        }

        return resultObject;
    }

    public static Object deserialize(String string) {
        Class<?> clazz = null;
        Object resultObject = null;

        StringBuilder clazzName = new StringBuilder();
        char[] chars = string.toCharArray();
        int currentCharacter = 0;
        while(true) {
            if(currentCharacter > chars.length) break;
            char c = chars[currentCharacter];

            if(c == '{') {
                currentCharacter++; // Skip the '{' character
                break;
            }

            clazzName.append(c);
            currentCharacter++;
        }

        try {
            clazz = Class.forName(clazzName.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            resultObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String currentField = "";
        String currentFieldName = "";
        int openedStatements = 0;

        while(true) {
            if(currentCharacter > chars.length) break;
            char c = chars[currentCharacter];

            if(c == '}') {
                break;
            }

            if(c == '(') {
                openedStatements++;
            } else if(c == ')') {
                openedStatements--;
            }

            if(c == ':' && currentFieldName.equals("")) {
                currentFieldName = currentField;
            }

            if(c == ';' && openedStatements == 0) {
                try {
                    Object obj = readField(resultObject, currentField);
                    Field field = obj.getClass().getDeclaredField(currentFieldName);
                    field.setAccessible(true);
                    field.set(resultObject, obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                currentField = "";
                currentFieldName = "";
            }

            currentField += c;
            currentCharacter++;
        }

        return resultObject;
    }
}
