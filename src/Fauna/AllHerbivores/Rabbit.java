package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Rabbit extends Herbivores {

    public Rabbit() {
        super("Rabbit", 2, 150, 2, 0.45);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Rabbit baby = new Rabbit();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
