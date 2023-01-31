package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto getItemRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Предмет");
        itemRequestDto.setCreated(LocalDateTime.of(2023,5,10,5,10));
        return itemRequestDto;
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(getItemRequestDto());
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(getItemRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getItemRequestDto().getId()), Long.class))
                .andExpect(jsonPath("$.description", is(getItemRequestDto().getDescription())))
                .andExpect(jsonPath("$.created",
                        is(getItemRequestDto().getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getItemRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(any(Long.class), any(Long.class)))
                .thenReturn(getItemRequestDto());
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(getItemRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getItemRequestDto().getId()), Long.class))
                .andExpect(jsonPath("$.description", is(getItemRequestDto().getDescription())))
                .andExpect(jsonPath("$.created",
                        is(getItemRequestDto().getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getItemRequestsByRequestorId() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(getItemRequestDto());
        when(itemRequestService.getItemRequestsByRequestorId(any(Long.class)))
                .thenReturn(itemRequestDtoList);
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(getItemRequestDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(getItemRequestDto().getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(getItemRequestDto().getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getAllItemRequests() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(getItemRequestDto());
        when(itemRequestService.getAllItemRequests(any(Long.class), any(Integer.class), nullable(Integer.class)))
                .thenReturn(itemRequestDtoList);
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemRequestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(getItemRequestDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(getItemRequestDto().getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(getItemRequestDto().getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}
