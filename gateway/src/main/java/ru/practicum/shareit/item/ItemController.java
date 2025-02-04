package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static ru.practicum.shareit.common.GlobalVariables.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("getting item with id itemId: {}, userId: {}", userId, itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("getting user items with userId: {}", userId);
        return itemClient.getUserItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @RequestBody ItemDto itemDto) {
        log.info("creating item with userId: {}, itemDto: {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("updating item with userId: {}, itemId: {}, itemDto: {}", userId, itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @RequestParam(name = "text") String query) {
        log.info("searching item with userId: {}, query: {}", userId, query);
        return itemClient.searchItem(userId, query);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.info("adding comment with userId: {}, itemId: {}, commentDto: {}", userId, itemId, commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}/comment")
    public ResponseEntity<Object> getComments(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("getting comments with userId: {}, itemId: {}", userId, itemId);
        return itemClient.getComments(userId, itemId);
    }
}
