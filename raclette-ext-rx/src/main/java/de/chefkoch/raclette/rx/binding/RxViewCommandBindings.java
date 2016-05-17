package de.chefkoch.raclette.rx.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import com.jakewharton.rxrelay.Relay;
import de.chefkoch.raclette.rx.Command;

/**
 * Created by christophwidulle on 27.04.16.
 */
public class RxViewCommandBindings {


    @BindingAdapter({"bind:clickCommand"})
    public static void bindClickCommand(final View view, final Command<Void> command) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.call(null);
            }
        });
    }

    @BindingAdapter({"bind:clickCommand", "bind:commandParameter"})
    public static void bindClickCommandWithParameter(final View view, final Command command, final Object parameter) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.call(parameter);
            }
        });
    }
}
