package com.example.authserver.persistence.respository;

import com.example.authserver.persistence.entity.Todo;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Integer> {

    @Query("UPDATE todo SET done = true WHERE id = :id")
    @Modifying
    public void updateDoneById(Integer id);
}
