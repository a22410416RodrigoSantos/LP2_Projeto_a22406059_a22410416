package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Abismo implements Effect {
    protected int id;
    protected String title;
    protected String imageName;

    public Abismo(int id, String title, String imageName) {
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

    // Cada abismo implementa o seu efeito
    @Override
    public abstract void apply(Programmer programmer, GameManager gameManager);
}