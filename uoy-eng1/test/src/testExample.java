
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.badlogic.gdx.Gdx;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class testExample {
    @Test
    public void badlogicLogoFileExists() {
        assertTrue("This test will only pass when the badlogic.jpg file coming with a new project setup has not been deleted.", Gdx.files
                .internal("../assets/badlogic.jpg").exists());
    }
}

