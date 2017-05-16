package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.PasswordOnlyDto;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1/admins",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {
    private final AdminServiceInterface adminServiceInterface;

    public AdminController(AdminServiceInterface adminServiceInterface) {
        this.adminServiceInterface = adminServiceInterface;
    }

    @GetMapping()
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        return new ResponseEntity<>(adminServiceInterface.getAllAdmins(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable long id) {
        return new ResponseEntity<>
                (adminServiceInterface.getAdmin(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable long id) {
        adminServiceInterface.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable long id, @Validated @RequestBody AdminDto adminDto) {
        adminDto.setId(id);
        return new ResponseEntity<>(adminServiceInterface.updateAdmin(adminDto), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AdminDto> createAdmin(@Validated @RequestBody AdminWithPasswordDto adminDto) {
        return new ResponseEntity<>(
                adminServiceInterface.createAdmin(adminDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable long id, @RequestBody PasswordOnlyDto passwordOnlyDto) {
        adminServiceInterface.changePassword(id, passwordOnlyDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
