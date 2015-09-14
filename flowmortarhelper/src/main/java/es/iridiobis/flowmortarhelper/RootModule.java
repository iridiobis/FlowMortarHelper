package es.iridiobis.flowmortarhelper;

import dagger.Module;
import dagger.Provides;
import es.iridiobis.flowmortarhelper.flow.CanShowScreen;

/**
 * Created by iridio on 02/08/15.
 */
@Module
public class RootModule {
    private final RootPresenter flowOwner;

    public RootModule() {
        flowOwner = new RootPresenter();
    }
    @Provides
    @ActivityScope
    RootPresenter provideRootPresenter() {
        return flowOwner;
    }

    @Provides
    @ActivityScope
    CanShowScreen provideFlow() {
        return flowOwner;
    }

}
