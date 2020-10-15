// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import java.util.Objects;

@SuppressWarnings("unused")
public class Key<T> {
    private final String name;

    private Key(String name) {
        this.name = name;
    }

    public static <TT> Key<TT> of(String name, Class<TT> type) {
        return new Key<>(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return Objects.equals(name, key.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
