package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Need something")
            .build();

    private final UserDto userDto = UserDto.builder()
            .name("John Smith")
            .email("testRequests@test.org")
            .build();

    @Test
    void testItemRequestService() {
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto savedRequest = itemRequestService.saveRequest(createdUser.getId(), itemRequestDto);

        assertThat(savedRequest.getDescription(), equalTo(itemRequestDto.getDescription()));

        List<ItemRequestDto> requestsByUserId = itemRequestService.findRequestsByUserId(createdUser.getId());
        List<ItemRequestDto> allRequests = itemRequestService.findAllRequests(createdUser.getId());
        ItemRequestDto requestById = itemRequestService.findRequestById(createdUser.getId(), savedRequest.getId());

        assertThat(requestsByUserId.size(), equalTo(1));
        assertThat(allRequests.size(), equalTo(0));
        assertThat(requestById.getId(), equalTo(savedRequest.getId()));
    }
}