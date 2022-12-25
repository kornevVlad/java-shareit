package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping() //создание предмета
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Post Item {}, userId {}",itemDto,ownerId);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}") //обновление предмета
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable int itemId,
                              @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Patch Item {},itemId {}, userId {}",itemDto,itemId,ownerId);
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}") //получение предмета по id
    public ItemDto getItemById(@PathVariable int itemId) {
        log.info("Get itemId {}",itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping() //список предметов
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Get список предметов");
        return itemService.getItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchQuery(@RequestParam String text) {
        log.info("GET текстом {}", text);
        return itemService.getItemsBySearch(text);
    }
}