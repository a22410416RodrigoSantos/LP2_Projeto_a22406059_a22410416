package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    @Test
    public void testCreateInitialBoard_ValidInput() {
        GameManager game = new GameManager();

        String[][] playerInfo = {
                {"42", "Ada Lovelace", "Java;Python;C", "PURPLE"},
                {"15", "Grace Hopper", "C;COBOL", "BLUE"}
        };

        boolean result = game.createInitialBoard(playerInfo, 20);

        assertTrue(result, "Deve retornar true com input válido");
        assertEquals(2, game.players.size(), "Deve ter 2 jogadores");
        assertEquals(42, game.getCurrentPlayerID(), "Primeiro jogador (menor ID) = 42? Não! Deve ser 15!");
    }

    @Test
    public void testGetCurrentPlayerID_AfterInit() {
        GameManager game = new GameManager();
        String[][] info = {
                {"101", "Teste1", "Python;Java", "GREEN"},
                {"102", "Teste2", "C", "BROWN"}
        };
        game.createInitialBoard(info, 10);
        assertEquals(101, game.getCurrentPlayerID(), "Menor ID deve ser o primeiro a jogar");
    }
}