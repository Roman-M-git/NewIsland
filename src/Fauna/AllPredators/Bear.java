package Fauna.AllPredators;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Bear extends Predators{


    public Bear() {
        super("Bear", 500, 5, 2, 80);
        setMaxAge(50);
    }

    @Override
    protected double getEatChance(Animal prey) {
        // Упрощённая таблица вероятностей
        return switch (prey.getName()) {
            case  "Mouse"-> 0.9;
            case "Rabbit","Boa","Deer" -> 0.8;
            case "Goat", "Sheep" -> 0.7;
            case "Horse" -> 0.4;
            default -> 0.0;
        };
    }

    @Override
    protected Animal createChild() {
        Bear baby = new Bear();
        // Копируем позицию родителя, если она установлена
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
