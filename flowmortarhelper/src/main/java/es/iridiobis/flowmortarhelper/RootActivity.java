package es.iridiobis.flowmortarhelper;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;

import es.iridiobis.flowmortarhelper.dagger.DaggerService;
import es.iridiobis.flowmortarhelper.flow.FramePathContainerView;
import es.iridiobis.flowmortarhelper.flow.GsonParceler;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

@ActivityScope
public abstract class RootActivity<T extends RootComponent<T>> extends AppCompatActivity implements Flow.Dispatcher {

    AppContainer appContainer;

    FramePathContainerView container;

    private MortarScope activityScope;
    private FlowDelegate flowSupport;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColor(Window window) {
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    protected abstract T buildRootComponent();

    protected abstract Path getInitialScreen();

    public T getDaggerComponent() {
        return DaggerService.<T>getDaggerComponent(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Remove the status bar color. The DrawerLayout is responsible for drawing it from
            // now on.
            setStatusBarColor(getWindow());
        }

        activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            RootComponent component = buildRootComponent();
//            RootActivity.Component component = DaggerRootActivity_Component.builder()
//                    .applicationComponent(ConsumoApplication.get(this).component())
//                    .build();

            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .build(getScopeName());
        }

        getDaggerComponent().inject(this);
        appContainer = AppContainer.DEFAULT;
        //TODO rename container
        ViewGroup container = appContainer.get(this, RootApplication.get(this));
        getLayoutInflater().inflate(R.layout.root, container);
        this.container = (FramePathContainerView) findViewById(R.id.root_container);
        @SuppressWarnings("deprecation") FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        GsonParceler parceler = new GsonParceler(new Gson());
        flowSupport = FlowDelegate.onCreate(
                nonConfig, getIntent(), savedInstanceState, parceler,
                History.single(getInitialScreen()), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowSupport.onResume();
    }

    @Override
    protected void onPause() {
        flowSupport.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //isfinishing == false on rotation
        if (isFinishing() && activityScope != null) {
            activityScope.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowSupport.onNewIntent(intent);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return flowSupport.onRetainNonConfigurationInstance();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowSupport.onSaveInstanceState(outState);
    }

    @Override
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        container.dispatch(
                traversal, new Flow.TraversalCallback() {
                    @Override
                    public void onTraversalCompleted() {
                        callback.onTraversalCompleted();
                    }
                });
    }

    @Override
    public Object getSystemService(String name) {
        if (flowSupport != null) {
            Object flowService = flowSupport.getSystemService(name);
            if (flowService != null) {
                return flowService;
            }
        }

        return activityScope != null && activityScope.hasService(name) ? activityScope.getService
                (name)
                : super.getSystemService(name);
    }

    @Override
    public void onBackPressed() {
        if (!container.onBackPressed() && !flowSupport.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private String getScopeName() {
        return getClass().getName();
    }

}
