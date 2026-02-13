package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Horse extends Herbivores{


    public Horse() {
        super("Horse", 400, 20, 4, 60);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Horse baby = new Horse();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
