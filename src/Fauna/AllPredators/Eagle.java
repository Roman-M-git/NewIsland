package Fauna.AllPredators;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Eagle extends Predators{


    public Eagle() {
        super("Eagle", 6, 20, 3, 1);
        setMaxAge(50);
    }

    @Override
    protected double getEatChance(Animal prey) {
        // Упрощённая таблица вероятностей
        return switch (prey.getName()) {
            case "Rabbit","Mouse" -> 0.9;
            case "Fox" -> 0.1;
            default -> 0.0;
        };
    }

    @Override
    protected Animal createChild() {
        Eagle baby = new Eagle();
        // Копируем позицию родителя, если она установлена
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
