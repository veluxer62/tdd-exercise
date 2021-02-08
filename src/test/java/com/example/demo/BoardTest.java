package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    @DisplayName("업데이트 함수는 수정 커멘드를 입력받아 타이틀과 내용을 변경한다")
    void test1() {
        // Given
        final var sut = new Board("테스트 타이틀", "테스트 내용", "사용자");
        final var command = new BoardUpdateCommand("수정 타이틀", "수정 내용");

        // When
        sut.update(command);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals("수정 타이틀", sut.getTitle()),
                () -> Assertions.assertEquals("수정 내용", sut.getContent())
        );
    }
}