package com.test.springtest.repository;

import com.test.springtest.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b order by b.id DESC ")
    List<Board> findAllDesc();
}
