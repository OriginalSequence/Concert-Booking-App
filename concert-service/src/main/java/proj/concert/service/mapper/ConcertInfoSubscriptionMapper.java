package proj.concert.service.mapper;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

public class ConcertInfoSubscriptionMapper {
	public static ConcertInfoSubscription toDomainModel(ConcertInfoSubscriptionDTO dto) {
		return new ConcertInfoSubscription(dto.getConcertId(), dto.getDate(), dto.getPercentageBooked());
	}
}
