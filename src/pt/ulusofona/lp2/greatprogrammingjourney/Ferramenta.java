package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Ferramenta implements Effect {
    protected int id;
    protected String title;
    protected String imageName;

    public Ferramenta(int id, String title, String imageName) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getImageName() { return imageName; }

    // Quando o jogador cai numa casa com ferramenta, apanha-a
    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        programmer.addFerramenta(this);
    }

    // Cada ferramenta decide se anula um abismo específico
    public abstract boolean podeNeutralizar(Abismo abismo);
}