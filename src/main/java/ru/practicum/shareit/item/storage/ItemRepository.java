package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item getById(Long id);

    List<Item> getByOwner(Long userId);

    List<Item> search(String query);

    Item create(Item item);

    Item update(Item item);
}
