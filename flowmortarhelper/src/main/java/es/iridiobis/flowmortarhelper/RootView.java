package es.iridiobis.flowmortarhelper;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import javax.inject.Inject;

import es.iridiobis.flowmortarhelper.flow.FramePathContainerView;

/**
 * Created by iridio on 18/07/15.
 */
public class RootView extends DrawerLayout {
    protected NavigationView drawer;
    @Inject
    RootPresenter presenter;
    @Inject
    NavigationView.OnNavigationItemSelectedListener drawerListener;
    Toolbar toolbar;
    FramePathContainerView container;
    private ActionBarDrawerToggle toggle;


    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            ((RootActivity) context).getDaggerComponent().inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            drawer = (NavigationView) findViewById(R.id.root_navigation);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            container = (FramePathContainerView) findViewById(R.id.root_container);

            ((RootActivity) getContext()).setSupportActionBar(toolbar);


            toggle = new ActionBarDrawerToggle(((RootActivity) getContext()), this, toolbar, R.string.app_name, R.string.app_name);
            setDrawerListener(toggle);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    container.onBackPressed();
                }
            });
            setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            drawer.setNavigationItemSelectedListener(drawerListener);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

    public void applyConfiguration(final ToolbarConfiguration configuration) {
        if (configuration.isRoot()) {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            ((RootActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            ((RootActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle(configuration.getTitle());
        toolbar.getMenu().clear();
        if (configuration.getMenu().isPresent()) {
            toolbar.inflateMenu(configuration.getMenu().get());
            toolbar.setOnMenuItemClickListener(configuration.getListener());
        }
        if (configuration.getBackground().isPresent()) {
            toolbar.setBackgroundColor(configuration.getBackground().get());
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        }
        if (configuration.getForeground().isPresent()) {
            toolbar.setTitleTextColor(configuration.getForeground().get());
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.foreground));
        }


        toggle.syncState();
    }
}
