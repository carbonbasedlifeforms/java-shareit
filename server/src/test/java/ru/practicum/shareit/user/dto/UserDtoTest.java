package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void testUserDto() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Smith")
                .email("john.smith@matrix.org")
                .build();
        JsonContent<UserDto> jsonContent = jacksonTester.write(userDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Smith");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email")
                .isEqualTo("john.smith@matrix.org");
    }
}