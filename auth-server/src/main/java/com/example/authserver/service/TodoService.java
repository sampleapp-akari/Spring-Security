package com.example.authserver.service;

import com.example.authserver.persistence.entity.Todo;

public interface TodoService {

    public Iterable<Todo> findAll();

    public void save(Todo todo);

    public void updateDoneById(Integer id);

    public void deleteById(Integer id);

    public boolean existsById(Integer id);
}
