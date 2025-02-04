package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.common.GlobalVariables.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 0);
    private static final LocalDateTime END = START.plusDays(1);

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private final BookingInDto bookingInDto = BookingInDto.builder()
            .itemId(1L)
            .start(START)
            .end(END)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(START)
            .end(END)
            .item(ItemDto.builder().id(1L).build())
            .booker(UserDto.builder().id(1L).build())
            .build();

    private final BookingDto bookingDtoApproved = bookingDto;

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(bookingInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
                .andExpect(jsonPath("$.start", is(START.toString())))
                .andExpect(jsonPath("$.end", is(END.toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    void changeBookingApproved() throws Exception {
        bookingDtoApproved.setStatus(BookingStatus.APPROVED);
        when(bookingService.changeBookingApproved(anyLong(), anyLong(), any()))
                .thenReturn(bookingDtoApproved);
        mockMvc.perform(patch("/bookings/{bookingId}", bookingDtoApproved.getId())
                        .header(USER_ID_HEADER, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoApproved.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoApproved.getStatus().name())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
                .andExpect(jsonPath("$.start", is(START.toString())))
                .andExpect(jsonPath("$.end", is(END.toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingService.getUserBookings(anyLong(), anyString()))
                .thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$"), hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())))
                .andExpect(jsonPath("$[0].start", is(START.toString())))
                .andExpect(jsonPath("$[0].end", is(END.toString())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    void getBookingsForOwnerItems() throws Exception {
        when(bookingService.getBookingsForOwnerItems(anyLong(), anyString()))
                .thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())));
    }
}