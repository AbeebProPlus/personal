package africa.learnspace.traininginstitute.service;

import africa.learnspace.loan.models.program.Program;
import africa.learnspace.traininginstitute.dto.request.ProgramRequest;
import africa.learnspace.traininginstitute.dto.response.ApiResponse;

import java.util.List;

public interface ProgramService {
    ApiResponse createProgram(String email, ProgramRequest programRequest);
    List<Program> findAllPrograms(String instituteId);
    ApiResponse findProgram(String instituteId, String programId);

}
