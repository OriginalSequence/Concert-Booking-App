package proj.concert.service.mapper;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

public class PerformerMapper {
    public static PerformerDTO toDto(Performer performer){
        return new PerformerDTO(
            performer.getId(), 
            performer.getName(), 
            performer.getImageName(),
            performer.getGenre(),
            performer.getBlurb());
    }
}
