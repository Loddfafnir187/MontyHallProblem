package com.montyhall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import com.google.gson.Gson;

public class StatWindow extends JFrame{
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    private JButton strButton, genButton;
    JPanel panel1, panel2;
    JLabel label1, label2, label3, label4;
    Statistic stat;
    Gson gson = new Gson();
    

    public void readData() {
        try (FileReader reader = new FileReader("statistic.json")) {
            stat = gson.fromJson(reader, Statistic.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel createChart(){
        JPanel panel3 = new JPanel();
        // Создаем данные для графика
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        readData();
        dataset.addValue(stat.firstResult(), "1", "НЕ Менять выбор");
        dataset.addValue(stat.secondResult(), "2", "ПОМЕНЯТЬ выбор");

        // Создаем график
        JFreeChart chart = ChartFactory.createBarChart(
                "Результаты",
                "Выбор",
                "Процент побед",
                dataset
        );

        // Получаем объект оси значений графика
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        // Устанавливаем максимальное значение для оси значений
        rangeAxis.setUpperBound(100); 

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 135));
        panel3.add(chartPanel);

        return panel3;
    }

    public StatWindow(final GameWindow gameWindow) {
        
        setLocationRelativeTo(gameWindow);
        setLocation(getX() - WIDTH/2, getY() - HEIGHT/2+30);
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        panel1 = new JPanel(new GridLayout(1, 2));
        panel2 = new JPanel(new GridLayout(4, 1));
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        strButton = new JButton("Начать играть");
        genButton = new JButton("Сгенерировать статистику");
    
        strButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                gameWindow.startNewGame();
            }
        });

        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenWindow genWindow = new GenWindow(StatWindow.this);
                genWindow.setVisible(true);}
        });


        panel1.add(strButton);
        panel1.add(genButton);


        readData();
        label1.setText("Всего игр: " + stat.getAmount());
        label2.setText("Побед: " + stat.getWins());
        label3.setText("Побед с первоначальным выбором: " + stat.getPick1() + " ("+ stat.firstResult() + "%)");
        label4.setText("Побед с измененым выбором: " + stat.getPick2() + " (" + stat.secondResult() + "%)");

        panel2.add(label1);
        panel2.add(label2);
        panel2.add(label3);
        panel2.add(label4);
        

        add(panel2, BorderLayout.NORTH);
        add(panel1, BorderLayout.SOUTH);
        add(createChart(), BorderLayout.CENTER);

    }
    

}
