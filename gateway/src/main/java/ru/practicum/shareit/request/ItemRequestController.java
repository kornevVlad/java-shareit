package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                   @Valid @RequestBody RequestDto requestDto) {
        log.info("POST создание запроса предмета = {}, пользователем ID {}, описание {} ",
                requestDto ,requestorId ,requestDto.getDescription());
        return requestClient.createItemRequest(requestDto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequestorId(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET списка запросов пользователя с requestorId {}", userId);
        return requestClient.getItemRequestsByRequestorId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object> getItemRequestsById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemRequestId) {
        log.info("GET данные о запросе с ID {}, пользователем с ID {}",itemRequestId, userId);
        return requestClient.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(required = false) Integer size) {
        log.info("GET данные пользователем с ID {},  индекс первого элемента {}, " +
                "количество элементов для отображения {}", userId, from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }
}