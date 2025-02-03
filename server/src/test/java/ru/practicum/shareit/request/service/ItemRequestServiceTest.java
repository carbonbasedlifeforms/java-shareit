package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;


    private UserDto createdUser;
    private ItemRequestDto savedRequest;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Need something")
            .build();

    private final UserDto userDto = UserDto.builder()
            .name("John Smith")
            .email("testRequests@test.org")
            .build();

    @Test
    void testItemRequestService() {
        createdUser = userService.createUser(userDto);
        savedRequest = itemRequestService.saveRequest(createdUser.getId(), itemRequestDto);

        assertThat(savedRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(savedRequest.getId(), notNullValue());
        assertThat(savedRequest.getRequester(), equalTo(createdUser.getId()));
        assertThat(savedRequest.getCreated(), notNullValue());

        List<ItemRequestDto> requestsByUserId = itemRequestService.findRequestsByUserId(createdUser.getId());
        List<ItemRequestDto> allRequests = itemRequestService.findAllRequests(createdUser.getId());
        ItemRequestDto requestById = itemRequestService.findRequestById(createdUser.getId(), savedRequest.getId());

        assertThat(requestsByUserId.size(), equalTo(1));
        assertThat(allRequests.size(), notNullValue());
        assertThat(requestById.getId(), equalTo(savedRequest.getId()));

        assertThrows(NotFoundException.class, () -> itemRequestService.findRequestById(createdUser.getId(), -1L));
        assertThrows(NotFoundException.class, () -> itemRequestService.findRequestById(-1L,
                savedRequest.getId()));
    }

}