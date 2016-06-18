package de.chefkoch.raclette.sample.rest;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class Character {

    String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                '}';
    }
}
