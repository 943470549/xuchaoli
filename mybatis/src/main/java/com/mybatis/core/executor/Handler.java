package com.mybatis.core.executor;

public interface Handler<E> {
    void handle(E event);

}
