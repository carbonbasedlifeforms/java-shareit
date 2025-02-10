package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

    private LocalDateTime nextBooking;

    private LocalDateTime lastBooking;

    private List<CommentDto> comments;

    public Boolean hasName() {
        return name != null;
    }

    public Boolean hasDescription() {
        return description != null;
    }

    public Boolean hasAvailable() {
        return available != null;
    }
}
