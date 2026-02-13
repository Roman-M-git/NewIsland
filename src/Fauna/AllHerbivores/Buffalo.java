package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Buffalo extends  Herbivores{


    public Buffalo() {
        super("Buffalo", 700, 10, 3, 100);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Buffalo baby = new Buffalo();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
