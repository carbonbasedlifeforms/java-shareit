package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Set<Item>> ownerItems = new HashMap<>();

    @Override
    public Item getById(Long id) {
        log.info("Get item with id {}", id);
        return Optional.ofNullable(items.get(id))
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(id)));
    }

    @Override
    public List<Item> getByOwner(Long userId) {
        log.info("Get items by userId {}", userId);
        return ownerItems.get(userId).stream().toList();
    }

    @Override
    public List<Item> search(String query) {
        log.info("Get items by query {}", query);
        return items.values().stream()
                .filter(item -> item.getName().equalsIgnoreCase(query))
                .filter(Item::getAvailable)
                .toList();
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        ownerItems.computeIfAbsent(item.getOwner(), value -> new HashSet<>()).add(item);
        log.info("Created item {}", item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        ownerItems.computeIfAbsent(item.getOwner(), value -> new HashSet<>()).add(item);
        log.info("Updated item {}", item);
        return item;
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
