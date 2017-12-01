package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;
import uk.co.ribot.androidboilerplate.ui.register.RegisterActivity;
import uk.co.ribot.androidboilerplate.ui.signin.SignInActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ContentActivity contentActivity);

    void inject(RegisterActivity registerActivity);

    void inject(MainActivity mainActivity);

    void inject(SignInActivity signInActivity);

}
