package Fauna.AllPredators;

import Fauna.AllHerbivores.Herbivores;
import Fauna.Animal;
import SupportClasses.Statistics;

import java.util.List;
import java.util.Random;

public abstract class Predators extends Animal {

    protected final Random random = new Random();

    public Predators(String name, double maxWeight, int maxCountOnCell, int speed, double foodNeed) {
        super(name, maxWeight, maxCountOnCell, speed, foodNeed);
    }

    /**
     * Логика кормления хищника: ищет травоядных в списке и ест.
     */
    @Override
    public void eat(List<Animal> others) {
        for (Animal target : others) {
            if (target instanceof Herbivores && target.isAlive()) {
                double chance = getEatChance(target);
                if (random.nextDouble() < chance) {
//                    System.out.println(getName() + " attacked and ate the " + target.getName());
                    setHunger(1.0);      // полностью сытый
                    target.setAlive(false);

                    // травоядное помечаем как съеденное
                    Statistics.markDeath(target, true);

                    // хищник увеличивает счетчик съеденных животных
                    Statistics.markAnimalEatenByPredator(this);

                    return;
                }
            }
        }

        // если никого не съел — становится голоднее
        setHunger(getHunger() - 0.2);
    }

    /**
     * Вероятность съесть конкретного травоядного.
     * Каждый вид хищника должен переопределить этот метод.
     */
    protected abstract double getEatChance(Animal prey);

    /**
     * Метод для создания потомства конкретного вида хищника.
     */
    protected abstract Animal createChild();
}
