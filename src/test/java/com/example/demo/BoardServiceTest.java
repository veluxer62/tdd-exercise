package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SimpleBoardService.class)
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @MockBean
    private BoardRepository boardRepository;

    @Test
    @DisplayName("create함수는 게시글 등록 커멘드를 입력받아 Repository에 저장 요청을 한다")
    void test1() {
        // Given
        final var command = new BoardCreationCommand("테스트 타이틀", "테스트 내용", "사용자");

        // When
        boardService.create(command);

        // Then
        Mockito.verify(boardRepository).save(
                Mockito.argThat(board ->
                        board.getTitle().equals("테스트 타이틀") &&
                                board.getContent().equals("테스트 내용") &&
                                board.getAuthor().equals("사용자"))
        );
    }

    @Test
    @DisplayName("getBoard함수는 id를 입력받아 게시글 정보를 반환한다")
    void test2() {
        // Given
        final var id = 10L;

        Mockito.when(boardRepository.findById(id))
                .thenReturn(Optional.of(new Board("테스트 타이틀", "테스트 내용", "사용자")));

        // When
        final var actual = boardService.getBoard(id);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals("테스트 타이틀", actual.getTitle()),
                () -> Assertions.assertEquals("테스트 내용", actual.getContent()),
                () -> Assertions.assertEquals("사용자", actual.getAuthor())
        );
    }

    @Test
    @DisplayName("getBoard함수는 존재하지 않는 id를 입력받으면 예외를 던진다")
    void test2_1() {
        // Given
        final var id = 10L;

        Mockito.when(boardRepository.findById(id))
                .thenReturn(Optional.empty());

        // Expected
        Assertions.assertThrows(IllegalArgumentException.class, () -> boardService.getBoard(id));
    }

    @Test
    @DisplayName("update함수는 수정 커멘드와 ID를 입력받아 board를 수정한다")
    void test3() {
        // Given
        final var id = 10L;
        final var command = new BoardUpdateCommand("수정 타이틀", "수정 내용");

        final var board = new Board("테스트 타이틀", "테스트 내용", "사용자");
        Mockito.when(boardRepository.findById(id))
                .thenReturn(Optional.of(board));

        // When
        boardService.update(command, id);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals("수정 타이틀", board.getTitle()),
                () -> Assertions.assertEquals("수정 내용", board.getContent())
        );
    }

    @Test
    @DisplayName("getBoards함수는 게시글 목록을 반환한다")
    void test4() {
        // Given
        final var board1 = new Board("타이틀1", "내용1", "사용자1");
        final var board2 = new Board("타이틀2", "내용2", "사용자2");
        final var board3 = new Board("타이틀3", "내용3", "사용자3");
        final var boardList = List.of(board1, board2, board3);
        Mockito.when(boardRepository.findAll())
                .thenReturn(boardList);

        // When
        final var actual = boardService.getBoards(new BoardFilter());

        // Then
        Assertions.assertEquals(boardList, actual);
    }

    @Test
    @DisplayName("getBoards함수는 제목검색 필터가 주어지면 게시글 목록을 반환한다")
    void test4_1() {
        // Given
        final var board1 = new Board("타이틀1", "내용1", "사용자1");
        final var boardList = List.of(board1);
        Mockito.when(boardRepository.findByTitle("타이틀1"))
                .thenReturn(boardList);

        // When
        final var actual = boardService.getBoards(new BoardFilter("타이틀1"));

        // Then
        Assertions.assertEquals(boardList, actual);
    }

    @Test
    @DisplayName("삭제 함수는 ID가 주어지면 repository의 삭제함수를 호출한다")
    void test5() {
        // Given
        final var id = 10L;
        Mockito.when(boardRepository.findById(id))
                .thenReturn(Optional.of(new Board("테스트 제목", "테스트 내용", "사용자")));

        // When
        boardService.delete(id);

        // Then
        Mockito.verify(boardRepository).deleteById(id);
    }

    @Test
    @DisplayName("삭제 함수는 존재하지 않는 ID가 주어지면 예외를 던진다")
    void test5_1() {
        // Given
        final var id = 10L;
        Mockito.when(boardRepository.findById(id))
                .thenReturn(Optional.empty());

        // Expected
        Assertions.assertThrows(EntityNotFoundException.class, () -> boardService.delete(id));
    }
}