package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.ValidationBadRequest;
import ru.practicum.shareit.validation.ValidationNotFound;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Lazy
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;


    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository,
                           UserRepository userRepository, BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = validationUserById(ownerId);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto,owner)));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        User owner = validationUserById(ownerId);
        Item upItem = validationItemById(itemId);
        if (!upItem.getOwner().getId().equals(owner.getId())) {
            log.error("Предмем пользователя с id {} не найден", ownerId);
            throw new ValidationNotFound("Предмет пользователя не найден");
        }
        Item item = itemMapper.toItem(itemDto,owner);
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
        return itemMapper.toItemDto(itemRepository.save(upItem));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        validationItemById(itemId);
        Item item = itemRepository.getReferenceById(itemId);
        return getFinalDto(item, userId);
    }

    @Override
    public List<ItemDto> getItems(Long ownerId) {
        validationUserById(ownerId);
        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(ownerId);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(getFinalDto(item, ownerId));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<Item> itemList = itemRepository.findAll();
        List<Item> finalItems = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : itemList) {
                if (item.getAvailable() == ItemStatus.TRUE) {
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
        for (Item item : finalItems) {
            itemsDto.add(itemMapper.toItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public CommentDto createComment(RequestCommentDto requestCommentDto, Long itemId, Long userId) {
        Item item = validationItemById(itemId);
        User user = validationUserById(userId);
        Booking booking = validationBookingByUserIdAndItemId(itemId, userId);
        Comment comment = itemMapper.toComment(requestCommentDto,user,item);
        commentRepository.save(comment);
        return itemMapper.toCommentDto(comment);
    }


    private List<CommentDto> getCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(itemMapper.toCommentDto(comment));
        }
        return commentDtos;
    }

    private User validationUserById(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            log.error("Пользователь с id {} не найден", ownerId);
            throw new ValidationNotFound("Пользователь не найден");
        } else {
            log.info("Пользователь с id {} прошел верификацию", ownerId);
            return userRepository.getReferenceById(ownerId);
        }
    }

    private Item validationItemById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.error("Предмет с id {} не найден", itemId);
            throw new ValidationNotFound("Предмет с таким id не найден");
        } else {
            log.info("Предмет с id {} найден", itemId);
            return itemRepository.getReferenceById(itemId);
        }
    }

    private Booking validationBookingByUserIdAndItemId(Long itemId, Long userId) {
         Booking booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBefore(
                 itemId, userId, LocalDateTime.now());
         if (booking == null) {
             log.error("Пользователь с ID={} не бронировал предмет с ID={}",userId, itemId);
             throw new ValidationBadRequest("Пользователь не бронировал предмет");
         } else {
             return booking;
         }
    }

    private ItemDto getFinalDto(Item item, Long userId) {
        ItemDto itemDto;
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = bookingRepository
                    .findFirstByItemIdAndEndIsBeforeOrderByEndDesc(item.getId(), LocalDateTime.now());
            Booking nextBooking = bookingRepository
                    .findFirstByItemIdAndStartIsAfter(item.getId(), LocalDateTime.now());

            itemDto = itemMapper.toItemAndBookingDto(item, itemMapper.toLastBookingDto(lastBooking),
                    itemMapper.toNextBookingDto(nextBooking), getCommentsByItemId(item.getId()));

        } else {
            itemDto = itemMapper.toItemAndBookingDto(item, itemMapper.toLastBookingDto(null),
                    itemMapper.toNextBookingDto(null), getCommentsByItemId(item.getId()));
        }
        return itemDto;
    }
}