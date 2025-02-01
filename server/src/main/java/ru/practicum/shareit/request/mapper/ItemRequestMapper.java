package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface ItemRequestMapper {

    @Mapping(target = "requester", source = "itemRequest.requester.id")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @Mapping(target = "requester", source = "itemRequest.requester.id")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "items", source = "itemDtoList")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> itemDtoList);

    @Mapping(target = "id", source = "itemRequestDto.id")
    @Mapping(target = "requester", source = "user")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user);
}
