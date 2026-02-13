package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Goat extends Herbivores{


    public Goat() {
        super("Goat", 60, 140, 3, 10);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Goat baby = new Goat();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
