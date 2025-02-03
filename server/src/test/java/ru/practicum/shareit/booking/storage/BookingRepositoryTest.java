package ru.practicum.shareit.booking.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {
    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1);
    private static final LocalDateTime PAST = LocalDateTime.of(2019, 1, 1, 5, 1, 1);
    private static final LocalDateTime END = LocalDateTime.of(2020, 1, 1, 1, 1, 1);

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    User savedUser;
    Item savedItem;
    Booking savedBooking;

    private User user = User.builder()
            .name("test booking user")
            .email("bookingTestUser@test.org")
            .build();

    private Item item = Item.builder()
            .name("Test booking item")
            .available(true)
//            .owner(user)
            .description("Test booking description")
            .build();

    private Booking booking = Booking.builder()
//            .item(item)
//            .booker(user)
            .status(BookingStatus.WAITING)
            .start(START)
            .end(END)
            .build();

    @BeforeEach
    void beforeEach() {
        savedUser = userRepository.save(user);
        item.setOwner(savedUser);
        savedItem = itemRepository.save(item);
        booking.setItem(savedItem);
        booking.setBooker(savedUser);
        savedBooking = bookingRepository.save(booking);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getByUserIdOrOwnerItem() {
        Optional<Booking> result = bookingRepository.getByUserIdOrOwnerItem(booking.getId(), savedUser.getId());
        assertThat(result.isPresent(), equalTo(true));
        assertThat(savedBooking, equalTo(result.get()));
    }

    @Test
    void findAllByBookerId() {
        List<Booking> result = bookingRepository.findAllByBookerId(savedUser.getId());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndBefore(savedUser.getId(), LocalDateTime.now());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartAfter(savedUser.getId(), PAST);
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByBookerAndNowBetweenStartAndEnd() {
        List<Booking> result = bookingRepository
                .findAllByBookerAndNowBetweenStartAndEnd(savedUser.getId(), LocalDateTime.now());
        assertThat(result.size(), equalTo(0));
    }

    @Test
    void findAllByBookerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatus(savedUser.getId(), BookingStatus.WAITING);
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByItemOwnerId() {
        List<Booking> result = bookingRepository.findAllByItemOwnerId(savedItem.getId());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByOwnerAndNowBetweenStartAndEnd() {
        List<Booking> result = bookingRepository
                .findAllByOwnerAndNowBetweenStartAndEnd(savedItem.getId(), LocalDateTime.now());
        assertThat(result.size(), equalTo(0));
    }

    @Test
    void findAllByItemOwnerIdAndEndBefore() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndEndBefore(savedItem.getId(),
                LocalDateTime.now());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByItemOwnerIdAndStartAfter() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartAfter(savedItem.getId(), PAST);
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStatus(savedItem.getId(),
                BookingStatus.WAITING);
        assertThat(result.size(), equalTo(1));
        assertThat(result.getLast(), equalTo(savedBooking));
    }

    @Test
    void findFirstByItemIdAndBookerIdAndEndBeforeAndStatus() {
        Optional<Booking> result = bookingRepository.findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(
                savedItem.getId(), savedUser.getId(), LocalDateTime.now(), BookingStatus.WAITING);
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalTo(savedBooking));
    }

//    @Test
//    void findFirstByItemIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc() {
//        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(
//                savedItem.getId(), BookingStatus.WAITING, LocalDateTime.now(), LocalDateTime.now());
//        assertThat(result.isPresent(), equalTo(true));
//        assertThat(result.get(), equalTo(savedBooking));
//    }
//
//    @Test
//    void findFirstByItemIdAndStartAfterOrderByStartAsc() {
//        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(savedItem.getId(),
//                LocalDateTime.now());
//        assertThat(result.isPresent(), equalTo(true));
//        assertThat(result.get(), equalTo(savedBooking));
//    }
}