package es.iridiobis.flowmortarhelper;

import android.app.Activity;
import android.app.Application;
import android.view.ViewGroup;

/** An indirection which allows controlling the root container used for each activity. */
public interface AppContainer {
    /** An {@link AppContainer} which returns the normal activity content view. */
    AppContainer DEFAULT = new AppContainer() {
        @Override public ViewGroup get(final Activity activity, final Application app) {
            return (ViewGroup) activity.findViewById(android.R.id.content);
        }
    };

    /**
     * The root {@link ViewGroup} into which the activity should place its contents.
     */
    ViewGroup get(Activity activity, Application app);
}
