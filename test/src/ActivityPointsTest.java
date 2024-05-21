import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityPointsTest {

    @Test
    public void testStudyActivityMapObjectConstructor() {
        // Create a MapObject with properties
        MapObject mapObject = new MapObject();
        MapProperties properties = mapObject.getProperties();
        properties.put("name", "Study");
        properties.put("activityStr", "STUDY");
        properties.put("activityType", "STUDY");
        properties.put("activityTime", 1);
        properties.put("changeAmount", "0.3,0.2,0.15,0.2");

        // Verify the properties are correctly set
        assertEquals("Study", mapObject.getProperties().get("name"));
        assertEquals("STUDY", mapObject.getProperties().get("activityStr"));
        assertEquals("STUDY", mapObject.getProperties().get("activityType"));
        assertEquals(1, mapObject.getProperties().get("activityTime"));
        assertEquals("0.3,0.2,0.15,0.2", mapObject.getProperties().get("changeAmount"));
    }

    @Test
    public void testSleepActivityMapObjectConstructor() {
        // Create a MapObject with properties
        MapObject mapObject = new MapObject();
        MapProperties properties = mapObject.getProperties();
        properties.put("name", "Sleep");
        properties.put("activityStr", "SLEEP");
        properties.put("activityType", "SLEEP");
        properties.put("activityTime", 8);
        properties.put("changeAmount", "0,0,0,0.3");

        // Verify the properties are correctly set
        assertEquals("Sleep", mapObject.getProperties().get("name"));
        assertEquals("SLEEP", mapObject.getProperties().get("activityStr"));
        assertEquals("SLEEP", mapObject.getProperties().get("activityType"));
        assertEquals(8, mapObject.getProperties().get("activityTime"));
        assertEquals("0,0,0,0.3", mapObject.getProperties().get("changeAmount"));
    }

    @Test
    public void testNapActivityMapObjectConstructor() {
        // Create a MapObject with properties
        MapObject mapObject = new MapObject();
        MapProperties properties = mapObject.getProperties();
        properties.put("name", "Nap");
        properties.put("activityStr", "NAP");
        properties.put("activityType", "NAP");
        properties.put("activityTime", 1);
        properties.put("changeAmount", "0,0,0,0.1");

        // Verify the properties are correctly set
        assertEquals("Nap", mapObject.getProperties().get("name"));
        assertEquals("NAP", mapObject.getProperties().get("activityStr"));
        assertEquals("NAP", mapObject.getProperties().get("activityType"));
        assertEquals(1, mapObject.getProperties().get("activityTime"));
        assertEquals("0,0,0,0.1", mapObject.getProperties().get("changeAmount"));
    }
}