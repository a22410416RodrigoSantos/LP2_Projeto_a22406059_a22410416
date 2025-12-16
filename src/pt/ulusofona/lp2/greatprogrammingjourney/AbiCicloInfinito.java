package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiCicloInfinito extends Abismo {

    public AbiCicloInfinito() {
        super(8, "Ciclo Infinito", "infinite_loop.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int position = programmer.getPosition();

        // Libertar jogador preso (se existir)
        for (Programmer p : gameManager.getPlayers()) {
            if (p.isInGame() && p.getPosition() == position && "Preso".equals(p.getState())) {
                p.setState("Em Jogo");
                break;
            }
        }

        // Prender o novo jogador
        programmer.setState("Preso");
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 15; // Ajuda do Professor
    }
}