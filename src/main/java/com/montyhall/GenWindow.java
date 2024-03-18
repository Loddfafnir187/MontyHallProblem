package com.montyhall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class GenWindow extends JFrame{
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    private JButton extButton, genButton;
    JPanel panel1, panel2, panel3;
    JLabel label1, label2, label3;
    Statistic stat;
    Generation gen = new Generation();

    private JPanel createChart(Statistic statistic){
        JPanel panel3 = new JPanel();
        // Создаем данные для графика
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(statistic.firstResult(), "1", "НЕ Менять выбор");
        dataset.addValue(statistic.secondResult(), "2", "ПОМЕНЯТЬ выбор");

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

    public GenWindow(final StatWindow statWindow) {
        setLocationRelativeTo(statWindow);
        setLocation(getX() - WIDTH/2, getY() - HEIGHT/2+30);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        
        panel1 = new JPanel(new GridLayout(1, 2));
        panel2 = new JPanel(new GridLayout(4, 1));
        panel3 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        genButton = new JButton("Сгенерировать заново");
        extButton = new JButton("Закрыть");
        stat = gen.gen(1000);
        
        extButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenWindow.this.dispose();
            }
        });

        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenWindow.this.getContentPane().repaint();
            }
        });


        panel1.add(genButton);
        panel1.add(extButton);
        
        label1.setText("Всего игр: " + stat.getAmount());
        label2.setText("Побед, если не менять выбор: " + stat.firstResult() + "%" );
        label3.setText("Побед, если менять выбор: " + stat.secondResult() + "%" );
        panel2.add(label1);
        panel2.add(label2);
        panel2.add(label3);
        
        add(panel2, BorderLayout.NORTH);
        add(panel1, BorderLayout.SOUTH);
        add(createChart(stat), BorderLayout.CENTER);
    }
}