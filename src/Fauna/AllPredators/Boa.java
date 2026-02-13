
package Fauna.AllPredators;
import Fauna.Animal;
import MapEngine.Coordinate;

    public class Boa extends Predators {

        public Boa() {
            super("Boa", 15, 30, 1, 3);
            setMaxAge(40);
        }

        @Override
        protected double getEatChance(Animal prey) {
            return switch (prey.getName()) {
                case "Rabbit" -> 0.2;
                case "Mouse" -> 0.4;
                case "Fox" -> 0.15;

                default -> 0.0;
            };
        }

        @Override
        protected Animal createChild() {
            Boa baby = new Boa();
            if (this.getPosition() != null) {
                baby.setPosition(new Coordinate(this.getPosition().x, this.getPosition().y));
            }
            return baby;
        }
    }


