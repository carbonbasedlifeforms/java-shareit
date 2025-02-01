package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    private final UserDto owner = UserDto.builder()
            .name("New user")
            .email("testItems@test.org")
            .build();

    private final ItemDto itemDto = ItemDto.builder()
//            .id(1L)
            .name("New item")
            .available(true)
            .description("My very new item")
            .build();

    private final CommentDto commentDto = CommentDto.builder()
            .text("Test comment")
            .authorName("Some User")
            .build();


    @Test
    void testItemService() {
        Long ownerId = userService.createUser(owner).getId();
        ItemDto savedItem = itemService.createItem(ownerId, itemDto);
        BookingDto bookingDto = bookingService.createBooking(ownerId, BookingInDto.builder()
                .itemId(savedItem.getId())
                .start(LocalDateTime.now().minusSeconds(2))
                .end(LocalDateTime.now().minusSeconds(1))
                .build());
        bookingService.changeBookingApproved(ownerId, bookingDto.getId(), "true");

        assertThat(savedItem.getId(), notNullValue());
        assertThat(savedItem.getName(), equalTo(itemDto.getName()));
        assertThat(savedItem.getDescription(), equalTo(itemDto.getDescription()));

        List<ItemDto> itemsGotByOwner = itemService.getItemsByOwner(ownerId);

        assertThat(itemsGotByOwner.size(), equalTo(1));
        assertThat(itemsGotByOwner.getFirst().getId(), equalTo(savedItem.getId()));
        assertThat(itemsGotByOwner.getFirst().getName(), equalTo(savedItem.getName()));
        assertThat(itemsGotByOwner.getFirst().getDescription(), equalTo(savedItem.getDescription()));

        assertThat(itemService.searchItem("ite").getFirst().getName(), equalTo(savedItem.getName()));

        savedItem.setName("Blah");
        assertThat(itemService.updateItem(ownerId, savedItem.getId(), savedItem).getName(), equalTo("Blah"));

        assertThat(savedItem.getComments(), equalTo(null));

        itemService.addComment(ownerId, savedItem.getId(), commentDto);

        ItemDto itemGotById = itemService.getItemById(ownerId, savedItem.getId());

        assertThat(itemGotById.getComments().size(), equalTo(1));
        assertThat(itemGotById.getComments().getFirst().getText(), equalTo(commentDto.getText()));
        assertThat(itemGotById.getId(), equalTo(savedItem.getId()));
        assertThat(itemGotById.getName(), equalTo(savedItem.getName()));
        assertThat(itemGotById.getDescription(), equalTo(savedItem.getDescription()));
    }
}