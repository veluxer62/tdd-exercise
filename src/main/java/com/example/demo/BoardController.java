package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    void create(@RequestBody BoardCreationCommand command) {
        boardService.create(command);
    }

    @GetMapping("/{id}")
    BoardDto getBoard(@PathVariable("id") long id) {
        final var board = boardService.getBoard(id);
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getAuthor(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    @PutMapping("/{id}")
    void update(@RequestBody BoardUpdateCommand command, @PathVariable("id") long id) {
        boardService.update(command, id);
    }

    @GetMapping
    ItemsDto<BoardDto> getList(BoardFilter filter) {
        final var boards = boardService.getBoards(filter);
        final var boardDtoList = boards.stream()
                .map(board -> new BoardDto(
                        board.getId(), board.getTitle(),
                        board.getContent(), board.getAuthor(),
                        board.getCreatedAt(), board.getUpdatedAt()))
                .collect(Collectors.toList());
        return new ItemsDto<>(boardDtoList);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") long id) {
        boardService.delete(id);
    }
}
