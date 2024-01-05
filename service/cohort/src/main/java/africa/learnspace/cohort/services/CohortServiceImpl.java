package africa.learnspace.cohort.services;

import africa.learnspace.cohort.data.request.CreateCohortRequest;
import africa.learnspace.cohort.data.request.CohortRequest;
import africa.learnspace.cohort.data.request.ViewCohortsRequest;
import africa.learnspace.cohort.data.response.CohortResponse;
import africa.learnspace.cohort.data.response.CreateCohortResponse;
import africa.learnspace.cohort.data.response.ViewCohortsResponse;
import africa.learnspace.cohort.exceptions.InvalidInputException;
import africa.learnspace.cohort.mappers.CohortMapper;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.loan.manager.CohortManager;
import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.manager.ProgramManager;
import africa.learnspace.loan.manager.TraineeManager;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.models.trainee.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.util.ObjectUtils;



@Service
public class CohortServiceImpl implements CohortService {

    private final InstituteManager instituteRepository;
    private final ProgramManager programRepository;
    private final CohortManager cohortRepository;
    private final TraineeManager traineeRepository;
    private final CohortMapper cohortMapper;


    @Autowired
    public CohortServiceImpl(InstituteManager instituteRepository,
                             ProgramManager programRepository,
                             CohortManager cohortRepository,
                             TraineeManager traineeRepository,
                             CohortMapper cohortMapper) {
        this.instituteRepository = instituteRepository;
        this.programRepository = programRepository;
        this.cohortRepository = cohortRepository;
        this.traineeRepository = traineeRepository;
        this.cohortMapper = cohortMapper;
    }

    @Override
    public CreateCohortResponse createCohort(String instituteId,
                               String programId,
                               CreateCohortRequest createCohortRequest) {
        validateInput(instituteId, programId, createCohortRequest);
        Program program = getProgram(instituteId, programId);
        Cohort cohort = cohortMapper.createCohortRequestToCohort(createCohortRequest);
        cohort.setProgram(program);
        cohortRepository.save(cohort);

        return cohortMapper.cohortToCreateCohortResponse(cohort);
    }

    @Override
    public ViewCohortsResponse viewAllCohorts(String instituteId, String programId){
        return viewAllCohorts(instituteId, programId, 0, 5);
    }

    @Override
    public ViewCohortsResponse viewAllCohorts(String instituteId,
                                              String programId,
                                              int pageNumber,
                                              int pageSize) {

        ViewCohortsRequest viewCohortsRequest = new ViewCohortsRequest();
        viewCohortsRequest.setPageNumber(pageNumber);
        viewCohortsRequest.setPageSize(pageSize);

        validateInput(instituteId, programId, viewCohortsRequest);

        Program program = getProgram( instituteId, programId);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Cohort> cohortsInPages = cohortRepository.getAllCohortsByProgramId(program.getProgramId(), pageable);

        return viewCohortsResponse(cohortsInPages);
    }

    private ViewCohortsResponse viewCohortsResponse(Page<Cohort> cohortsInPages) {
        ViewCohortsResponse response = new ViewCohortsResponse();

        List<Cohort> cohorts = cohortsInPages.getContent();
        if (cohorts.isEmpty()) {
            throw new ResourceNotFoundException("No cohorts found for the specified program.");
        }
        response.setContent(cohorts);
        response.setPageNumber(cohortsInPages.getNumber());
        response.setPageSize(cohortsInPages.getSize());
        response.setTotalElement(cohortsInPages.getTotalElements());
        response.setTotalPages(cohortsInPages.getTotalPages());
        response.setLast(cohortsInPages.isLast());

        return response;
    }

    @Override
    public CohortResponse viewCohort(String instituteId, String programId, String cohortId) {
        validateInput(instituteId, programId, cohortId);
        Cohort cohort = getCohort(instituteId, programId, cohortId);

        return cohortMapper.cohortToCohortResponse(cohort);
    }


    @Override
    public CohortResponse editCohort(String instituteId,
                                     String programId,
                                     String cohortId,
                                     CohortRequest editRequest) {
        validateInput(instituteId, programId, editRequest);
        Cohort cohortToEdit = getCohort(instituteId, programId, cohortId);

        cohortMapper.updateCohortFromEditRequest(editRequest, cohortToEdit);

        Cohort editedCohort = cohortRepository.save(cohortToEdit);

        return cohortMapper.cohortToCohortResponse(editedCohort);

    }


    @Override
    public String deleteCohort(String instituteId, String programId, String cohortId){
        validateInput(instituteId, programId, cohortId);

        Cohort cohort = getCohort(instituteId, programId, cohortId);
        String cohortName = cohort.getName();

        List<Trainee> traineesInCohort = traineeRepository.getAllTraineesByCohortId(cohortId);

        if (traineesInCohort.isEmpty()) {
            cohortRepository.delete(cohort);
            return String.format("Cohort %s has been deleted successfully", cohortName);
        }
        return String.format("Cohort %s cannot be deleted", cohortName);
    }

    private Cohort getCohort(String instituteId, String programId, String cohortId) {
        Program program = getProgram(instituteId, programId);
        Cohort cohort = cohortRepository
                .getCohort(cohortId)
                .orElseThrow(()-> new ResourceNotFoundException("Cohort not found"));

        if (cohort.getProgram().getProgramId().equals(program.getProgramId())){
            return cohort;
        }else throw new ResourceNotFoundException("Cohort not found");
    }


    public Program getProgram(String instituteId, String programId) {
        Optional<Institute> foundInstitute = instituteRepository.findInstituteById(instituteId);

        if (foundInstitute.isPresent()) {
            Institute institute = foundInstitute.get();

            Optional<Program> foundProgram = programRepository.findProgramById(programId);

            if (foundProgram.isPresent()) {
                Program program = foundProgram.get();

                if (program.getInstitute().getInstituteId().equals(institute.getInstituteId())) {
                    return program;
                }
                    else throw new ResourceNotFoundException("Program not found in the specified institute");

            } else throw new ResourceNotFoundException("Program not found");

        } else throw new ResourceNotFoundException("Institute not found");

    }

    private void validateInput(String instituteId, String programId, Object request) {
        if (ObjectUtils.isEmpty(instituteId) || ObjectUtils.isEmpty(programId) || request == null) {
            throw new InvalidInputException("Invalid input data");
        }
    }

}
