package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .request(itemDto.getRequest() != null ? ItemRequest.builder()
                        .id(itemDto.getRequest()).build() : null)
                .build();
    }

    public void updateItemFields(ItemDto itemDto, Item item) {
        if (itemDto.hasName())
            item.setName(itemDto.getName());
        if (itemDto.hasDescription())
            item.setDescription(itemDto.getDescription());
        if (itemDto.hasAvailable())
            item.setAvailable(itemDto.getAvailable());
    }
}
