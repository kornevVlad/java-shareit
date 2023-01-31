package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.status.ItemStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(), any(Long.class)))
                .thenReturn(getItemDto());
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(getItemDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getItemDto().getName())))
                .andExpect(jsonPath("$.description", is(getItemDto().getDescription())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(any(), any(Long.class), any(Long.class)))
                .thenReturn(getItemDto());
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(getItemDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getItemDto().getName())))
                .andExpect(jsonPath("$.description", is(getItemDto().getDescription())));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItemById(any(Long.class), any(Long.class)))
                .thenReturn(getItemDto());
        mvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(getItemDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getItemDto().getName())))
                .andExpect(jsonPath("$.description", is(getItemDto().getDescription())));
    }

    @Test
    void getItems() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(getItemDto());
        when(itemService.getItems(any(Long.class)))
                .thenReturn(items);
        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(items))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(getItemDto().getName())))
                .andExpect(jsonPath("$.[0].description", is(getItemDto().getDescription())));
    }

    @Test
    void getItemsBySearch() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(getItemDto());
        when(itemService.getItemsBySearch(any(String.class)))
                .thenReturn(items);
        mvc.perform(get("/items/search?text=description")
                        .content(mapper.writeValueAsString(items))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(getItemDto().getName())))
                .andExpect(jsonPath("$.[0].description", is(getItemDto().getDescription())));
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(any(), any(Long.class), any(Long.class)))
                .thenReturn(getCommentDto());
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(getCommentDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getCommentDto().getId()), Long.class))
                .andExpect(jsonPath("$.text", is(getCommentDto().getText())))
                .andExpect(jsonPath("$.authorName", is(getCommentDto().getAuthorName())));
    }

    private CommentDto getCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("COMMENT");
        commentDto.setAuthorName("Vlad");
        commentDto.setCreated(LocalDateTime.of(2023,1,30,12,10));
        return commentDto;
    }

    private ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Предмет");
        itemDto.setDescription("Предмет предмет");
        itemDto.setAvailable(ItemStatus.of(true));
        return itemDto;
    }
}
