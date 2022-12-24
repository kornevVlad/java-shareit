package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(Item item, int itemId);

    Item getItemById(int itemId);

    List<Item> getItems(int ownerId);

    List<Item> getItemsBySearch(String text);
}