package com.ua.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
