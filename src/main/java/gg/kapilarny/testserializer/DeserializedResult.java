/*
 * Copyright @Kapilarny 2023.
 * Do whatever you want with this lol.
 */

package gg.kapilarny.testserializer;

import java.util.List;

public class DeserializedResult {
    public Object object;
    public List<DeserializedStaticObject> staticObjects;

    public DeserializedResult(Object object, List<DeserializedStaticObject> staticObjects) {
        this.object = object;
        this.staticObjects = staticObjects;
    }

    public Object getObject() {
        return object;
    }

    public List<DeserializedStaticObject> getStaticObjects() {
        return staticObjects;
    }
}
