package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;
import ru.practicum.shareit.validation.ValidationNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private int itemId = 1;

    private Map<Integer,Item> items = new HashMap<>();

    private InMemoryUserStorage inMemoryUserStorage;

    public InMemoryItemStorage(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public Item createItem(Item item) {
        validationItemUser(item.getOwner());
        item.setId(itemId);
        items.put(itemId,item);
        itemId++;
        log.info("Добавлен Item {}, количество {}",item,items.size());
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item, int itemId) {
        validationItemOwnerAndOtherOwner(item, itemId);
        validationItemUser(item.getOwner());
        Item upItem = items.get(itemId);
        if (item.getName() != null) {
            upItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            upItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            upItem.setAvailable(item.getAvailable());
        }
        upItem.setId(itemId);
        items.put(itemId, upItem);
        log.info("Обновлен Item {}",upItem);
        return items.get(upItem.getId());
    }

    @Override
    public Item getItemById(int itemId) {
        validationItem(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getItems(int ownerId) {
        validationItemUser(ownerId);

        List<Item> itemList =  new ArrayList<>(items.values());
        List<Item> finalItems = new ArrayList<>();
        for (Item item : itemList){
            if (item.getOwner() == ownerId) {
                finalItems.add(item);
            }
        }
        log.info("Количество Item {}",finalItems.size());
        return finalItems;
    }

    @Override
    public List<Item> getItemsBySearch(String text) {

        List<Item> itemList =  new ArrayList<>(items.values());
        List<Item> finalItems = new ArrayList<>();
        if(!text.isEmpty()) {
            for (Item item : itemList) {
                if (item.getAvailable()) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        finalItems.add(item);
                        log.info("Совпадение Name {}",item);
                    } else if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        finalItems.add(item);
                        log.info("Совпадение Description {}",item);
                    }
                }
            }
        }
        return finalItems;
    }

    private Integer validationItemUser(int id) {
        if (inMemoryUserStorage.getUserById(id) == null) {
            throw new ValidationNotFound("Пользователь предмета не найден");
        }
        return id;
    }

    private Integer validationItem(int id) {
        if (items.get(id) == null) {
            throw new ValidationNotFound("Предмет с таким id не найден");
        }
        return id;
    }

    private Item validationItemOwnerAndOtherOwner(Item item, int id) {
        if (item.getOwner() != items.get(id).getOwner()) {
            throw new ValidationNotFound("Предмет данного пользователя не найден");
        }
        return item;
    }
}