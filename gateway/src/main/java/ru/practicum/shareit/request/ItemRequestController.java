package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.validation_interface.Create;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @Validated(Create.class) @RequestBody RequestDto requestDto) {
        log.info("POST создание запроса предмета = {}, пользователем ID {}, описание {} ",
                requestDto, requestorId, requestDto.getDescription());
        return requestClient.createItemRequest(requestDto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequestorId(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET списка запросов пользователя с requestorId {}", userId);
        return requestClient.getItemRequestsByRequestorId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object> getItemRequestsById(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long userId,
            @NotNull @Min(value = 1) @PathVariable Long itemRequestId) {
        log.info("GET данные о запросе с ID {}, пользователем с ID {}",itemRequestId, userId);
        return requestClient.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false) Integer size) {
        log.info("GET данные пользователем с ID {},  индекс первого элемента {}, " +
                "количество элементов для отображения {}", userId, from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }
}