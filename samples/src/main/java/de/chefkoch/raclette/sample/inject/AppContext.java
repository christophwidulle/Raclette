package de.chefkoch.raclette.sample.inject;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Qualifier
@Retention(CLASS)
public @interface AppContext {
}
