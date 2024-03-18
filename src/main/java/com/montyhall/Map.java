package com.montyhall;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.google.gson.Gson;

public class Map extends JPanel{
    private static final Random rnd = new Random();
    ImageIcon closedDoorIcon = new ImageIcon("./Pics/ClosedDoor.png"); 
    ImageIcon openedDoorIcon = new ImageIcon("./Pics/OpenedDoor.png"); 
    ImageIcon winDoorIcon = new ImageIcon("./Pics//WinDoor.png"); 
    
    // ИГРА ИДЕТ
    public static final int GAME_STATE = 0; 
    // ПОБЕДА
    public static final int WIN_STATE = 1;
    // ПОТРАЧЕНО
    public static final int LOSE_STATE = 2;
 
    // ПОБЕДА
    public static final String MSG_WIN_STATE = "Вы победили!!!";
    // ПОТРАЧЕНО
    public static final String MSG_LOSE_STATE = "Вы проиграли :(";

    // private int mode;
    private int width, height, cellWidth, cellHeight, padding;
    private int gameStateType;
    private boolean secondChanceState;
    private Door[] field;
    private boolean gameWork;
    private int firstChoice;
    private int secondChoise;
    Gson gson = new Gson();
    Statistic stat = new Statistic();

    Map() {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameWork) {
                    update(e);
                }
            }
        });
    }

    private void initMap(){
        field = new Door[3];
        for (int i = 0; i < field.length; i++) {
            field[i] = new Door(0, false);
        }
        field[rnd.nextInt(3)].setWinDoor(true);
    }

    public void startNewGame() {
        try (FileReader reader = new FileReader("statistic.json")) {
            Gson gson = new Gson();
            stat = gson.fromJson(reader, Statistic.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initMap();
        gameWork = true;
        gameStateType = GAME_STATE;
        secondChanceState = false;        

        repaint();
    }

    private void update(MouseEvent e) {
        int clickedX = e.getX();
        int clickedY = e.getY();
        int cellIndex = getCellIndex(clickedX, clickedY);
        if (cellIndex == -1 || field[cellIndex].getDoorState() == 1) {
            return;
        }
        if (!secondChanceState) {
            firstChoice = cellIndex;
        } else {
            secondChoise = cellIndex;
        }
        if (checkEndGame(field[cellIndex], secondChanceState))
            return;
        aiTurn(cellIndex);
        repaint();
    }

    private int getCellIndex(int x, int y) {
        // Определить индекс клетки по координатам x и y
        for (int i = 0; i < 3; i++) {
            int cellX = padding + i * (cellWidth + padding);
            int cellY = padding;
            if (x >= cellX && x <= cellX + cellWidth && y >= cellY && y <= cellY + cellHeight) {
                return i;
            }
        }
        return -1; // Если нажатие было между клетками
    }

    private void aiTurn(int pickedDoor) {
        for (int i = 0; i < field.length; i++) {
            if (!field[i].isWinDoor() && !(i == pickedDoor)) {
                field[i].setDoorState(1);
                return;
            }
        }
        return;
    }

    private boolean checkEndGame(Door door, boolean secondChanceState) {
        if (secondChanceState && checkWin(door)) {
            this.gameStateType = WIN_STATE;
            door.setDoorState(2);
            repaint();
            return true;
        } else if (secondChanceState) {
            this.gameStateType = LOSE_STATE;
            door.setDoorState(1);
            repaint();
            return true;
        }
        this.secondChanceState = true;
        return false;
    }
    
    private boolean checkWin(Door door) {
        return door.isWinDoor();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameWork) {
            render(g);
        }
    }

    private void render (Graphics g) {
        width = getWidth();
        height = getHeight();
        padding = 20;
        cellHeight = 350;
        cellWidth = 175;
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < 3; i++) {
            ImageIcon icon = new ImageIcon();
            switch (field[i].getDoorState()) {
                case 0:
                    icon = createImageIcon("./Pics/ClosedDoor.png");
                    break;
                case 1:
                    icon = createImageIcon("./Pics/OpenedDoor.png");
                    break;
                case 2:
                    icon = createImageIcon("./Pics/winDoor.png");
                    break;
                default:
                    break;
            }
            Image image = icon.getImage();
            g2d.drawImage(image, padding + i * (cellWidth + padding), padding, cellWidth, cellHeight, null);
        }

        if (gameStateType != GAME_STATE) {
            showMessage(g);
        }
    }

    private void showMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() / 2, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        stat.addAmount();
        switch (gameStateType) {
            case WIN_STATE:
                g.drawString(MSG_WIN_STATE, 180, getHeight() / 2 + 60);
                if (firstChoice == secondChoise) {
                    stat.setPick1(stat.getPick1() + 1);
                } else {
                    stat.setPick2(stat.getPick2() + 1);
                }
                stat.addWin();
                break;
            case LOSE_STATE:
                g.drawString(MSG_LOSE_STATE, 180, getHeight() / 2 + 60);
                break;
            default:
                throw new RuntimeException("UNCHECKED GAME_STATE: " + gameStateType);
        }
        try (FileWriter writer = new FileWriter("Statistic.json")) {
            writer.write(gson.toJson(stat));
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
        }

        gameWork = false;
    }

    private ImageIcon createImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
