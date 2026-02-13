package MapEngine;

import Fauna.Animal;
import Fauna.Herbs;

import java.util.concurrent.*;

public class Island {

    private final int width;
    private final int height;
    private final Cell[][] cells;

    // Один общий пул потоков для острова
    private final ExecutorService pool;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];

        // создаём клетки
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(new Coordinate(x, y));
            }
        }

        // создаём пул потоков под число процессоров
        this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /** Возвращает клетку по координатам */
    public Cell getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return cells[y][x];
    }

    /** Симуляция одного шага (все клетки работают параллельно) */
    public void simulateStep() {
        CompletionService<Void> completionService = new ExecutorCompletionService<>(pool);

        int taskCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                completionService.submit(() -> {
                    cell.liveCycle(this);
                    return null;
                });
                taskCount++;
            }
        }

        for (int i = 0; i < taskCount; i++) {
            try {
                completionService.take(); // ждём, пока каждая задача завершится
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /** Добавить животное в клетку по координатам */
    public void addAnimal(Animal animal, int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) {
            cell.addAnimal(animal);
            animal.setPosition(new Coordinate(x, y));
        }
    }

    /** Добавить растение в клетку по координатам */
    public void addPlant(Herbs herb, int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) {
            cell.addPlant(herb);
        }
    }

    /** Печать карты (безопасный снимок данных) */
    public void printMap() {
        System.out.println("\n===== ISLAND MAP =====");

        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();

            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                String symbol = "⬜ ";

                var animals = cell.getAnimals();
                if (!animals.isEmpty()) {
                    symbol = getAnimalSymbol(animals.get(0)) + " ";
                } else if (!cell.getPlants().isEmpty()) {
                    symbol = "🌿 ";
                }

                row.append(symbol);
            }
            System.out.println(row);
        }
    }

    /** Эмодзи для животных */
    private String getAnimalSymbol(Animal animal) {
        return switch (animal.getName()) {
            case "Rabbit" -> "🐇";
            case "Fox" -> "🦊";
            case "Wolf" -> "🐺";
            case "Boa" -> "🐍";
            case "Bear" -> "\uD83D\uDC3B";
            case "Eagle" -> "\uD83E\uDD85";
            case "Horse" -> "\uD83D\uDC0E";
            case "Deer" -> "\uD83E\uDD8C";
            case "Mouse" -> "\uD83D\uDC01";
            case "Goat" -> "\uD83D\uDC10";
            case "Sheep" -> "\uD83D\uDC11";
            case "Boar" -> "\uD83D\uDC17";
            case "Buffalo" -> "\uD83D\uDC03";
            case "Duck" -> "\uD83E\uDD86";
            case "Caterpillar" -> "\uD83D\uDC1B";
            default -> "❓";
        };
    }

    /** Геттеры для размеров */
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /** Завершить все потоки (при остановке программы) */
    public void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    }
