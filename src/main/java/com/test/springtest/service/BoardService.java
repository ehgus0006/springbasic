package com.test.springtest.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.test.springtest.dto.*;
import com.test.springtest.entity.Board;
import com.test.springtest.entity.QBoard;
import com.test.springtest.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardSaveDTO boardSaveDTO) {
        log.info("boardService 들어옴");
        Board board = Board.builder()
                .title(boardSaveDTO.getTitle())
                .content(boardSaveDTO.getContent())
                .writer(boardSaveDTO.getWriter())
                .build();
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<BoardListDTO> findAll() {
        // 스트림은 자바8에 새롭게 추가된 기능으로, 선언형(sql같은 질의형)으로 데이터(컬렉션, 배열, 파일, iterate...)를 처리할 수 있다.
        // Collectors 란 Stream 을 일반적인 List, Set 등으로 변경시키는 Stream 메서드
        // List<String> givenList = Arrays.asList("a", "bb", "cc", "bb");
        // List<String> result = givenList.stream().collect(Collectors.toList()); // a, bb, ccc, dd

        /* 전체 조회 하는거 repository에 모든값을 pk기준으로 desc로 가져올거기 때문에 값이 여러개니깐 List 타입 배열형태로 값을 받고
            자바 8람다를 이용해서 전체 값을 가져올거임.
        *

        */
       return boardRepository.findAllDesc()
               .stream()
               .map(BoardListDTO::new)
               .collect(Collectors.toList());
    }


    public void modify(Long id, BoardModifyDTO modifyDTO) {
        Board result = boardRepository.findById(id).get();
        result.modify(modifyDTO);
        boardRepository.save(result);
    }

    public void delete(Long id) {
        /* repository는 deleteById delete 두가지 방법으로 삭제 할 수 있는데
        *  deleteById 를 사용하면 서비스 로직에서 메소드를 하나만 사용해도 조회와 삭제가 다된다
        *  delete 를 사용하면 조회 후 삭제 해야한다.
        *  단점은 deleteById를 사용하면 조회 시 값이 없을 경우 ResultDataAccessException 이 발생하고
        *  더 다양한 퍼포먼스를 사용하려면 delete 를 사용해서 처리하는게 안정성과 유지보수에 용이 하다.
        */
        Board board = boardRepository.findById(id).get(); //  조회
        boardRepository.delete(board);

//        boardRepository.deleteById(id);
    }

    private BoardListDTO dtoList(Board entity) {
        BoardListDTO dto = BoardListDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

    public PageResultDTO<BoardListDTO, Board> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());
        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
        Page<Board> result = boardRepository.findAll(booleanBuilder,pageable);
        Function<Board,BoardListDTO> fn = (entity -> dtoList(entity));

        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDto) {  // Querydsl처리
        String type = requestDto.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        String keyword = requestDto.getKeyword();
        BooleanExpression expression = qBoard.id.gt(0L);
        booleanBuilder.and(expression);

        //검색조건이 없는경우
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        // 검색조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qBoard.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qBoard.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qBoard.writer.contains(keyword));
        }

        // 모든조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }


}
