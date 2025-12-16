package pt.ulusofona.lp2.greatprogrammingjourney;
import java.util.List;

public class abiCodigoDuplicado extends Abismo {

    public abiCodigoDuplicado() {
        super(5, "Código Duplicado", "duplicated_code.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua para a posição onde estava ANTES de chegar a esta casa
        // (posição anterior no histórico)
        List<Integer> history = programmer.getPositionHistory();

        if (history.size() >= 2) {
            // Remove a posição atual (a última)
            history.remove(history.size() - 1);
            // Volta para a penúltima (posição anterior)
            int previousPos = history.get(history.size() - 1);
            programmer.setPosition(previousPos);
        } else {
            // Se não tiver histórico suficiente, vai para 1
            programmer.setPosition(1);
        }
    }
}