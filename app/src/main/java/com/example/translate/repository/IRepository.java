package com.example.translate.repository;

import java.util.List;
import java.util.UUID;

public interface IRepository<E> {
    void insert(E e);
    E get(UUID uuid);
    List<E> getList();
    void delete(E e);
    void update(E e);
}
