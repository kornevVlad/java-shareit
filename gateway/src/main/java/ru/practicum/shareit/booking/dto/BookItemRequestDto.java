package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation_interface.Create;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {

	@NotNull(groups = Create.class)
	private long itemId;
	@FutureOrPresent(groups = Create.class)
	private LocalDateTime start;
	@Future(groups = Create.class)
	private LocalDateTime end;
}
