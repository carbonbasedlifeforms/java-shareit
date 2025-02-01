package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getItemsByOwner(Long userId);

    List<ItemDto> searchItem(String query);

    ItemDto createItem(Long userId, ItemDto item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentDto> getComments(Long userId, Long itemId);
}
