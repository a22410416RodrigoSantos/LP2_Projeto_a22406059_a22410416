package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    ArrayList<Programmer> players = new ArrayList<>();
    ArrayList<Slot> slots = new ArrayList<>();
    int boardSize = 0;
    int currentPlayerIndex = 0;
    int totalTurns = 0;

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }

        this.boardSize = worldSize;
        players.clear();
        slots.clear();
        totalTurns = 0;

        for (int i = 1; i <= worldSize; i++) {
            Slot slot = new Slot();
            slot.setNumber(i);
            slot.setImageName("normal.png");
            if (i == 1) {
                slot.setStart(true);
            }
            if (i == worldSize) {
                slot.setEnd(true);
            }
            slots.add(slot);
        }

        for (String[] info : playerInfo) {
            if (info.length != 4) {
                return false;
            }

            try {
                int id = Integer.parseInt(info[0].trim());
                String name = info[1].trim();
                String languages = info[2].trim();
                String color = info[3].trim();

                if (id <= 0 || name.isEmpty()) {
                    return false;
                }

                if (!isValidColor(color)) {
                    return false;
                }

                for (Programmer p : players) {
                    if (p.getId() == id) {
                        return false;
                    }
                }

                Programmer p = new Programmer();
                p.setId(id);
                p.setName(name);

                String[] langs = languages.split(";");
                for (int i = 0; i < langs.length; i++) {
                    langs[i] = langs[i].trim();
                }
                p.setFavoriteLanguages(langs);

                p.setColor(color);
                p.setPosition(1);
                players.add(p);

            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (worldSize < 2 * players.size()) {
            return false;
        }

        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(i).getId() > players.get(j).getId()) {
                    Programmer temp = players.get(i);
                    players.set(i, players.get(j));
                    players.set(j, temp);
                }
            }
        }

        currentPlayerIndex = 0;
        return true;
    }

    public boolean isValidColor(String color) {
        if (color.equals("Purple")) {
            return true;
        }
        if (color.equals("Blue")) {
            return true;
        }
        if (color.equals("Green")) {
            return true;
        }
        if (color.equals("Brown")) {
            return true;
        }
        return false;
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }
        if (nrSquare == boardSize) {
            return "glory.png";
        }
        return "normal.png";
    }

    public String[] getProgrammerInfo(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String langStr = "";
                for (int i = 0; i < p.getFavoriteLanguages().length; i++) {
                    langStr += p.getFavoriteLanguages()[i];
                    if (i < p.getFavoriteLanguages().length - 1) {
                        langStr += ";";
                    }
                }
                return new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        langStr,
                        p.getColor(),
                        String.valueOf(p.getPosition())
                };
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String[] langs = p.getFavoriteLanguages().clone();

                for (int i = 0; i < langs.length - 1; i++) {
                    for (int j = i + 1; j < langs.length; j++) {
                        if (langs[i].compareTo(langs[j]) > 0) {
                            String temp = langs[i];
                            langs[i] = langs[j];
                            langs[j] = temp;
                        }
                    }
                }

                String langStr = "";
                for (int i = 0; i < langs.length; i++) {
                    langStr += langs[i];
                    if (i < langs.length - 1) {
                        langStr += "; ";
                    }
                }

                String state = p.getPosition() == boardSize ? "Derrotado" : "Em Jogo";

                return p.getId() + " | " + p.getName() + " | " + p.getPosition() +
                        " | " + langStr + " | " + state;
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return null;
        }

        ArrayList<Integer> ids = new ArrayList<>();
        for (Programmer p : players) {
            if (p.getPosition() == position) {
                ids.add(p.getId());
            }
        }

        for (int i = 0; i < ids.size() - 1; i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                if (ids.get(i) > ids.get(j)) {
                    int temp = ids.get(i);
                    ids.set(i, ids.get(j));
                    ids.set(j, temp);
                }
            }
        }

        String idStr = "";
        for (int i = 0; i < ids.size(); i++) {
            idStr += ids.get(i);
            if (i < ids.size() - 1) {
                idStr += ",";
            }
        }

        return new String[]{idStr};
    }

    public int getCurrentPlayerID() {
        if (players.isEmpty()) {
            return -1;
        }
        return players.get(currentPlayerIndex).getId();
    }

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (nrSpaces < 1 || nrSpaces > 6 || players.isEmpty()) {
            return false;
        }

        Programmer current = players.get(currentPlayerIndex);
        int newPos = current.getPosition() + nrSpaces;

        if (newPos > boardSize) {
            int excess = newPos - boardSize;
            newPos = boardSize - excess;
        }

        current.setPosition(newPos);
        totalTurns++;

        if (newPos != boardSize) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        return true;
    }

    public boolean gameIsOver() {
        for (Programmer p : players) {
            if (p.getPosition() == boardSize) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> result = new ArrayList<>();
        result.add("THE GREAT PROGRAMMING JOURNEY");
        result.add("");
        result.add("NR. DE TURNOS");
        result.add(String.valueOf(totalTurns));
        result.add("");
        result.add("VENCEDOR");

        Programmer winner = null;
        for (Programmer p : players) {
            if (p.getPosition() == boardSize) {
                winner = p;
                break;
            }
        }
        result.add(winner != null ? winner.getName() : "N/A");
        result.add("");
        result.add("RESTANTES");

        ArrayList<Programmer> remaining = new ArrayList<>();
        for (Programmer p : players) {
            if (winner == null || p.getId() != winner.getId()) {
                remaining.add(p);
            }
        }

        for (int i = 0; i < remaining.size() - 1; i++) {
            for (int j = i + 1; j < remaining.size(); j++) {
                Programmer a = remaining.get(i);
                Programmer b = remaining.get(j);
                if (b.getPosition() > a.getPosition() ||
                        (b.getPosition() == a.getPosition() && b.getName().compareTo(a.getName()) < 0)) {
                    remaining.set(i, b);
                    remaining.set(j, a);
                }
            }
        }

        for (Programmer p : remaining) {
            result.add(p.getName());
        }
        return result;
    }

    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(300, 300));
        panel.add(new JLabel("Nome: Rodrigo Santos"));
        panel.add(new JLabel("Número: a22410416"));
        panel.add(new JLabel("Nome: Marwan Ghunim"));
        panel.add(new JLabel("Número: a22406059"));
        return panel;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}