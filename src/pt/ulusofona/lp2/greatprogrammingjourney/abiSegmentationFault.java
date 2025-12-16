package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiSegmentationFault extends Abismo {

    public abiSegmentationFault() {
        super(9, "Segmentation Fault", "seg_fault.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int position = programmer.getPosition();
        int count = 0;

        for (Programmer p : gameManager.getPlayers()) {
            if (p.isInGame() && p.getPosition() == position) {
                count++;
            }
        }

        if (count >= 2) {
            for (Programmer p : gameManager.getPlayers()) {
                if (p.isInGame() && p.getPosition() == position) {
                    int newPos = Math.max(1, p.getPosition() - 3);
                    p.setPosition(newPos);
                }
            }
        }
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 15; // Ajuda do Professor
    }
}