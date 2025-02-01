package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto saveRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findRequestsByUserId(Long userId);

    List<ItemRequestDto> findAllRequests(Long userId);

    ItemRequestDto findRequestById(Long userId, Long id);
}
