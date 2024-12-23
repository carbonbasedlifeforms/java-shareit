package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public static Item toItem(ItemDto itemDto, Long userId) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userId)
                .request(itemDto.getRequest())
                .build();
    }

    public static void updateItemFields(ItemDto itemDto, Item item) {
        if (itemDto.hasName())
            item.setName(itemDto.getName());
        if (itemDto.hasDescription())
            item.setDescription(itemDto.getDescription());
        if (itemDto.hasAvailable())
            item.setAvailable(itemDto.getAvailable());
    }
}
