package proj.concert.service.mapper;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import java.util.*;

public class BookingMapper {
	public static BookingDTO toDto(Booking book) {
		List<SeatDTO> seatsDto = new ArrayList<>();
		List<Seat> seats = book.getSeats();
		for (Seat seat: seats) seatsDto.add(SeatMapper.toDto(seat));
		return new BookingDTO(book.getConcertId(), book.getDate(), seatsDto);
	}
}
