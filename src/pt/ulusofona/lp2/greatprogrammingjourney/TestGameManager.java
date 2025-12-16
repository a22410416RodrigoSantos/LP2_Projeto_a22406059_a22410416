package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    @Test
    public void testCreateInitialBoardValid() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Alice", "Java;Python", "Blue"},
                {"2", "Bob", "C++;JS", "Green"}
        };
        boolean result = gm.createInitialBoard(players, 20);
        assertTrue(result);
        assertEquals(1, gm.getCurrentPlayerID());
    }
}