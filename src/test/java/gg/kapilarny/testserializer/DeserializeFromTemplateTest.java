/*
 * Copyright (c) 2023.
 */

package gg.kapilarny.testserializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeserializeFromTemplateTest {

        @Test
        public void deserializeFromTemplate() {
            ObjectSerializer serializer = new ObjectSerializer();
            String serializedObject = serializer.serialize(new TestClass());
            DeserializedResult result = serializer.deserializeIntoObject(serializedObject, new TestClassTemplate());
            TestClassTemplate deserialized = (TestClassTemplate) result.getObject();

            Assertions.assertEquals(69, deserialized.testInt);
        }

        class TestClass {
            public int testInt;

            public TestClass() {
                testInt = 69;
            }
        }

        class TestClassTemplate {
            public int testInt;
        }
}
