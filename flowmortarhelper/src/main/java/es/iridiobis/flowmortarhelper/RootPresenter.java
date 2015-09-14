package es.iridiobis.flowmortarhelper;

import android.os.Bundle;

import es.iridiobis.flowmortarhelper.flow.CanShowScreen;
import flow.Flow;
import flow.History;
import flow.path.Path;
import mortar.ViewPresenter;

import static flow.Flow.Direction.FORWARD;

/**
 * Created by iridio on 18/07/15.
 */
public class RootPresenter extends ViewPresenter<RootView> implements CanShowScreen {

    private ToolbarConfiguration configuration;

    @Override
    public <S extends Path> void showScreen(S screen) {
        if (hasView()) {
            Flow.get(getView()).set(screen);
        }
    }

    @Override
    public <S extends Path> void cleanAndShowScreen(S screen) {
        if (hasView()) {
            Flow.get(getView().getContext())
                    .setHistory(History.emptyBuilder().push(screen).build(), FORWARD);
        }
    }

    @Override
    public boolean goBack() {
        if (hasView()) {
            return Flow.get(getView().getContext()).goBack();
        } else {
            return true;
        }
    }

    @Override
    public void showInitial() {

    }

    public void configure(final ToolbarConfiguration configuration) {
        this.configuration = configuration;
        applyConfiguration();
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        applyConfiguration();
    }

    private void applyConfiguration() {
        if (hasView()) {
            getView().applyConfiguration(configuration);
        }
    }

    public void closeDrawers() {
        if (hasView()) {
            getView().closeDrawers();
        }
    }
}
