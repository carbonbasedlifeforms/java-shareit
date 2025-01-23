package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.OnCreate;

import java.util.List;

import static ru.practicum.shareit.common.GlobalVariables.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("getting item with id itemId: {}, userId: {}", userId, itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("getting user items with userId: {}", userId);
        return itemService.getItemsByOwner(userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                              @Validated(OnCreate.class) @Valid @RequestBody ItemDto itemDto) {
        log.info("creating item with userId: {}, itemDto: {}", userId, itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("updating item with userId: {}, itemId: {}, itemDto: {}", userId, itemId, itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                    @RequestParam(name = "text") String query) {
        log.info("searching item with userId: {}, query: {}", userId, query);
        return itemService.searchItem(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid@RequestBody CommentDto commentDto) {
        log.info("adding comment with userId: {}, itemId: {}, commentDto: {}", userId, itemId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}/comment")
    public List<CommentDto> getComments(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("getting comments with userId: {}, itemId: {}", userId, itemId);
        return itemService.getComments(userId, itemId);
    }
}
