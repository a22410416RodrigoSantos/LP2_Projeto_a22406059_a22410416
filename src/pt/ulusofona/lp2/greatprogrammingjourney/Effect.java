package pt.ulusofona.lp2.greatprogrammingjourney;

public interface Effect {
    int getId();

    String getTitle();

    String getImageName();

    void apply(Programmer programmer, GameManager gameManager);
}