package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.List;

public class abiEfeitosSecundarios extends Abismo {

    public abiEfeitosSecundarios() {
        super(6, "Efeitos Secundários", "side_effects.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua para a posição onde estava há 2 movimentos atrás
        // (duas posições anteriores no histórico)

        List<Integer> history = programmer.getPositionHistory();

        if (history.size() >= 3) { // precisa de pelo menos 3 posições (atual + 2 anteriores)
            // A posição há 2 movimentos atrás é a terceira a contar do fim
            int posTwoMovesBack = history.get(history.size() - 3);
            programmer.setPosition(posTwoMovesBack);
        } else {
            // Se não houver histórico suficiente, volta ao início
            programmer.setPosition(1);
        }
    }
}