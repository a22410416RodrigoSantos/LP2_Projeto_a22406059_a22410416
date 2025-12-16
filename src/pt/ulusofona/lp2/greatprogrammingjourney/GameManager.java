package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private ArrayList<Programmer> players = new ArrayList<>();
    private ArrayList<Slot> slots = new ArrayList<>();
    private int boardSize = 0;
    private int currentPlayerIndex = 0;
    private int totalTurns = 0;
    private int lastNrSpaces = 0;

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
                for (int j = 0; j < langs.length; j++) {
                    langs[j] = langs[j].trim();
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

        players.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        currentPlayerIndex = 0;
        return true;
    }

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        if (!createInitialBoard(playerInfo, worldSize)) {
            return false;
        }

        if (abyssesAndTools != null) {
            for (String[] item : abyssesAndTools) {
                if (item.length != 3) {
                    return false;
                }
                try {
                    int type = Integer.parseInt(item[0].trim());
                    int id = Integer.parseInt(item[1].trim());
                    int pos = Integer.parseInt(item[2].trim());

                    if (pos < 1 || pos > boardSize) {
                        return false;
                    }

                    if (type != 0 && type != 1) {
                        return false;
                    }

                    if (type == 0) {
                        if (id < 0 || id > 9) return false;
                    } else if (type == 1) {
                        if (id < 0 || id > 5) return false;
                    }

                    Effect effect = null;
                    if (type == 0) {
                        effect = createAbismoById(id);
                    } else if (type == 1) {
                        effect = createFerramentaById(id);
                    }

                    if (effect != null) {
                        slots.get(pos - 1).setEffect(effect);
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public String reactToAbyssOrTool() {
        if (players.isEmpty() || currentPlayerIndex >= players.size()) {
            return null;
        }

        Programmer current = players.get(currentPlayerIndex);
        if (!current.isInGame()) {
            return null;
        }

        int pos = current.getPosition();
        if (pos < 1 || pos > boardSize) {
            return null;
        }

        Slot slot = slots.get(pos - 1);
        if (!slot.hasEffect()) {
            return null;
        }

        Effect effect = slot.getEffect();
        String type = effect.getType();

        if ("abismo".equals(type)) {
            Abismo abismo = (Abismo) effect;
            Ferramenta tool = current.getFerramentaQueNeutraliza(abismo);

            if (tool != null) {
                current.removeFerramenta(tool);
                return abismo.getTitle();  // Ferramenta usada → efeito anulado
            } else {
                abismo.apply(current, this);
                return abismo.getTitle();  // Efeito aplicado
            }
        } else if ("ferramenta".equals(type)) {
            Ferramenta ferramenta = (Ferramenta) effect;
            ferramenta.apply(current, this);  // Adiciona ao inventário
            slot.setEffect(null);             // Ferramenta é consumida (removida da casa)
            slot.setImageName("normal.png");
            return ferramenta.getTitle();
        }

        return null;
    }
    private Abismo createAbismoById(int id) {
        switch (id) {
            case 0: return new AbiErroSintaxe();
            case 1: return new AbiErroLogica();
            case 2: return new AbiException();
            case 3: return new AbiFileNotFoundException();
            case 4: return new AbiCrash();
            case 5: return new AbiCodigoDuplicado();
            case 6: return new AbiEfeitosSecundarios();
            case 7: return new AbiBlueScreenOfDeath();
            case 8: return new AbiCicloInfinito();
            case 9: return new AbiSegmentationFault();
            default: return null;
        }
    }

    private Ferramenta createFerramentaById(int id) {
        switch (id) {
            case 0: return new FerHeranca();
            case 1: return new FerProgramacaoFuncional();
            case 2: return new FerTestesUnitarios();
            case 3: return new FerTratamentoExcepcoes();
            case 4: return new FerIDE();
            case 5: return new FerAjudaDoProfessor();
            default: return null;
        }
    }

    public String getProgrammersInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            String info = getProgrammerInfoAsStr(players.get(i).getId());
            if (info == null) {
                info = "-1 | Unknown | 1 | No tools | | Em Jogo";
            }
            sb.append(info);
        }
        return sb.toString();
    }

    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) throw new InvalidFileException("Empty file");
            String[] parts = line.split(";");
            if (parts.length < 2) throw new InvalidFileException("Invalid header");

            int worldSize = Integer.parseInt(parts[0]);
            int numPlayers = Integer.parseInt(parts[1]);

            String[][] playerInfo = new String[numPlayers][4];
            for (int i = 0; i < numPlayers; i++) {
                line = br.readLine();
                if (line == null) throw new InvalidFileException("Missing player data");
                playerInfo[i] = line.split(";");
                if (playerInfo[i].length != 4) {
                    throw new InvalidFileException("Invalid player data");
                }
            }

            String[][] abyssesAndTools = null;
            line = br.readLine();
            if (line != null) {
                int numEffects = Integer.parseInt(line);
                if (numEffects > 0) {
                    abyssesAndTools = new String[numEffects][3];
                    for (int i = 0; i < numEffects; i++) {
                        line = br.readLine();
                        if (line == null) break;
                        abyssesAndTools[i] = line.split(";");
                        if (abyssesAndTools[i].length != 3) {
                            throw new InvalidFileException("Invalid effect data");
                        }
                    }
                }
            }

            boolean success = (abyssesAndTools != null)
                    ? createInitialBoard(playerInfo, worldSize, abyssesAndTools)
                    : createInitialBoard(playerInfo, worldSize);

            if (!success) throw new InvalidFileException("Failed to recreate board");

            line = br.readLine();
            if (line != null) currentPlayerIndex = Integer.parseInt(line);
            line = br.readLine();
            if (line != null) totalTurns = Integer.parseInt(line);

            for (int i = 0; i < numPlayers; i++) {
                line = br.readLine();
                if (line == null) break;
                String[] playerState = line.split(";");
                // 🔥 Proteção contra IndexOutOfBounds
                if (playerState.length >= 3) {
                    int id = Integer.parseInt(playerState[0]);
                    int pos = Integer.parseInt(playerState[1]);
                    String state = playerState[2];
                    for (Programmer p : players) {
                        if (p.getId() == id) {
                            p.setPosition(pos);
                            p.setState(state);
                            p.setInGame(!"Derrotado".equals(state));
                            break;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new InvalidFileException("Invalid file format: " + e.getMessage());
        }
    }

    public boolean saveGame(File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(boardSize + ";" + players.size());

            for (Programmer p : players) {
                String langStr = String.join(";", p.getFavoriteLanguages());
                pw.println(p.getId() + ";" + p.getName() + ";" + langStr + ";" + p.getColor());
            }

            int effectCount = 0;
            for (Slot s : slots) {
                if (s.hasEffect()) effectCount++;
            }
            pw.println(effectCount);

            for (int i = 0; i < slots.size(); i++) {
                Slot s = slots.get(i);
                if (s.hasEffect()) {
                    Effect e = s.getEffect();
                    int type = "abismo".equals(e.getType()) ? 0 : 1;
                    pw.println(type + ";" + e.getId() + ";" + (i + 1));
                }
            }

            pw.println(currentPlayerIndex);
            pw.println(totalTurns);

            for (Programmer p : players) {
                StringBuilder toolIds = new StringBuilder();
                for (Ferramenta f : p.getFerramentas()) {
                    if (toolIds.length() > 0) toolIds.append(",");
                    toolIds.append(f.getId());
                }
                pw.println(p.getId() + ";" + p.getPosition() + ";" + p.getState() + ";" + toolIds);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Programmer> getPlayers() {
        return players;
    }

    public boolean isValidColor(String color) {
        return "Purple".equals(color) || "Blue".equals(color) ||
                "Green".equals(color) || "Brown".equals(color);
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize || slots.isEmpty()) {
            return "normal.png";
        }
        Slot slot = slots.get(nrSquare - 1);
        String img = slot.getCurrentImageName();
        return (img != null && !img.isEmpty()) ? img : "normal.png";
    }

    public String[] getProgrammerInfo(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String[] langs = p.getFavoriteLanguages();
                if (langs == null) langs = new String[0];
                StringBuilder langStr = new StringBuilder();
                for (int i = 0; i < langs.length; i++) {
                    if (i > 0) langStr.append(";");
                    langStr.append(langs[i]);
                }
                return new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        langStr.toString(),
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
                // Linguagens ordenadas alfabeticamente
                String[] langs = p.getFavoriteLanguages().clone();
                java.util.Arrays.sort(langs);
                StringBuilder langStr = new StringBuilder();
                for (int i = 0; i < langs.length; i++) {
                    if (i > 0) langStr.append("; ");
                    langStr.append(langs[i]);
                }

                // Ferramentas ordenadas pelo título
                ArrayList<Ferramenta> tools = new ArrayList<>(p.getFerramentas());
                tools.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
                StringBuilder toolsStr = new StringBuilder();
                for (int i = 0; i < tools.size(); i++) {
                    if (i > 0) toolsStr.append("; ");
                    toolsStr.append(tools.get(i).getTitle());
                }
                String toolsText = toolsStr.length() == 0 ? "No tools" : toolsStr.toString();

                // Estado: apenas "Derrotado" se não estiver mais em jogo (inclui vencedor)
                String state = p.isInGame() ? "Em Jogo" : "Derrotado";

                return p.getId() + " | " + p.getName() + " | " + p.getPosition() +
                        " | " + toolsText + " | " + langStr + " | " + state;
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return new String[]{"", "", ""};
        }

        ArrayList<Integer> ids = new ArrayList<>();
        for (Programmer p : players) {
            if (p.getPosition() == position && p.isInGame()) {
                ids.add(p.getId());
            }
        }

        ids.sort(Integer::compareTo);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(ids.get(i));
        }

        String[] result = new String[3];
        result[0] = sb.toString();
        result[1] = "";  // Abismo
        result[2] = "";  // Ferramenta

        Slot slot = slots.get(position - 1);
        if (slot.hasEffect()) {
            Effect e = slot.getEffect();
            if ("abismo".equals(e.getType())) {
                result[1] = e.getTitle();
            } else if ("ferramenta".equals(e.getType())) {
                result[2] = e.getTitle();
            }
        }

        return result;
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

        // 🔥 Restrições por linguagem
        if (current.getFavoriteLanguages().length > 0) {
            String firstLang = current.getFavoriteLanguages()[0];
            if ("Assembly".equals(firstLang) && nrSpaces > 2) {
                return false;
            }
            if ("C".equals(firstLang) && nrSpaces > 3) {
                return false;
            }
        }

        int newPos = current.getPosition() + nrSpaces;

        if (newPos > boardSize) {
            int excess = newPos - boardSize;
            newPos = boardSize - excess;
        }

        current.setPosition(newPos);
        totalTurns++;
        lastNrSpaces = nrSpaces;

        if (newPos != boardSize) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            // Chegou à última casa → vencedor (mas estado = Derrotado)
            current.setInGame(false);
            current.setState("Derrotado");
        }

        reactToAbyssOrTool();
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

        ArrayList<Programmer> remaining = new ArrayList<>(players);
        remaining.removeIf(p -> p.getPosition() == boardSize);

        // Ordenação: maior posição → menor posição, desempate por nome ascendente
        remaining.sort((a, b) -> {
            if (b.getPosition() != a.getPosition()) {
                return Integer.compare(b.getPosition(), a.getPosition());
            }
            return a.getName().compareTo(b.getName());
        });

        for (Programmer p : remaining) {
            result.add(p.getName() + " " + p.getPosition());
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

    public int getLastNrSpaces() {
        return lastNrSpaces;
    }
}