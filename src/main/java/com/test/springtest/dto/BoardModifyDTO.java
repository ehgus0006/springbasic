package com.test.springtest.dto;

import com.test.springtest.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BoardModifyDTO {

    private String title;
    private String content;
    private LocalDateTime modDate;

}
