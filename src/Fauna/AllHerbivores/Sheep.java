package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Sheep extends Herbivores{


    public Sheep() {
        super("Sheep", 70, 140, 3, 15);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Sheep baby = new Sheep();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
