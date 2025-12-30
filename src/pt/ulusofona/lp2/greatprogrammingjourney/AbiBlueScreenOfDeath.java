package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiBlueScreenOfDeath extends Abismo {

    public AbiBlueScreenOfDeath() {
        super(7, "Blue Screen of Death", "bsod.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        programmer.setInGame(false);
        programmer.setState("Derrotado");
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 5; // Ajuda do Professor
    }
}