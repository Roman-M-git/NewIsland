package Fauna.AllHerbivores;

import Fauna.Animal;
import MapEngine.Coordinate;

public class Boar extends Herbivores{


    public Boar() {
        super("Boar", 400, 50, 2, 50);
        setMaxAge(10);
    }

    @Override
    protected Animal createChild() {
        Boar baby = new Boar();
        if (this.getPosition() != null) {
            baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
        }
        return baby;
    }
}
