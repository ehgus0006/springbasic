package com.test.springtest.controller;

import com.test.springtest.dto.BoardModifyDTO;
import com.test.springtest.entity.Board;
import com.test.springtest.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/board")
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/modify/{id}")
    public void modify(@PathVariable Long id, @RequestBody BoardModifyDTO modifyDTO) {
        log.info("id=" + id);
        log.info("데이터 값들" + modifyDTO.getTitle() + "/" + modifyDTO.getContent());
        boardService.modify(id, modifyDTO);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
