package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;


    public ItemServiceImpl(ItemStorage itemStorage,ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        return itemMapper.toItemDto(itemStorage.createItem(itemMapper.toItem(itemDto,ownerId)));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        return itemMapper.toItemDto(itemStorage.updateItem(itemMapper.toItem(itemDto, ownerId), itemId));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItems(Long ownerId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemStorage.getItems(ownerId)) {
            itemsDto.add(itemMapper.toItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemStorage.getItemsBySearch(text)) {
            itemsDto.add(itemMapper.toItemDto(item));
        }
        return itemsDto;
    }
}