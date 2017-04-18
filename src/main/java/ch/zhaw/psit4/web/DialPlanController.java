package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Dial Plan REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class DialPlanController {

    @GetMapping(path = "/dialplan/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DialPlanDto> getDialPlan() {
        //TODO
        DialPlanDto dialPlanDto = new DialPlanDto();
        return new ResponseEntity<>(dialPlanDto, HttpStatus.OK);
    }

    @PostMapping(path = "/dialplan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DialPlanDto> createDialPlan(@RequestBody DialPlanDto dialPlanDto) {
        // TODO
        for (ActionDto actionDto : dialPlanDto.getActions()) {
            if (actionDto.getType() == ActionDto.ActionType.TEAM) {
                ObjectMapper objMapper = new ObjectMapper();
                DialAction dialAction = objMapper.convertValue(actionDto.getTypeSpecific(), DialAction.class);
                String test = dialAction.getTime();
            }
        }
        return new ResponseEntity<>(dialPlanDto, HttpStatus.CREATED);
    }

}
