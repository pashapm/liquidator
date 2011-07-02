import junit.framework.TestCase;
import ru.hackaton.liquidator.Glass;

public class GlassTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
    }

    public void testAdd() throws Exception {
        Glass glass = new Glass(250, 50);
        glass.add(100);
        assertEquals(glass.getAmount(), 100);
    }

    public void testGetPercent() throws Exception {
        Glass glass = new Glass(250, 50);
        glass.add(125);
        assertEquals(50, glass.getPercent());
    }

    public void testGetState() throws Exception {
        Glass glass = new Glass(500, 100);
        assertEquals(Glass.STATE_MORE, glass.getState());
        glass.add(94);//94
        assertEquals(Glass.STATE_MORE, glass.getState());
        glass.add(1);//95
        assertEquals(Glass.STATE_OK, glass.getState());
        glass.add(10);//105
        assertEquals(Glass.STATE_OK, glass.getState());
        glass.add(1);//106
        assertEquals(Glass.STATE_TOO_MUCH, glass.getState());
        glass.add(394);//500
        assertEquals(Glass.STATE_TOO_MUCH, glass.getState());
        glass.add(1);//501
        assertEquals(Glass.STATE_OVERFLOW, glass.getState());
    }
}
