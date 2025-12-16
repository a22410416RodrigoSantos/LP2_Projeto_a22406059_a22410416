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

    @Override public int getId() { return id; }
    @Override public String getTitle() { return title; }
    @Override public String getImageName() { return imageName; }
    @Override public String getType() { return "ferramenta"; }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        programmer.addFerramenta(this);
    }

    public abstract boolean podeNeutralizar(Abismo abismo);
}