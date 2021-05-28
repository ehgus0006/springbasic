package com.test.springtest.entity;

import com.test.springtest.dto.BoardModifyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 글제목
    private String title;

    // 글내용
    private String content;

    // 작성자
    private String writer;


    public void modify(BoardModifyDTO modifyDTO) {
        this.title = modifyDTO.getTitle();
        this.content = modifyDTO.getContent();
    }



}
