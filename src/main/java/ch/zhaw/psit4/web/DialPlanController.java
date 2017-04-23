package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dial Plan REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class DialPlanController {
    private final DialPlanServiceInterface dialPlanServiceInterface;

    public DialPlanController(DialPlanServiceInterface dialPlanServiceInterface) {
        this.dialPlanServiceInterface = dialPlanServiceInterface;
    }

    @GetMapping(path = "/dialplans", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<DialPlanDto>> getAllDialPlans() {
        return new ResponseEntity<>(dialPlanServiceInterface.getAllDialPlans(), HttpStatus.OK);
    }

    @GetMapping(path = "/dialplans/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DialPlanDto> getDialPlan(@PathVariable long id) {
        return new ResponseEntity<>(dialPlanServiceInterface.getDialPlan(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/dialplans/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteDialPlan(@PathVariable long id) {
        dialPlanServiceInterface.deleteDialPlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/dialplans/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DialPlanDto> updateDialPlan(@PathVariable long id, @RequestBody DialPlanDto dialPlanDto) {
        dialPlanDto.setId(id);
        return new ResponseEntity<>(dialPlanServiceInterface.updateDialPlan(dialPlanDto), HttpStatus.OK);
    }

    @PostMapping(path = "/dialplans", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DialPlanDto> createDialPlan(@RequestBody DialPlanDto dialPlanDto) {
        return new ResponseEntity<>(dialPlanServiceInterface.createDialPlan(dialPlanDto), HttpStatus.CREATED);
    }

}
