package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    @Test
    public void testCreateInitialBoard_ValidTwoPlayers() {
        GameManager game = new GameManager();
        String[][] info = {
                {"15", "Ada", "Java;Python", "Purple"},
                {"42", "Grace", "C", "Blue"}
        };
        assertTrue(game.createInitialBoard(info, 20));
        assertEquals(15, game.getCurrentPlayerID());
    }

    @Test
    public void testGetCurrentPlayerID_AfterInit() {
        GameManager game = new GameManager();
        String[][] info = {
                {"101", "Test", "Python", "Green"},
                {"50", "Other", "C", "Brown"}
        };
        game.createInitialBoard(info, 10);
        assertEquals(50, game.getCurrentPlayerID());
    }

    @Test
    public void testMoveCurrentPlayer_ValidMove() {
        GameManager game = new GameManager();
        String[][] info = {
                {"1", "Player", "Java", "Blue"}
        };
        game.createInitialBoard(info, 10);
        assertTrue(game.moveCurrentPlayer(3));
        assertEquals(4, game.players.get(0).getPosition());
    }

    @Test
    public void testGameIsOver_WhenAtEnd() {
        GameManager game = new GameManager();
        String[][] info = {
                {"1", "Winner", "Java", "Blue"}
        };
        game.createInitialBoard(info, 5);
        game.moveCurrentPlayer(4);
        assertTrue(game.gameIsOver());
    }
}