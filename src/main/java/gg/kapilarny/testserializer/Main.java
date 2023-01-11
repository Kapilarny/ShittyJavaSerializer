package gg.kapilarny.testserializer;

public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
//        testClass.setTestString("Hello World!");
        testClass.setTestInt(123);
        testClass.setTestDouble(123.456);

        ObjectSerializer serializer = new ObjectSerializer();

        String serialized = serializer.serialize(testClass);
        System.out.println(serialized);

        TestClass deserialized = (TestClass) serializer.deserialize(serialized);
        System.out.println(deserialized.getTestInt());
        System.out.println(deserialized.getTestDouble());
        System.out.println(deserialized.getTestNestedClass().testInt);
        System.out.println(deserialized.getTestNestedClass().testString);
    }
}