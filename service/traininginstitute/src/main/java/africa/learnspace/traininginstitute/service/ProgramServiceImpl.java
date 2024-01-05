package africa.learnspace.traininginstitute.service;

import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.manager.ProgramManager;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.models.program.enums.DeliveryType;
import africa.learnspace.loan.models.program.enums.ProgramMode;
import africa.learnspace.loan.models.program.enums.ProgramStatus;
import africa.learnspace.loan.models.program.enums.ProgramType;
import africa.learnspace.traininginstitute.dto.response.ApiResponse;
import africa.learnspace.traininginstitute.dto.response.GenerateResponse;
import africa.learnspace.traininginstitute.dto.request.ProgramRequest;
import africa.learnspace.traininginstitute.exception.InstituteProgramException;
import africa.learnspace.traininginstitute.util.InstituteProgramConstant;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramManager programManager;

    @Autowired
    private InstituteManager instituteManager;

    @Autowired
    private UserManager userManager;


    @Override
    public ApiResponse createProgram(String email, ProgramRequest programRequest) {
        User user = userManager.findByEmail(email);
        Institute foundInstitute =
                instituteManager.findInstituteById(programRequest.getInstituteId())
                        .orElseThrow(() -> new InstituteProgramException(InstituteProgramConstant.TRAINING_INSTITUTE_DOES_NOT_EXIST));

        List<String> programs = programManager.findProgramNameByInstituteId(programRequest.getInstituteId());
        boolean programNameExists = programs.stream()
                .anyMatch(program -> Objects.equals(program, programRequest.getProgramName()));

        if (programNameExists) {
            throw new InstituteProgramException(InstituteProgramConstant.PROGRAM_ALREADY_EXIST);
        }

        Program instituteProgram = buildInstituteProgram(programRequest, foundInstitute, user);
        //TODO: send push notification to portfolio manager
        return GenerateResponse.createdResponse(InstituteProgramConstant.PROGRAM_ADDED_SUCCESSFULLY);
    }

    private Program buildInstituteProgram(ProgramRequest programRequest, Institute institute, User user) {
        Program instituteProgram = Program.builder()
                .institute(institute)
                .name(programRequest.getProgramName())
                .description(programRequest.getProgramDescription())
                .duration(programRequest.getProgramDuration())
                .mode(String.valueOf(ProgramMode.valueOf(programRequest.getProgramMode().toUpperCase())))
                .programType(String.valueOf(ProgramType.valueOf(programRequest.getProgramType().toUpperCase())))
                .deliveryType(String.valueOf(DeliveryType.valueOf(programRequest.getDeliveryType())))
                .programStatus(String.valueOf(ProgramStatus.INACTIVE))
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUserId())
                .build();
        programManager.saveProgram(instituteProgram);
        return instituteProgram;
    }

    @Override
    public List<Program> findAllPrograms(String instituteId) {
        Institute foundInstitute = instituteManager.findInstituteById(instituteId)
                .orElseThrow(() -> new InstituteProgramException(InstituteProgramConstant.TRAINING_INSTITUTE_DOES_NOT_EXIST));
        return programManager.findAllProgramByInstituteId(foundInstitute.getInstituteId());
    }

    @Override
    public ApiResponse findProgram(String instituteId, String programId) {
        if (!instituteManager.existsByInstituteId(instituteId)) {
           throw new InstituteProgramException(InstituteProgramConstant.TRAINING_INSTITUTE_DOES_NOT_EXIST);
        }

        Program program = programManager.findById(programId)
                .orElseThrow(()-> new InstituteProgramException(InstituteProgramConstant.PROGRAM_NOT_FOUND));

        if (!program.getInstitute().getInstituteId().equals(instituteId)) {
           throw  new InstituteProgramException(InstituteProgramConstant.PROGRAM_NOT_FOUND);
        }
        return GenerateResponse.okResponse(programManager.findById(programId));
    }

}