package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("사용자는 게시글 등록정보를 입력하여 게시글을 등록할 수 있다")
    void test1() throws Exception {
        // Given
        final var requestBody = new BoardCreationCommand("테스트 타이틀", "테스트 내용", "사용자");
        final var requestBodyString = mapper.writeValueAsString(requestBody);

        // When
        mvc
                .perform(
                        post("/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyString)
                )
                .andExpect(status().isOk());

        // Then
        Mockito.verify(boardService).create(requestBody);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("사용자는 빈 제목을 가진 게시글 등록정보를 입력하면 400 Bad Request를 반환한다")
    void test1_2(String title) throws Exception {
        // Given
        final var jsonString = "{\"title\":\"" + title + "\", \"content\":\"테스트 내용\", \"author\":\"사용자\"}";

        // When
        mvc
                .perform(
                        post("/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                .andExpect(status().isBadRequest());

        // Then
        Mockito.verify(boardService, Mockito.never()).create(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("사용자는 빈 사용자명을 가진 게시글 등록정보를 입력하면 400 Bad Request를 반환한다")
    void test1_3(String author) throws Exception {
        // Given
        final var jsonString = "{\"title\":\"테스트 타이틀\", \"content\":\"테스트 내용\", \"author\":\"" + author + "\"}";

        // When
        mvc
                .perform(
                        post("/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                .andExpect(status().isBadRequest());

        // Then
        Mockito.verify(boardService, Mockito.never()).create(any());
    }

    @Test
    @DisplayName("사용자는 게시글 ID를 이용하여 단일 게시글을 조회할 수 있다")
    void test2() throws Exception {
        // Given
        final var id = 10L;

        Mockito.when(boardService.getBoard(id))
                .thenReturn(new Board("테스트 타이틀", "테스트 내용", "사용자"));

        // Expected
        mvc.perform(get("/boards/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 타이틀"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                .andExpect(jsonPath("$.author").value("사용자"));
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 게시글 ID를 이용하여 단일 게시글을 조회하면 400 Bad Request를 반환한다")
    void test2_1() throws Exception {
        // Given
        final var id = 10L;

        Mockito.when(boardService.getBoard(id))
                .thenThrow(new IllegalArgumentException());

        // Expected
        mvc.perform(get("/boards/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자는 게시글 수정 정보를 입력하여 게시글을 수정할 수 있다")
    void test3() throws Exception {
        // Given
        final var id = 10L;
        final var requestBody = new BoardUpdateCommand("수정 타이틀", "수정 내용");
        final var requestBodyString = mapper.writeValueAsString(requestBody);

        // When
        mvc.perform(
                put("/boards/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyString)
        ).andExpect(status().isOk());

        // Then
        Mockito.verify(boardService).update(requestBody, id);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("사용자는 빈 제목을 가진 게시글 수정 정보를 입력하면 400 Bad Request를 반환한다")
    void test3_1(String title) throws Exception {
        // Given
        final var id = 10L;
        final var jsonString = "{\"title\":\"" + title + "\", \"content\":\"테스트 내용\"}";

        // When
        mvc.perform(
                put("/boards/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        ).andExpect(status().isBadRequest());

        // Then
        Mockito.verify(boardService, Mockito.never()).update(any(), eq(10L));
    }

    @Test
    @DisplayName("사용자는 게시글 목록을 조회할 수 있다")
    void test4() throws Exception {
        // Given
        final var board1 = new Board("타이틀1", "내용1", "사용자1");
        final var board2 = new Board("타이틀2", "내용2", "사용자2");
        final var board3 = new Board("타이틀3", "내용3", "사용자3");
        Mockito.when(boardService.getBoards(new BoardFilter()))
                .thenReturn(List.of(board1, board2, board3));

        // Expected
        mvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(3));
    }

    @Test
    @DisplayName("사용자는 제목을 입력하여 입력한 제목과 일치하는 게시글 목록을 조회할 수 있다")
    void test4_1() throws Exception {
        // Given
        final var board1 = new Board("타이틀1", "내용1", "사용자1");

        final var filter = new BoardFilter("타이틀1");
        Mockito.when(boardService.getBoards(filter))
                .thenReturn(List.of(board1));

        // Expected
        mvc.perform(get("/boards?title=타이틀1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(1));
    }

    @Test
    @DisplayName("사용자는 게시글 ID를 이용하여 선택한 게시글을 삭제할 수 있다.")
    void test5() throws Exception {
        // Given
        final var id = 10L;

        // When
        mvc.perform(delete("/boards/" + id))
                .andExpect(status().isOk());

        // Then
        Mockito.verify(boardService).delete(id);
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 게시글 ID가 주어지면 404 Bad Request를 반환한다")
    void test5_1() throws Exception {
        // Given
        final var id = 10L;
        Mockito.doThrow(new EntityNotFoundException())
                .when(boardService)
                .delete(id);

        // Expected
        mvc.perform(delete("/boards/" + id))
                .andExpect(status().isNotFound());
    }
}
