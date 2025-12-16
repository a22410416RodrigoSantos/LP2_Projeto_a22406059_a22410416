package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiBlueScreenOfDeath extends Abismo {

    public abiBlueScreenOfDeath() {
        super(7, "Blue Screen of Death", "bsod.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        programmer.setInGame(false);
        programmer.setState("Derrotado");
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 15; // Ajuda do Professor
    }
}