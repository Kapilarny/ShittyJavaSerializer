/*
 * Copyright @Kapilarny 2023.
 * Do whatever you want with this lol.
 */

package gg.kapilarny.testserializer;

import java.io.Serializable;

public class TestNestedClass implements Serializable {
    int testInt;
    String testString;

    public TestNestedClass(int testInt, String testString) {
        this.testInt = testInt;
        this.testString = testString;
    }
}
