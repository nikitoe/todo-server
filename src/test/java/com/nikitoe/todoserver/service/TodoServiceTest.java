package com.nikitoe.todoserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.nikitoe.todoserver.model.TodoEntity;
import com.nikitoe.todoserver.model.TodoRequest;
import com.nikitoe.todoserver.repository.TodoRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;


    @Test
    @DisplayName("TodoList 추가")
    void add() {
        //given
        //when
        when(this.todoRepository.save(any(TodoEntity.class)))
            .then(AdditionalAnswers.returnsFirstArg());

        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test title");

        TodoEntity actual = this.todoService.add(expected);

        //then
        assertEquals(expected.getTitle(), actual.getTitle());

    }

    @Test
    @DisplayName("ID를 통해 TodoList 검색_성공")
    void searchById() {
        // given
        TodoEntity entity = new TodoEntity();
        entity.setId(123L);
        entity.setTitle("Title");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoEntity> optional = Optional.of(entity);

        given(this.todoRepository.findById(anyLong()))
            .willReturn(optional);

       // when
       TodoEntity actual = this.todoService.searchById(123L);
       TodoEntity expected = optional.get();

       // then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());

    }

    @Test
    @DisplayName("Id를 통해 TodoList 검색_실패")
    void searchByIdFalse() {
        //given
        given(this.todoRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResponseStatusException.class, ()-> {
            this.todoService.searchById(123L);
        });


    }
}