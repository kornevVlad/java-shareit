package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(Item item, Long itemId);

    Item getItemById(Long itemId);

    List<Item> getItems(Long ownerId);

    List<Item> getItemsBySearch(String text);
}