package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.actions.TeamAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

/**
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class ActionController {

    @GetMapping(path = "/action/{id}", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ActionDto> getAction(@PathVariable long id) {
        ActionDto actionDto = new ActionDto();
        TeamAction teamAction = new TeamAction();
        actionDto.setId(123);
        actionDto.setName("my action");
        actionDto.setTypeSpecific(teamAction);
        return new ResponseEntity<>(actionDto, HttpStatus.OK);
    }

    @PostMapping(path = "/action", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ActionDto> updateAction(@RequestBody ActionDto actionDto) {
        LinkedHashMap linkedHashMap;
        linkedHashMap = (LinkedHashMap) actionDto.getTypeSpecific();
        TeamAction teamAction;
        if (actionDto.getType() == ActionDto.ActionType.TEAM) {
            ObjectMapper objMapper = new ObjectMapper();
            teamAction = objMapper.convertValue(linkedHashMap, TeamAction.class);
        }

        return new ResponseEntity<>(actionDto, HttpStatus.CREATED);
    }

}
