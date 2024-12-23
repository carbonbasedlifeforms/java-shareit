package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto getItemById(Long userId, Long itemId);

    public List<ItemDto> getItemsByOwner(Long userId);

    public List<ItemDto> searchItem(String query);

    public ItemDto createItem(Long userId, ItemDto item);

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
