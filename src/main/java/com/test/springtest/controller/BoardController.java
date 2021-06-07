package com.test.springtest.controller;

import com.test.springtest.dto.BoardSaveDTO;
import com.test.springtest.dto.PageRequestDTO;
import com.test.springtest.entity.Board;
import com.test.springtest.repository.BoardRepository;
import com.test.springtest.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("/select")
    public String main(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", boardService.getList(pageRequestDTO));
        model.addAttribute("pageRequestDTO", pageRequestDTO);
//        model.addAttribute("dto", boardService.findAll());
        return "board/boardSelect";
    }

    @GetMapping("/resister")
    public String resister() {
        return "board/boardResister";
    }

    @PostMapping("/resister")
    public String save(BoardSaveDTO boardSaveDTO, RedirectAttributes redirectAttributes) {
        log.info(boardSaveDTO + "BoardController POST");
        boardService.save(boardSaveDTO);
        redirectAttributes.addFlashAttribute("dto", boardSaveDTO);
        return "redirect:/board/select";
    }

    //
    //
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model,PageRequestDTO pageRequestDTO) {
        log.info("id =" + id);
        Board data = boardRepository.findById(id).get();
        model.addAttribute("requestDTO", pageRequestDTO);
        model.addAttribute("dto", data);
        return "board/boardDetail";
    }

    @GetMapping("/modify/{id}")
    public String modify(PageRequestDTO requestDTO,@PathVariable Long id, Model model) {
        log.info("id =" + id);
        log.info("page번호" + requestDTO.getPage());
        Board data = boardRepository.findById(id).get();
        model.addAttribute("dto", data);
        model.addAttribute("requestDTO", requestDTO);
        return "board/boardModify";
    }

}
