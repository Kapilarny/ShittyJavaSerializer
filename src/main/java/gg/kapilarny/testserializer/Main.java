package gg.kapilarny.testserializer;

public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.setTestString("Hello World!");
        testClass.setTestInt(123);
        testClass.setTestDouble(123.456);

        String serialized = ObjectSerializer.serialize(testClass);
        System.out.println(serialized);

        TestClass deserialized = (TestClass) ObjectSerializer.deserialize(serialized);
        System.out.println(deserialized.getTestString());
    }

    private static class TestClass {
        private String testString;
        private int testInt;
        private double testDouble;
        private TestNestedClass testNestedClass;

        public TestClass() {
            testNestedClass = new TestNestedClass(432, "heheh");
        }

        public String getTestString() {
            return testString;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }

        public int getTestInt() {
            return testInt;
        }

        public void setTestInt(int testInt) {
            this.testInt = testInt;
        }

        public double getTestDouble() {
            return testDouble;
        }

        public void setTestDouble(double testDouble) {
            this.testDouble = testDouble;
        }
    }

    private static class TestNestedClass {
        int testInt;
        String testString;

        public TestNestedClass(int testInt, String testString) {
            this.testInt = testInt;
            this.testString = testString;
        }
    }
}