package proj.concert.service.mapper;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import java.util.*;
import java.time.LocalDateTime;


public class ConcertMapper {
	public static ConcertDTO toDto(Concert concert){
        List<Performer> performers = concert.getPerformers();
    	List<PerformerDTO> performersDTO = new ArrayList<>();
        for (Performer p : performers){
            performersDTO.add(PerformerMapper.toDto(p));
        }
        Set<LocalDateTime> dates = concert.getDates();
        List<LocalDateTime> listDates = new ArrayList<>(dates);
        ConcertDTO concertDTO = new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb()
        );
        concertDTO.setDates(listDates);
        concertDTO.setPerformers(performersDTO);
		return concertDTO;
	}

}
