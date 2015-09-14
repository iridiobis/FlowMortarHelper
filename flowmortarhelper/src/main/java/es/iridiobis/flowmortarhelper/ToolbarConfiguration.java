package es.iridiobis.flowmortarhelper;

import android.support.v7.widget.Toolbar;

import com.google.common.base.Optional;

/**
 * Created by iridio on 11/08/15.
 */
public class ToolbarConfiguration {
    private final boolean root;
    private final String title;
    private final Optional<Integer> menu;
    private final Toolbar.OnMenuItemClickListener listener;
    private final Optional<Integer> background;
    private final Optional<Integer> foreground;

    private ToolbarConfiguration(Builder builder) {
        root = builder.root;
        title = builder.title;
        menu = builder.menu;
        listener = builder.listener;
        background = builder.background;
        foreground = builder.foreground;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(ToolbarConfiguration copy) {
        Builder builder = new Builder();
        builder.root = copy.root;
        builder.title = copy.title;
        builder.menu = copy.menu;
        builder.listener = copy.listener;
        builder.background = copy.background;
        builder.foreground = copy.foreground;
        return builder;
    }

    public boolean isRoot() {
        return root;
    }

    public String getTitle() {
        return title;
    }

    public Optional<Integer> getMenu() {
        return menu;
    }

    public Toolbar.OnMenuItemClickListener getListener() {
        return listener;
    }

    public Optional<Integer> getBackground() {
        return background;
    }

    public Optional<Integer> getForeground() {
        return foreground;
    }

    /**
     * {@code ToolbarConfiguration} builder static inner class.
     */
    public static final class Builder {
        private boolean root;
        private String title;
        private Optional<Integer> menu;
        private Toolbar.OnMenuItemClickListener listener;
        private Optional<Integer> background;
        private Optional<Integer> foreground;

        private Builder() {
            root = true;
            title = "";
            menu = Optional.absent();
            background = Optional.absent();
            foreground = Optional.absent();
        }

        /**
         * Sets the {@code root} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param root the {@code root} to set
         * @return a reference to this Builder
         */
        public Builder root(final boolean root) {
            this.root = root;
            return this;
        }

        /**
         * Sets the {@code title} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param title the {@code title} to set
         * @return a reference to this Builder
         */
        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the {@code menu} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param menu the {@code menu} to set
         * @return a reference to this Builder
         */
        public Builder menu(Optional<Integer> menu) {
            this.menu = menu;
            return this;
        }

        /**
         * Sets the {@code listener} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param listener the {@code listener} to set
         * @return a reference to this Builder
         */
        public Builder listener(Toolbar.OnMenuItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Sets the {@code background} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param background the {@code background} to set
         * @return a reference to this Builder
         */
        public Builder background(Optional<Integer> background) {
            this.background = background;
            return this;
        }

        /**
         * Sets the {@code foreground} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param foreground the {@code foreground} to set
         * @return a reference to this Builder
         */
        public Builder foreground(Optional<Integer> foreground) {
            this.foreground = foreground;
            return this;
        }

        /**
         * Sets the {@code menu} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param menu the {@code menu} to set
         * @return a reference to this Builder
         */
        public Builder menu(final int menu) {
            this.menu = Optional.of(menu);
            return this;
        }

        /**
         * Sets the {@code background} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param background the {@code background} to set
         * @return a reference to this Builder
         */
        public Builder background(final int background) {
            this.background = Optional.of(background);
            return this;
        }

        /**
         * Sets the {@code foreground} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param foreground the {@code foreground} to set
         * @return a reference to this Builder
         */
        public Builder foreground(final int foreground) {
            this.foreground = Optional.of(foreground);
            return this;
        }

        /**
         * Returns a {@code ToolbarConfiguration} built from the parameters previously set.
         *
         * @return a {@code ToolbarConfiguration} built with parameters of this {@code ToolbarConfiguration.Builder}
         */
        public ToolbarConfiguration build() throws IllegalStateException {
            if (menu.isPresent() && listener == null) {
                throw new IllegalStateException("No listener for the menu");
            }
            return new ToolbarConfiguration(this);
        }
    }
}
