package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItServer.USER_ID_HEADER;


@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    private static final LocalDateTime REQUEST_TIME = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 0);

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("I need something")
            .requester(1L)
            .created(REQUEST_TIME)
            .build();

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Some thing")
            .description("Some kind of useful thing")
            .requestId(1L)
            .available(true)
            .build();


    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.saveRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())));
    }

    @Test
    void getUserRequests() throws Exception {
        itemRequestDto.setItems(List.of(itemDto));
        when(itemRequestService.findRequestsByUserId(anyLong()))
                .thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId()), Long.class));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.findAllRequests(anyLong()))
                .thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].items", nullValue()));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.findRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())));
    }
}