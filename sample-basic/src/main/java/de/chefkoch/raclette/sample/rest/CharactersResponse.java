package de.chefkoch.raclette.sample.rest;

import java.util.List;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class CharactersResponse {

    int count;
    List<Character> results;

    public int getCount() {
        return count;
    }

    public List<Character> getResults() {
        return results;
    }
}
