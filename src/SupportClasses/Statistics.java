package SupportClasses;

import Fauna.Animal;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class Statistics {

    public static class StatRecord {
        public int left = 0;       // живых животных
        public int rip = 0;        // умерших животных
        public int reproduced = 0; // потомства
        public int eaten = 0;      // съедено животных
        public int ateGrass = 0;   // съедено травы
        public int ateAnimals = 0; // сколько животных съел хищник
    }

    // Основная статистика по видам
    private static final Map<String, StatRecord> stats = new IdentityHashMap<>();
    private static int deathsLastStep = 0;
    private static int previousTotalDeaths = 0;


    // Набор уже зарегистрированных объектов (уникальных экземпляров животных)
    private static final Set<Animal> registered = Collections.newSetFromMap(new IdentityHashMap<>());

    /** Полный сброс статистики */
    public static void reset() {
        stats.clear();
        registered.clear();
    }

    /** Регистрируем животное только один раз */
    public static void registerAnimal(Animal animal) {
        if (!registered.add(animal)) return; // уже зарегистрировано, пропускаем

        stats.putIfAbsent(animal.getName(), new StatRecord());
        stats.get(animal.getName()).left++;
    }

    /** Отмечаем смерть животного */
    public static void markDeath(Animal animal, boolean eaten) {
        // удаляем из набора зарегистрированных
        registered.remove(animal);

        StatRecord record = stats.computeIfAbsent(animal.getName(), n -> new StatRecord());
        record.left = Math.max(0, record.left - 1);
        record.rip++;
        if (eaten && canBeEaten(animal.getName())) {
            record.eaten++;
        }
    }

    /** Отмечаем появление потомка */
    public static void markReproduce(Animal child) {
        // потомок — это новый объект, его тоже регистрируем
        registerAnimal(child);

        StatRecord record = stats.computeIfAbsent(child.getName(), n -> new StatRecord());
        record.reproduced++;
    }

    /** Травоядное съело траву */
    public static void markGrassEaten(Animal animal) {
        if (!isHerbivore(animal.getName())) return;
        StatRecord record = stats.computeIfAbsent(animal.getName(), n -> new StatRecord());
        record.ateGrass++;
    }

    /** Хищник съел животное */
    public static void markAnimalEatenByPredator(Animal predator) {
        if (!predatorEatsAnimals(predator.getName())) return;
        StatRecord record = stats.computeIfAbsent(predator.getName(), n -> new StatRecord());
        record.ateAnimals++;
    }

    /** Обнуляем количество съеденной травы (например, раз в шаг) */
    public static void resetGrassCounter() {
        for (StatRecord record : stats.values()) {
            record.ateGrass = 0;
        }
    }

    /** Вывод всей статистики по видам */
    public static void print() {
        for (Map.Entry<String, StatRecord> entry : stats.entrySet()) {
            String name = entry.getKey();
            StatRecord s = entry.getValue();

            if (isHerbivore(name)) {
                System.out.printf("%s: left - %d, RIP - %d, reproduced - %d, eaten - %d, ate grass - %d%n",
                        name, s.left, s.rip, s.reproduced, s.eaten, s.ateGrass);
            } else if (predatorEatsAnimals(name) && !canBeEaten(name)) {
                System.out.printf("%s: left - %d, RIP - %d, reproduced - %d, ate animals - %d%n",
                        name, s.left, s.rip, s.reproduced, s.ateAnimals);
            } else {
                System.out.printf("%s: left - %d, RIP - %d, reproduced - %d, eaten - %d, ate animals - %d%n",
                        name, s.left, s.rip, s.reproduced, s.eaten, s.ateAnimals);
            }
        }
    }

    // --- вспомогательные методы определения типа животного ---

    private static boolean isHerbivore(String name) {
        return switch (name) {
            case "Rabbit", "Mouse", "Goat", "Sheep","Horse","Deer","Boar","Buffalo","Duck","Caterpillar" -> true;
            default -> false;
        };
    }

    private static boolean canBeEaten(String name) {
        return switch (name) {
            case "Rabbit", "Mouse", "Goat", "Sheep","Horse","Deer","Boar","Buffalo","Duck","Caterpillar", "Fox","Boa" -> true;
            default -> false;
        };
    }

    private static boolean predatorEatsAnimals(String name) {
        return switch (name) {
            case "Wolf", "Fox", "Boa","Bear","Eagle" -> true;
            default -> false;
        };
    }
    public static int getTotalDeaths() {
        int total = 0;
        for (StatRecord record : stats.values()) {
            total += record.rip;
        }
        return total;
    }

    public static int getDeathsThisStep() {
        int totalNow = getTotalDeaths();
        deathsLastStep = totalNow - previousTotalDeaths;
        previousTotalDeaths = totalNow;
        return Math.max(deathsLastStep, 0);
    }

}
