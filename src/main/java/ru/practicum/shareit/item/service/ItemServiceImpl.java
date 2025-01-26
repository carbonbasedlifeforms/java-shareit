package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        log.info("Get item by id {}", itemId);
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId))));
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItemIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(itemId, BookingStatus.APPROVED,
                        LocalDateTime.now(), LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId,
                LocalDateTime.now());
        itemDto.setLastBooking(lastBooking.map(Booking::getStart).orElse(null));
        itemDto.setNextBooking(nextBooking.map(Booking::getStart).orElse(null));
        List<CommentDto> commentsDto = commentRepository.findAllByAuthor_Id(userId).stream()
                .map(CommentMapper::toCommentDto).toList();
        itemDto.setComments(commentsDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        log.info("Get items by owner with id {}", userId);
        return itemRepository.findByOwner(getUserIfExists(userId)).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItem(String query) {
        log.info("Search item by query{}", query);
        if (query.isBlank()) {
            log.info("Query string is empty");
            return List.of();
        }
        return itemRepository.search(query).stream()
                .map(ItemMapper::toItemDto).toList();
    }

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto, getUserIfExists(userId)) ;
        if (itemDto.getRequestId() != null) {
            itemRequestRepository.findById(itemDto.getRequestId())
                    .ifPresent(item::setRequest);
        }
        log.info("Create item {}", item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        getUserIfExists(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId)));
        if (!item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Item owner with id %d not owned by user with id %d"
                    .formatted(itemId, userId));
        }
        ItemMapper.updateItemFields(itemDto, item);
        log.info("Update item {}", item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = getUserIfExists(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId)));
        bookingRepository.findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(itemId, userId,
                LocalDateTime.now(), BookingStatus.APPROVED)
                .orElseThrow(() -> new ValidationException(
                        "Comment for item with id %d and user with id %d is not possible"
                                .formatted(itemId, userId)));
        Comment comment = CommentMapper.toComment(commentDto, author, item);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getComments(Long userId, Long itemId) {
        return List.of();
    }

    private User getUserIfExists(Long userId) {
        log.info("Check user exists with id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
    }
}
