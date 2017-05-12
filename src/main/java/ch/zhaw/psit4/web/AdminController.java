package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {
    private final AdminServiceInterface adminServiceInterface;

    public AdminController(AdminServiceInterface adminServiceInterface) {
        this.adminServiceInterface = adminServiceInterface;
    }

    @GetMapping(path = "/admins")
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        return new ResponseEntity<>(adminServiceInterface.getAllAdmins(), HttpStatus.OK);
    }

    @GetMapping(path = "/admins/{id}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable long id) {
        return new ResponseEntity<>
                (adminServiceInterface.getAdmin(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admins/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable long id) {
        adminServiceInterface.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/admins/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable long id, @RequestBody AdminDto adminDto) {
        adminDto.setId(id);
        return new ResponseEntity<>(adminServiceInterface.updateAdmin(adminDto), HttpStatus.OK);
    }

    @PostMapping(path = "/admins")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        return new ResponseEntity<>(
                adminServiceInterface.createAdmin(adminDto), HttpStatus.CREATED);
    }
}
