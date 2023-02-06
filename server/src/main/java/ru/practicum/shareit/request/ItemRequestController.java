package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST создание запроса предмета = {}, пользователем ID {} ",itemRequestDto, requestorId);
        return itemRequestService.createItemRequest(itemRequestDto, requestorId, LocalDateTime.now());
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequestorId(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET списка запросов пользователя с requestorId {}", userId);
        return itemRequestService.getItemRequestsByRequestorId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestsById(@RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemRequestId) {
        log.info("GET данные о запросе с ID {}, пользователем с ID {}",itemRequestId, userId);
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(required = false) Integer size) {
        log.info("GET данные пользователем с ID {},  индекс первого элемента {}, " +
                        "количество элементов для отображения {}", userId, from, size);
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
