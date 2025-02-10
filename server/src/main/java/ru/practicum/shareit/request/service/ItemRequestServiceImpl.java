package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional
    @Override
    public ItemRequestDto saveRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = checkUserExists(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, user);
        log.info("save user {} request {} for item", userId, itemRequestDto);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findRequestsByUserId(Long userId) {
        checkUserExists(userId);
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterId(userId);
        List<Long> requestIdList = itemRequestList.stream()
                .map(ItemRequest::getId)
                .toList();
        List<Item> items = itemRepository.findAllByRequestIdIn(requestIdList);
        for (ItemRequest itemRequest : itemRequestList) {
            List<ItemDto> itemDtoList = items.stream()
                    .filter(item -> item.getRequest().getId().equals(itemRequest.getId()))
                    .map(ItemMapper::toItemDto)
                    .toList();
            itemRequestDtoList.add(itemRequestMapper.toItemRequestDto(itemRequest, itemDtoList));
        }
        log.info("find user {} requests", userId);
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> findAllRequests(Long userId) {
        checkUserExists(userId);
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterIdNot(userId);
        List<Long> requestIdList = itemRequestList.stream()
                .map(ItemRequest::getId)
                .toList();
        List<Item> items = itemRepository.findAllByRequestIdIn(requestIdList);
        for (ItemRequest itemRequest : itemRequestList) {
            List<ItemDto> itemDtoList = items.stream()
                    .filter(item -> item.getRequest().getId().equals(itemRequest.getId()))
                    .map(ItemMapper::toItemDto)
                    .toList();
            itemRequestDtoList.add(itemRequestMapper.toItemRequestDto(itemRequest, itemDtoList));
        }
        log.info("find other users requests");
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto findRequestById(Long userId, Long id) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item request not found"));
        List<ItemDto> items = itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.info("find user {} request {}", userId, id);
        return itemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
