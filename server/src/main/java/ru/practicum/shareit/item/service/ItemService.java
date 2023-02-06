package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItems(Long ownerId);

    List<ItemDto> getItemsBySearch(String text);

    CommentDto createComment(RequestCommentDto requestCommentDto, Long itemId, Long userId);
}