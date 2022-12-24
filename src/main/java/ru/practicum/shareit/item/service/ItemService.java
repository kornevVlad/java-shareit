package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, int ownerId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId);

    ItemDto getItemById(int id);

    List<ItemDto> getItems(int ownerId);

    List<ItemDto> getItemsBySearch(String text);
}