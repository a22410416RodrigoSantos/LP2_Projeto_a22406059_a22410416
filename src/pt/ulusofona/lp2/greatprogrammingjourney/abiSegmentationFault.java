package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiSegmentationFault extends Abismo {

    public abiSegmentationFault() {
        super(9, "Segmentation Fault", "seg_fault.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int position = programmer.getPosition();

        // Conta quantos programadores ativos estão nesta casa
        int count = 0;
        for (Programmer p : gameManager.getPlayers()) {
            if (p.isInGame() && p.getPosition() == position) {
                count++;
            }
        }

        // Só ativa se houver 2 ou mais programadores na mesma casa
        if (count >= 2) {
            // Todos os programadores nessa casa recuam 3 casas
            for (Programmer p : gameManager.getPlayers()) {
                if (p.isInGame() && p.getPosition() == position) {
                    int newPos = p.getPosition() - 3;
                    if (newPos < 1) newPos = 1;
                    p.setPosition(newPos);
                }
            }
        }
        // Se só houver 1, nada acontece
    }
}