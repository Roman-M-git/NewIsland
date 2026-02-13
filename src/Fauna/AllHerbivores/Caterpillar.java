package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Caterpillar extends Herbivores{


    public Caterpillar() {
        super("Caterpillar", 0.01, 1000, 0, 0);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Caterpillar baby = new Caterpillar();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
