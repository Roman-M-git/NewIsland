package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Mouse extends Herbivores{


    public Mouse() {
        super("Mouse", 0.05, 500, 1, 0.01);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Mouse baby = new Mouse();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
