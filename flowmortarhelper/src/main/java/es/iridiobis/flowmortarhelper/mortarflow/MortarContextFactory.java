package es.iridiobis.flowmortarhelper.mortarflow;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import dagger.Module;
import es.iridiobis.flowmortarhelper.dagger.DaggerService;
import es.iridiobis.flowmortarhelper.dagger.WithComponent;
import flow.path.Path;
import flow.path.PathContextFactory;
import mortar.MortarScope;
import timber.log.Timber;

public final class MortarContextFactory implements PathContextFactory {

    @Override
    public Context setUpContext(Path path, Context parentContext) {
        return setUpContext(path, path.toString(), parentContext);
    }

    public Context setUpContext(Path path, final String name, Context parentContext) {
        MortarScope scope = MortarScope.findChild(parentContext, name);
        if (scope == null) {
            final WithComponent withComponent = path.getClass().getAnnotation(WithComponent.class);
            if (withComponent == null) {
                throw new IllegalStateException(String.format("Missing WithComponent annotation " +
                        "on %s", path.getClass().getName()));
            }

            scope = MortarScope.buildChild(parentContext)
                    .withService(DaggerService.SERVICE_NAME, new SimpleComponentFactory
                            (withComponent.value()).getService(parentContext, path))
                    .build(name);
        }

        return new TearDownContext(parentContext, scope);
    }

    @Override
    public void tearDownContext(Context context) {
        TearDownContext.destroyScope(context);
    }

    static class TearDownContext extends ContextWrapper {
        private static final String SERVICE = "SNEAKY_MORTAR_PARENT_HOOK";
        private final MortarScope parentScope;
        private LayoutInflater inflater;

        public TearDownContext(Context context, MortarScope scope) {
            super(scope.createContext(context));
            this.parentScope = MortarScope.getScope(context);
        }

        static void destroyScope(Context context) {
            MortarScope.getScope(context).destroy();
        }

        @Override
        public Object getSystemService(String name) {
            if (LAYOUT_INFLATER_SERVICE.equals(name)) {
                if (inflater == null) {
                    inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
                }
                return inflater;
            }

            if (SERVICE.equals(name)) {
                return parentScope;
            }

            return super.getSystemService(name);
        }
    }

    private static class SimpleComponentFactory {
        protected final Class serviceClass;

        public SimpleComponentFactory(Class serviceClass) {
            this.serviceClass = serviceClass;
        }

        public Object getService(Context context, Path screen) {
            Object depComponent = DaggerService.getDaggerComponent(context);
            Object depModule = null;
            // Find and instantiate inner module if any
            for (Class innerClass : screen.getClass().getClasses()) {
                if (Modifier.isStatic(innerClass.getModifiers())) continue;
                if (innerClass.getAnnotation(Module.class) != null) {
                    try {
                        Constructor constructor = innerClass.getDeclaredConstructor(screen
                                .getClass());
                        depModule = constructor.newInstance(screen);
                    } catch (IllegalAccessException e) {
                        Timber.e("Error getting service", e);
                    } catch (NoSuchMethodException e) {
                        Timber.e("Error getting service", e);
                    } catch (InstantiationException e) {
                        Timber.e("Error getting service", e);
                    } catch (InvocationTargetException e) {
                        Timber.e("Error getting service", e);
                    }
                    break;
                }
            }
            if (depModule == null) {
                return DaggerService.createComponent(serviceClass, depComponent);
            } else {
                return DaggerService.createComponent(serviceClass, depComponent, depModule);
            }
        }
    }
}
