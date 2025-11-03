package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    @Test
    public void testCreateInitialBoard_ValidInput_TwoPlayers() {
        GameManager game = new GameManager();
        String[][] info = {
                {"10", "Alice", "Java", "Blue"},
                {"20", "Bob", "Python", "Green"}
        };
        assertTrue(game.createInitialBoard(info, 10));
        assertEquals(10, game.getCurrentPlayerID());
    }

    @Test
    public void testCreateInitialBoard_ValidInput_FourPlayers() {
        GameManager game = new GameManager();
        String[][] info = {
                {"5", "Ana", "C", "Purple"},
                {"15", "Bruno", "Java", "Blue"},
                {"25", "Clara", "Python", "Green"},
                {"35", "Diogo", "C++", "Brown"}
        };
        assertTrue(game.createInitialBoard(info, 20));
        assertEquals(5, game.getCurrentPlayerID());
    }

    @Test
    public void testGetCurrentPlayerID_AfterInit() {
        GameManager game = new GameManager();
        String[][] info = {
                {"100", "Test", "Java", "Blue"},
                {"50", "Other", "C", "Green"}
        };
        game.createInitialBoard(info, 15);
        assertEquals(50, game.getCurrentPlayerID());
    }

    @Test
    public void testGetProgrammerInfoAsStr_InitialState() {
        GameManager game = new GameManager();
        String[][] info = {
                {"7", "Goiaba", "Closure; Common Lisp", "Brown"}
        };
        game.createInitialBoard(info, 10);
        assertEquals("7 | Goiaba | 1 | Closure; Common Lisp | Em Jogo",
                game.getProgrammerInfoAsStr(7));
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
    public void testGameIsOver_WhenPlayerAtEnd() {
        GameManager game = new GameManager();
        String[][] info = {
                {"1", "Winner", "Java", "Blue"}
        };
        game.createInitialBoard(info, 5);
        game.moveCurrentPlayer(4);
        assertTrue(game.gameIsOver());
    }
}