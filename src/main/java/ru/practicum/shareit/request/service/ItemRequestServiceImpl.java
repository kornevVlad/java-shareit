package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.ValidationBadRequest;
import ru.practicum.shareit.validation.ValidationNotFound;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itReqRepository;

    private final ItemRequestMapper itReqMapper;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemRequestServiceImpl(ItemRequestRepository itReqRepository,
                                  ItemRequestMapper itReqMapper, UserRepository userRepository,
                                  ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itReqRepository = itReqRepository;
        this.itReqMapper = itReqMapper;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto,
                                            Long requestorId, LocalDateTime dataTime) {
        validUserById(requestorId);
        User requestor = userRepository.getReferenceById(requestorId);
        ItemRequest itemRequest = itReqMapper.toItemRequest(itemRequestDto, requestor, dataTime);
        itReqRepository.save(itemRequest);
        log.info("Созданный запрос предмета сохранен {}", itemRequest);
        return itReqMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequestorId(Long requestorId) {
        validUserById(requestorId);
        List<ItemRequest> itemRequests = itReqRepository.findAllByRequestorId(requestorId);
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itReq : itemRequests) {
            ItemRequestDto itemRequestDto = itReqMapper.toItemRequestDtoAndItemDto(itReq, getItemsDto(itReq.getId()));
            itemRequestDtoList.add(itemRequestDto);
        }
        log.info("Список запросов пользователя с ID {}, количество {}", requestorId, itemRequestDtoList.size());
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        validUserById(userId);
        validItemRequestById(itemRequestId);
        ItemRequest itemRequest = itReqRepository.getReferenceById(itemRequestId);
        log.info("Запрос c ID {}, запрос {}", itemRequestId, itemRequest);
        return itReqMapper.toItemRequestDtoAndItemDto(itemRequest, getItemsDto(itemRequest.getId()));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        validUserById(userId);
        Page<ItemRequest> itemRequestsPage;
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        if (size != null) {
            validFromAndSize(from, size);
            Sort sort = Sort.by(Sort.Direction.DESC, "created");
            Pageable pageable = PageRequest.of(from, size, sort);
            itemRequestsPage = itReqRepository.findAllByRequestorIdNot(userId, pageable);
            for (ItemRequest itemRequest : itemRequestsPage) {
               ItemRequestDto itemRequestDto = itReqMapper.toItemRequestDtoAndItemDto(
                       itemRequest, getItemsDto(itemRequest.getId()));
               itemRequestDtoList.add(itemRequestDto);
            }
            return itemRequestDtoList;
        }
        return itemRequestDtoList;
    }

    private void validUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id {} не найден", userId);
            throw new ValidationNotFound("Пользователь не найден");
        }
    }

    private void validItemRequestById(Long itemRequestId) {
        if (!itReqRepository.existsById(itemRequestId)) {
            log.error("Запрос с id {} не найден", itemRequestId);
            throw new ValidationNotFound("Запрос не найден");
        }
    }

    private void validFromAndSize(Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationBadRequest("Индекс эллемента не может быть меньше нуля");
        }
        if (size < 0) {
            throw new ValidationBadRequest("Размер не может быть меньше нуля");
        }
        if (size == 0) {
            throw new ValidationBadRequest("Размер списка не может быть нулевым");
        }
        if (from > size) {
            throw new ValidationBadRequest("from не может быть больше size");
        }
    }

    private List<ItemDto> getItemsDto(Long requestId) {
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        if (items != null) {
            return items.stream()
                    .map(itemMapper::toItemDto).collect(toList());
        } else {
            return new ArrayList<>();
        }
    }
}
