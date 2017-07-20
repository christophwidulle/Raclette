package de.chefkoch.raclette.routing;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by christophwidulle on 06.12.15.
 */
public interface NavigationController {

    void to(NavRequest navRequest);

    void toForResult(NavRequest navRequest, ResultCallback resultCallback);

    void to(Intent intent);

    void toForResult(Intent intent, ResultCallback resultCallback);

    void back();

    void returnResult(Result result);

    void cancelResult();

}
