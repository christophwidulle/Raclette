package de.chefkoch.raclette.rx.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import com.jakewharton.rxrelay.Relay;

/**
 * Created by christophwidulle on 27.04.16.
 */
public class RxViewCommandBindings {


    @BindingAdapter({"bind:clickCommand"})
    public static void bindClickCommand(final View view, final Relay<Void, Void> relay) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relay.call(null);
            }
        });
    }
}
