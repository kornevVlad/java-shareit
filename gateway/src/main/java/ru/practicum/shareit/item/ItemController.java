package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation_interface.Create;
import ru.practicum.shareit.validation_interface.Update;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping() //создание предмета
    public ResponseEntity<Object> createItem(
            @Validated(Create.class) @RequestBody ItemDto itemDto,
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Post Item {}, userId {}",itemDto,ownerId);
        return itemClient.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}") //обновление предмета
    public ResponseEntity<Object> updateItem(
            @Validated(Update.class) @RequestBody ItemDto itemDto,
            @NotNull @Min(value = 1) @PathVariable Long itemId,
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Patch Item {},itemId {}, userId {}",itemDto,itemId,ownerId);
        return itemClient.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}") //получение предмета по id
    public ResponseEntity<Object> getItemById(
            @NotNull @Min(value = 1) @PathVariable Long itemId,
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get itemId {}",itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping() //список предметов
    public ResponseEntity<Object> getItems(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Get список предметов");
        return itemClient.getItems(ownerId);
    }

    @GetMapping("/search") //список предметов совпадение запроса по тексту
    public ResponseEntity<Object> getItemsBySearchQuery(@RequestParam String text) {
        log.info("GET текстом {}", text);
        return itemClient.getItemsBySearch(text);
    }

    @PostMapping("/{itemId}/comment") //создание комментария
    public ResponseEntity<Object> addComment(
            @RequestBody @Validated(Create.class) CommentDto commentDto,
            @NotNull @Min(value = 1) @PathVariable Long itemId,
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }
}