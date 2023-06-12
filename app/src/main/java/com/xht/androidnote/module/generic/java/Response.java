package com.xht.androidnote.module.generic.java;

public class Response<T> {
    private T entity;

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
