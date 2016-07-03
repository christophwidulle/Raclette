package de.chefkoch.raclette.compiler;

/**
 * Created by christophwidulle on 02.07.16.
 */
public class StringUtil {


    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String decapitalize(final String line) {
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }
}
