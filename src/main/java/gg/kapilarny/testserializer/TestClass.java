package gg.kapilarny.testserializer;

public class TestClass {
//    private String testString;
    private int testInt;
    private double testDouble;
    private TestNestedClass testNestedClass;

    public TestClass() {
        testNestedClass = new TestNestedClass(432, "heheh");
    }

//        public String getTestString() {
//            return testString;
//        }

//        public void setTestString(String testString) {
//            this.testString = testString;
//        }

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

