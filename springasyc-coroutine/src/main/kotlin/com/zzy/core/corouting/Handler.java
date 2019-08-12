package com.zzy.core.corouting;

public interface Handler<T> {
    void handle(T t);
}
