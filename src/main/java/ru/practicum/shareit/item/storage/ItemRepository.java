package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(User user);

    @Query("""
            select i
            from Item i
            where i.available = true
            and (lower(i.name) like lower(concat('%', :query, '%'))
            or i.description like lower(concat('%', :query, '%')))
            """)
    List<Item> search(String query);

    Optional<Item> findByIdAndAvailableIsTrue(Long id);

    List<Item> findAllByRequestIdIn(List<Long> Idlist);

    List<Item> findAllByRequestId(Long id);
}
