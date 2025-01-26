package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.common.GlobalVariables.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("create item request: {}", itemRequestDto);
        return itemRequestService.saveRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("get user requests: {}", userId);
        return itemRequestService.findRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("get requests for other users");
        return itemRequestService.findAllRequests(userId);
    }

    @GetMapping({("/{requestId}")})
    public ItemRequestDto getRequestById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                         @PathVariable Long requestId) {
        log.info("get request by id: {}", requestId);
        return itemRequestService.findRequestById(userId, requestId);
    }

}
