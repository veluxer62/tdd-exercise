package com.example.demo;

import java.util.List;

public interface BoardService {
    void create(BoardCreationCommand command);
    Board getBoard(long id);
    void update(BoardUpdateCommand command, long id);
    List<Board> getBoards(BoardFilter filter);
    void delete(long id);
}
