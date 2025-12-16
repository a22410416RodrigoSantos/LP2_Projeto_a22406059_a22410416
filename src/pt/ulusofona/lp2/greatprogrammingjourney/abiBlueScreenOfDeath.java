package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiBlueScreenOfDeath extends Abismo {

    public abiBlueScreenOfDeath() {
        super(7, "Blue Screen of Death", "bsod.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Jogador perde imediatamente o jogo
        programmer.setInGame(false);        // removido dos jogadores ativos
        programmer.setState("Derrotado");   // estado final

    }
}