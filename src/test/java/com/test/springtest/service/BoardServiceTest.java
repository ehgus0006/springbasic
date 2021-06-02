package com.test.springtest.service;

import com.test.springtest.entity.Board;
import com.test.springtest.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    public BoardRepository boardRepository;
    
    @Test
    public void dummy() {

        IntStream.range(1, 100).forEach(i -> {

            Board board = Board.builder()
                    .title("제목"+i)
                    .content("내용"+i)
                    .writer("작성자"+i)
                    .build();
            boardRepository.save(board);
        });
    }
}