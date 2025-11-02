package pt.ulusofona.lp2.thegreatjourney;

public class Programmer {
    private int id;
    private String name;
    private String[] favoriteLanguages;
    private String color;
    private int position;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String[] getFavoriteLanguages() { return favoriteLanguages; }
    public void setFavoriteLanguages(String[] langs) { this.favoriteLanguages = langs; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getPosition() { return position; }
    public void setPosition(int pos) { this.position = pos; }
}