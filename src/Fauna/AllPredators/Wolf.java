package Fauna.AllPredators;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Wolf extends Predators {

    public Wolf() {
        super("Wolf", 50, 30, 3, 8);
        setMaxAge(50);
    }

    @Override
    protected double getEatChance(Animal prey) {
        // Упрощённая таблица вероятностей
        return switch (prey.getName()) {
            case "Rabbit" -> 0.6;
            case "Mouse" -> 0.8;
            case "Goat", "Sheep" -> 0.7;
            default -> 0.0;
        };
    }

    @Override
    protected Animal createChild() {
        Wolf baby = new Wolf();
        // Копируем позицию родителя, если она установлена
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
