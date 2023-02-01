package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.status.ItemStatus;


import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Предмет");
        itemDto.setDescription("Предмет предмет");
        itemDto.setAvailable(ItemStatus.TRUE);
        return itemDto;
    }

    @Test
    void goodTest() throws IOException {
        JsonContent<ItemDto> result = json.write(getItemDto());

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Предмет");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Предмет предмет");
    }
}
