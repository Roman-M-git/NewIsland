package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Duck extends Herbivores{


    public Duck() {
        super("Duck", 1, 200, 4, 0.15);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Duck baby = new Duck();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
