package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.ShareItGateway.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                    @RequestBody ItemRequestDto itemRequestDto) {
        log.info("create item request: {}", itemRequestDto);
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("get user requests: {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("get requests for other users");
        return itemRequestClient.getOtherRequests(userId);
    }

    @GetMapping({("/{requestId}")})
    public ResponseEntity<Object> getRequestById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("get request by id: {}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

}
