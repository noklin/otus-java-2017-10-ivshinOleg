package com.noklin;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Launcher {

    public static void main(String[] args) throws IOException {
        calcTime(() -> lottery());
    }

    private static void calcTime(Runnable runnable) {
        long startTime = System.nanoTime();
        for (int i = 0; i < 1; i++)
            runnable.run();
        long finishTime = System.nanoTime();
        long timeNs = (finishTime - startTime) / 1;
        System.out.println("Time spent: " + timeNs + "ns (" + timeNs / 1_000_000 + "ms)");
    }

    private static void lottery() {
        String salt = "MovieMakingTeamAAA\u200Bу них hash'ы\uFEFF не будут совпадать всё равно)\n" +
                "\n" +
                "Владимир Капинос\u200Bнет\uFEFF\n" +
                "\n" +
                "Ольга Афанасьева\u200B)\uFEFF\n" +
                "\n" +
                "Дмитрий Дмитриевич\u200BЕсли пройти тесты\uFEFF с разных ящиков увеличит вероятность халявы\n" +
                "\n" +
                "Евгений Меднов\u200Bах Дмитрий Дмитриевич\uFEFF )";

        List<String> result = new ArrayList<>();
        Launcher.class.getClassLoader()
                .getResourceAsStream("/emails.csv");

        new BufferedReader(new InputStreamReader(Launcher.class.getClassLoader().getResourceAsStream("emails.csv"))).lines()
                .map(line -> line + "\t" + salt)
                .sorted(Comparator.comparingLong(String::hashCode))
                .map(line -> line.hashCode() + "\t" + line.replace(salt, ""))
                .forEach(line -> result.add(line));

        Collections.shuffle(result);
        ArrayList<String> reversedResult = new ArrayList<>(Lists.reverse(result));
        reversedResult.forEach(System.out::println);
    }
}