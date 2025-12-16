package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiCrash extends Abismo {

    public AbiCrash() {
        super(4, "Crash", "crash.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        programmer.setPosition(1);
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 14; // IDE
    }
}