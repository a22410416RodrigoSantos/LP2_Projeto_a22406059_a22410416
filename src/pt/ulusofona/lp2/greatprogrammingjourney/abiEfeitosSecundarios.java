package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.List;

public class abiEfeitosSecundarios extends Abismo {

    public abiEfeitosSecundarios() {
        super(6, "Efeitos Secundários", "side_effects.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        List<Integer> history = programmer.getPositionHistory();
        if (history.size() >= 3) {
            int twoBack = history.get(history.size() - 3);
            programmer.setPosition(twoBack);
        } else {
            programmer.setPosition(1);
        }
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 11; // Programação Funcional
    }
}