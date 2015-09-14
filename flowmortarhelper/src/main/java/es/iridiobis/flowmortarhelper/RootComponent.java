package es.iridiobis.flowmortarhelper;

import es.iridiobis.flowmortarhelper.flow.CanShowScreen;

/**
 * Created by iridio on 23/08/15.
 */
public interface RootComponent<T extends RootComponent<T>> {
    void inject(RootActivity<T> activity);

    void inject(RootView root);

    CanShowScreen canShowScreen();

    RootPresenter rootPresenter();
}
