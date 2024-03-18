package com.montyhall;

import java.util.Random;

import lombok.Data;

@Data
public class Generation {


    public Statistic gen(int num) {
        Random rnd = new Random();
        int numOfFirstPick = 0;
        int numOfSecondPick = 0;
        
        
        // Прогоняем NUM игр
        for (int i = 1; i <= num; i++) {
            // Создаем массив закрытых дверей
            int[] doors = {0, 0, 0};
            // Устанавливаем выигрышную дверь
            doors[rnd.nextInt(3)] = 2;
            // Генерируем ответ игрока
            int chois = rnd.nextInt(3);
            
            // Открываем проигрышную дверь
            for (int j = 0; j < doors.length; j++) {
                if (!(doors[j] == 2) && !(j == chois)) {
                    doors[j] = 1;
                }
            }

            // Записываем результат, если оставить выбор, и, если изменить
            if (doors[chois] == 2) {
                numOfFirstPick++;
            } else {
                numOfSecondPick++;
            }
        }

        return new Statistic(num, numOfFirstPick, numOfSecondPick, 0);
    }
}
