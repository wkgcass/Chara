// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

public interface BiConsumerThrowsException<T, U> {
    void accept(T t, U u) throws Exception;
}
