package MapEngine;

import Fauna.Animal;
import Fauna.Herbs;
import Fauna.AllHerbivores.Herbivores;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Cell {

    private final Coordinate coordinate;                     // координаты клетки
    private final List<Animal> animals = new ArrayList<>();   // животные в клетке
    private final List<Herbs> plants = new ArrayList<>();     // растения в клетке
    private final Queue<Animal> movedIn = new ConcurrentLinkedQueue<>(); // очередь прибывших

    private final ReentrantLock lock = new ReentrantLock();   // 🔒 для синхронизации

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /** Добавить животное в клетку (безопасно для многопоточности) */
    public void addAnimal(Animal animal) {
        if (animal == null) return;

        lock.lock();
        try {
            long sameTypeCount = animals.stream()
                    .filter(a -> a.getClass() == animal.getClass())
                    .count();

            if (sameTypeCount < animal.getMaxCountOnCell()) {
                animals.add(animal);
                animal.setPosition(coordinate);
            }
        } finally {
            lock.unlock();
        }
    }

    /** Используется другими потоками для прибытия животных */
    public void enqueueIncoming(Animal animal) {
        if (animal != null) movedIn.add(animal);
    }

    /** Добавить растение (потокобезопасно) */
    public void addPlant(Herbs herb) {
        if (herb != null) {
            lock.lock();
            try {
                plants.add(herb);
            } finally {
                lock.unlock();
            }
        }
    }

    public List<Animal> getAnimals() {
        lock.lock();
        try {
            return new ArrayList<>(animals); // возвращаем копию
        } finally {
            lock.unlock();
        }
    }

    public List<Herbs> getPlants() {
        lock.lock();
        try {
            return new ArrayList<>(plants);
        } finally {
            lock.unlock();
        }
    }

    /** Один цикл жизни клетки (выполняется в отдельном потоке) */
    public void liveCycle(Island island) {
        lock.lock();
        try {
            // 1️⃣ Еда
            List<Animal> snapshot = new ArrayList<>(animals);
            for (Animal animal : snapshot) {
                if (!animal.isAlive()) continue;

                if (animal instanceof Herbivores herbivore) {
                    boolean ate = herbivore.tryEatPlants(plants);
                    if (!ate) {
                        herbivore.eat(snapshot);
                    }
                } else {
                    animal.eat(snapshot);
                }
            }

            // 2️⃣ Размножение
            List<Animal> newborns = new ArrayList<>();
            Map<Class<?>, List<Animal>> byType = animals.stream()
                    .filter(Animal::isAlive)
                    .collect(Collectors.groupingBy(Object::getClass));

            for (List<Animal> group : byType.values()) {
                for (Animal parent : group) {
                    Animal child = parent.reproduce(group);
                    if (child != null) {
                        child.setPosition(coordinate); // сразу ставим координаты
                        newborns.add(child);
                    }
                }
            }
            for (Animal baby : newborns) addAnimal(baby);

            // 3️⃣ Передвижение — только решение, без изменения чужих клеток
            List<Animal> toMove = new ArrayList<>();
            for (Animal animal : new ArrayList<>(animals)) {
                if (!animal.isAlive()) continue;

                Coordinate oldPos = animal.getPosition();
                animal.move(island);
                Coordinate newPos = animal.getPosition();

                if (oldPos.x != newPos.x || oldPos.y != newPos.y) {
                    toMove.add(animal);
                }
            }

            // Убираем уехавших
            animals.removeAll(toMove);

            // Передаём их в новые клетки через очередь
            for (Animal moving : toMove) {
                Cell target = island.getCell(moving.getPosition().x, moving.getPosition().y);
                if (target != null) {
                    target.enqueueIncoming(moving);
                }
            }

            // 4️⃣ Старение и смерть
            Iterator<Animal> iterator = animals.iterator();
            while (iterator.hasNext()) {
                Animal a = iterator.next();
                a.liveCycle(island);
                if (!a.isAlive()) {
                    iterator.remove();
                }
            }

            // 5️⃣ Приём прибывших животных (в конце фазы)
            Animal arrived;
            while ((arrived = movedIn.poll()) != null) {
                addAnimal(arrived);
            }

        } finally {
            lock.unlock();
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
