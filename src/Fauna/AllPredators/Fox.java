package Fauna.AllPredators;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Fox extends Predators {

    public Fox() {
        super("Fox", 8, 30, 2, 2);
        setMaxAge(40);
    }

    @Override
    protected double getEatChance(Animal prey) {
        return switch (prey.getName()) {
            case "Rabbit" -> 0.8;
            case "Mouse" -> 0.7;
            case "Goat", "Sheep" -> 0.5;
            default -> 0.0;
        };
    }

    @Override
    protected Animal createChild() {
        Fox baby = new Fox();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
