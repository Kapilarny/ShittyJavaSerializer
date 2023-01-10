package gg.kapilarny.testserializer;

public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
//        testClass.setTestString("Hello World!");
        testClass.setTestInt(123);
        testClass.setTestDouble(123.456);

        String serialized = ObjectSerializer.serialize(testClass);
        System.out.println(serialized);

        TestClass deserialized = (TestClass) ObjectSerializer.deserialize(serialized);
        System.out.println(deserialized.getTestInt());
    }
}