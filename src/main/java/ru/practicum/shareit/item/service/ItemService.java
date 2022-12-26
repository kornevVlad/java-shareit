package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto getItemById(Long id);

    List<ItemDto> getItems(Long ownerId);

    List<ItemDto> getItemsBySearch(String text);
}