package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.List;

public class abiCodigoDuplicado extends Abismo {

    public abiCodigoDuplicado() {
        super(5, "Código Duplicado", "duplicated_code.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        List<Integer> history = programmer.getPositionHistory();
        if (history.size() >= 2) {
            int previous = history.get(history.size() - 2);
            programmer.setPosition(previous);
        } else {
            programmer.setPosition(1);
        }
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 10; // Herança
    }
}