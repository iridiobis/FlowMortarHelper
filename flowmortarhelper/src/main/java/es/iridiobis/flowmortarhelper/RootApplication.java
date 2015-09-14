package es.iridiobis.flowmortarhelper;

import android.app.Application;
import android.content.Context;

import mortar.MortarScope;

/**
 * TODO: Add a class header comment!
 *
 * @author jlbe
 * @since ${VERSION}
 */
public class RootApplication extends Application {
    private MortarScope rootScope;

    public static RootApplication get(Context context) {
        return (RootApplication) context.getApplicationContext();
    }

    @Override
    public Object getSystemService(final String name) {
        if (rootScope == null) rootScope = MortarScope.buildRootScope().build("root_scope");
        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }
}
