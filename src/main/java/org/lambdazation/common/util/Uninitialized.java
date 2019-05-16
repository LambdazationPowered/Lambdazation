package org.lambdazation.common.util;

import org.lambdazation.common.util.data.Maybe;

public final class Uninitialized<T> {
    private boolean initialized;
    private T value;

    public Uninitialized() {
        this.initialized = false;
        this.value = null;
    }

    public Uninitialized(T value) {
        this.initialized = true;
        this.value = value;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init(T value) {
        if (initialized)
            throw new IllegalStateException();
        initialized = true;
        this.value = value;
    }

    public T get() {
        if (!initialized)
            throw new IllegalStateException();
        return value;
    }

    public Maybe<T> asMaybe() {
        return initialized ? Maybe.ofJust(value) : Maybe.ofNothing();
    }
}
