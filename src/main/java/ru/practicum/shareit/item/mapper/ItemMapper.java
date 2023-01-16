package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.LastAndNextBookingDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }

    public LastAndNextBookingDto toLastBookingDto(Booking last) {
        if (last != null) {
            LastAndNextBookingDto lastBooking = new LastAndNextBookingDto();
            lastBooking.setId(last.getId());
            lastBooking.setBookerId(last.getBooker().getId());
            return lastBooking;
        } else {
            return null;
        }
    }

    public LastAndNextBookingDto toNextBookingDto(Booking next) {
        if (next != null) {
            LastAndNextBookingDto nextBooking = new LastAndNextBookingDto();
            nextBooking.setId(next.getId());
            nextBooking.setBookerId(next.getBooker().getId());
            return nextBooking;
        } else {
            return null;
        }
    }

    public ItemDto toItemAndBookingDto(Item item, LastAndNextBookingDto last, LastAndNextBookingDto next,
                                       List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setLastBooking(last);
        itemDto.setNextBooking(next);
        itemDto.setComments(comments);
        return itemDto;
    }

    public Comment toComment(RequestCommentDto requestCommentDto, User author, Item item) {
        Comment comment = new Comment();
        comment.setText(requestCommentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}