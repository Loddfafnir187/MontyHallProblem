package com.montyhall;

import javax.swing.*;

import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameWindow extends JFrame {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private JButton strButton, extButton, statButton;
    private StatWindow statWindow;
    private Map map;

    public GameWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        setTitle("Monty Hall problem");
        setResizable(false);
        
        String filename = "statistic.json";
        File statFail = new File(filename);
        if (!statFail.exists()) {
            try {
                statFail.createNewFile();
                Gson gson = new Gson();
                String json = gson.toJson(new Statistic());
                try (FileWriter writer = new FileWriter(statFail)) {
                    writer.write(json);
                } catch (IOException e) {
                    System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Не удалось создать файл: " + e.getMessage());
            }
        }

        strButton = new JButton("Сыграть");
        extButton = new JButton("Выход");
        statButton = new JButton("Статитстика");

        map = new Map();
        

        strButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {startNewGame();}
        });

        extButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {System.exit(0);}
        });

        statButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statWindow = new StatWindow(GameWindow.this);
                statWindow.setVisible(true);}
        });

        JPanel panBottom = new JPanel(new GridLayout(1, 3));
        panBottom.add(strButton);
        panBottom.add(statButton);
        panBottom.add(extButton);

        add(panBottom, BorderLayout.SOUTH);
        add(map);

        setVisible(true);
    }

    void startNewGame(){
        map.startNewGame();
    }
}
