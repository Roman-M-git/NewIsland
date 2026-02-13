package Fauna;

import MapEngine.Coordinate;
import MapEngine.Island;
import SupportClasses.Statistics;

import java.util.List;
import java.util.Random;

public abstract class Animal {
    // ==== Основные характеристики ====
    protected String name;
    protected double weight;
    protected double maxWeight;
    protected int maxCountOnCell;
    protected int speed;
    protected double foodNeed;
    protected boolean alive = true;

    // ==== Состояние ====
    protected double hunger;  // 1.0 = сыт, 0.0 = умирает
    protected int age = 0;
    protected int maxAge;
    protected Coordinate position;

    protected final Random random = new Random();

    // ==== Конструктор ====
    public Animal(String name, double maxWeight, int maxCountOnCell, int speed, double foodNeed) {
        this.name = name;
        this.maxWeight = maxWeight;
        this.weight = maxWeight;
        this.maxCountOnCell = maxCountOnCell;
        this.speed = speed;
        this.foodNeed = foodNeed;
        this.hunger = 1.0;

        // ✅ Регистрируем животное в статистике
        Statistics.registerAnimal(this);
    }

    // ==== Логика жизни ====

    /** Один шаг жизни животного */
    public void liveCycle(Island island) {
        if (!alive) return;

        age++;
        decreaseHunger();
        move(island);
        deathCheck();
    }

    /** Уменьшаем уровень сытости каждый шаг */
    protected void decreaseHunger() {
        hunger -= 0.1; // теряет 10% сытости за шаг
        if (hunger < 0) hunger = 0;
    }

    /** Проверка смерти от старости или голода */
    protected void deathCheck() {
        if (!alive) return;

        if (hunger <= 0) {
            alive = false;
            Statistics.markDeath(this, false);
           // System.out.println(name + " died of hunger ☠️");
        } else if (age >= maxAge) {
            alive = false;
            Statistics.markDeath(this, false);
           // System.out.println(name + " died of old age 🕯️");
        }
    }

    // ==== Основные действия ====

    /** Двигается в случайном направлении */
    public void move(Island island) {
        if (!alive || position == null) return;

        int dx = random.nextInt(speed * 2 + 1) - speed;
        int dy = random.nextInt(speed * 2 + 1) - speed;

        int newX = Math.max(0, Math.min(island.getWidth() - 1, position.x + dx));
        int newY = Math.max(0, Math.min(island.getHeight() - 1, position.y + dy));

        position = new Coordinate(newX, newY);
    }

    /** Еда — реализуется в наследниках */
    public abstract void eat(List<Animal> others);

    /** Размножение — если рядом есть партнёр */
    public Animal reproduce(List<Animal> sameSpecies) {
        if (!alive || sameSpecies.size() < 2) return null;

        if (random.nextDouble() < 0.3) { // шанс 30%
            Animal child = createChild();

            // ✅ ставим ту же позицию, что у родителя
            if (this.position != null) {
                child.setPosition(new Coordinate(this.position.x, this.position.y));
            }

            // ✅ регистрируем в статистике
            Statistics.markReproduce(child);

            return child;
        }
        return null;
    }

    /** Абстрактный метод создания детёныша */
    protected abstract Animal createChild();

    // ==== Геттеры и сеттеры ====

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public void setMaxCountOnCell(int maxCountOnCell) {
        this.maxCountOnCell = maxCountOnCell;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getFoodNeed() {
        return foodNeed;
    }

    public void setFoodNeed(double foodNeed) {
        this.foodNeed = foodNeed;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
