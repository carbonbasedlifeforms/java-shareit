package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        log.info("Get item by id {}", itemId);
        return ItemMapper.toItemDto(itemRepository.getById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        checkUserExists(userId);
        log.info("Get items by owner with id {}", userId);
        return itemRepository.getByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItem(String query) {
        log.info("Search item by query{}", query);
        return itemRepository.search(query).stream()
                .map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        checkUserExists(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        log.info("Create item {}", item);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        checkUserExists(userId);
        Item item = itemRepository.getById(itemId);
        if (!item.getOwner().equals(userId)) {
            throw new ValidationException("Item owner with id %d not owned by user with id %d"
                    .formatted(itemId, userId));
        }
        ItemMapper.updateItemFields(itemDto, item);
        log.info("Update item {}", item);
        return ItemMapper.toItemDto(itemRepository.update(item));
    }

    private void checkUserExists(Long userId) {
        log.info("Check user exists with id {}", userId);
        if (userRepository.getById(userId) == null) {
            throw new ValidationException("User with id %d not found".formatted(userId));
        }
    }
}
