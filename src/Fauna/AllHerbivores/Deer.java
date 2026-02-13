package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Deer extends Herbivores{


    public Deer() {
        super("Deer", 300, 20, 4, 50);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Deer baby = new Deer();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
