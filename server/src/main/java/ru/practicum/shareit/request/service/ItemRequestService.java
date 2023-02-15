package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto,Long requestorId,
                                     LocalDateTime dataTime);

    List<ItemRequestDto> getItemRequestsByRequestorId(Long requestorId);

    ItemRequestDto getItemRequestById(Long itemRequestId, Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
