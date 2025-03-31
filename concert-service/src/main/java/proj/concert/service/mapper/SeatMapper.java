package proj.concert.service.mapper;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

public class SeatMapper {
	public static SeatDTO toDto(Seat s) {
		return new SeatDTO(s.getLabel(), s.getPrice());
	}
}
