package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public Item getById(Long id);

    public List<Item> getByOwner(Long userId);

    public List<Item> search(String query);

    public Item create(Item item);

    public Item update(Item item);
}
