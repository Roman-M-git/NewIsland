package Fauna;

import Fauna.AllHerbivores.*;
import Fauna.AllPredators.*;
import MapEngine.Island;
import SupportClasses.Statistics;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // 🏝️ Создаём остров
        Island island = new Island(100, 20);
        Random random = new Random();

        // 🔄 Сбрасываем старую статистику
        Statistics.reset();

        // 🌿 Добавляем траву
        addHerbs(island, 200, random);

        // 🐾 Добавляем животных
        addAnimals(island, 30, Wolf.class, random, "🐺");
        addAnimals(island, 150, Rabbit.class, random, "🐇");
        addAnimals(island, 30, Fox.class, random, "🦊");
        addAnimals(island, 30, Boa.class, random, "🐍");
        addAnimals(island, 5, Bear.class, random, "🐻");
        addAnimals(island, 20, Eagle.class, random, "🦅");
        addAnimals(island, 20, Horse.class, random, "🐎");
        addAnimals(island, 20, Deer.class, random, "🦌");
        addAnimals(island, 100, Mouse.class, random, "🐁");
        addAnimals(island, 140, Goat.class, random, "🐐");
        addAnimals(island, 140, Sheep.class, random, "🐑");
        addAnimals(island, 50, Boar.class, random, "🐗");
        addAnimals(island, 50, Buffalo.class, random, "🐃");
        addAnimals(island, 50, Duck.class, random, "🦆");
        addAnimals(island, 50, Caterpillar.class, random, "🐛");

        // 🕒 Симуляция 10 шагов
        for (int i = 1; i <= 1000; i++) {
            System.out.println("\n===== Step " + i + " =====");

            // Сбрасываем счётчик травы перед шагом
            Statistics.resetGrassCounter();

            // 🚀 Запускаем шаг симуляции
            island.simulateStep();

            // 📊 Выводим статистику
            Statistics.print();

            // 🗺️ Отображаем карту
            island.printMap();

            // 💀 Подсчёт смертей
            System.out.println("💀 " + Statistics.getDeathsThisStep() + " animals died this step.");

            // 🌱 Регенерация травы
            regenerateGrass(island, 100, random);
        }

        // 🧾 Финальный отчёт
        System.out.println("\n===== FINAL SUMMARY =====");
        Statistics.print();
        System.out.println("=========================");

        // 🔚 Завершаем пул потоков
        island.shutdown();
    }

    // 🌿 Метод для добавления травы
    private static void addHerbs(Island island, int count, Random random) {
        for (int i = 0; i < count; i++) {
            Herbs herbs = new Herbs();
            int x = random.nextInt(island.getWidth());
            int y = random.nextInt(island.getHeight());
            island.addPlant(herbs, x, y);
        }
        System.out.println("🌿 Added " + count + " grass patches to the map.");
    }

    // 🌱 Метод для регенерации травы каждый шаг
    private static void regenerateGrass(Island island, int count, Random random) {
        for (int i = 0; i < count; i++) {
            Herbs herbs = new Herbs();
            int x = random.nextInt(island.getWidth());
            int y = random.nextInt(island.getHeight());
            island.addPlant(herbs, x, y);
        }
        System.out.println("🌱 Grass regrew by " + count + " patches.");
    }

    // 🐾 Метод для добавления животных
    private static void addAnimals(Island island, int count, Class<? extends Animal> animalClass, Random random, String emoji) {
        for (int i = 0; i < count; i++) {
            try {
                Animal animal = animalClass.getDeclaredConstructor().newInstance();
                int x = random.nextInt(island.getWidth());
                int y = random.nextInt(island.getHeight());
                island.addAnimal(animal, x, y);
                Statistics.registerAnimal(animal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(emoji + " Added " + count + " " + animalClass.getSimpleName() + "(s) to the map.");
    }
}
