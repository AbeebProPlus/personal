package africa.learnspace.web.controller.traininginstitutecontroller;

import africa.learnspace.loan.models.program.Program;
import africa.learnspace.traininginstitute.dto.request.ProgramRequest;
import africa.learnspace.traininginstitute.dto.response.ApiResponse;
import africa.learnspace.traininginstitute.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/training-institutes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class TrainingInstituteController {

    private final ProgramService programService;

    @PostMapping("/addProgram")
//    @PreAuthorize("hasRole('INSTITUTE_ADMIN')")
    public ResponseEntity<ApiResponse> createInstituteProgram(@AuthenticationPrincipal Jwt token, @Valid @RequestBody ProgramRequest programRequest) {
        String email = token.getClaim("email");
        return new ResponseEntity<>(programService.createProgram(email, programRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{instituteId}")
    public ResponseEntity<List<Program>> findAllInstitutesProgram(@PathVariable String instituteId) {
        return ResponseEntity.ok(programService.findAllPrograms(instituteId));
    }

    @GetMapping("/{instituteId}/{programId}")
    public ResponseEntity<ApiResponse> findProgram(@PathVariable String instituteId, @PathVariable String programId){
        return new ResponseEntity<>(programService.findProgram(instituteId, programId), HttpStatus.OK);
    }
}

