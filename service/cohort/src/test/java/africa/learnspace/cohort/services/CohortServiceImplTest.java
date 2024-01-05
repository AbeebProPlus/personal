package africa.learnspace.cohort.services;

import africa.learnspace.cohort.data.request.CohortRequest;
import africa.learnspace.cohort.data.request.CreateCohortRequest;
import africa.learnspace.cohort.data.response.CohortResponse;
import africa.learnspace.cohort.data.response.CreateCohortResponse;
import africa.learnspace.cohort.data.response.ViewCohortsResponse;
import africa.learnspace.cohort.mappers.CohortMapper;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.loan.manager.CohortManager;
import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.manager.ProgramManager;
import africa.learnspace.loan.manager.TraineeManager;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CohortServiceImplTest {

    @Mock
    private InstituteManager instituteRepository;
    @Mock
    private ProgramManager programRepository;
    @Mock
    private CohortManager cohortRepository;
    @Mock
    private CohortMapper cohortMapper;
    @Mock
    private TraineeManager traineeRepository;
    @InjectMocks
    public CohortServiceImpl cohortService;

    @Test
    public void testGetProgramValidProgramFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);

        Program program = new Program();
        program.setProgramId(programId);

        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));

        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));

        // Act
        Program result = cohortService.getProgram(instituteId, programId);

        // Assert
        assertEquals(programId, result.getProgramId());
    }

    @Test
    public void testGetProgramProgramNotFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);

        // Here, we don't associate any program with the institute.

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> cohortService.getProgram(instituteId, programId));
    }

    @Test
    public void testGetProgramInstituteNotFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> cohortService.getProgram(instituteId, programId));
    }

    @Test
    public void testCreateCohortValidData() {
        // Arrange
        String instituteId = "123";
        String programId = "456";

        // Create the Institute and Program
        Institute institute = new Institute();
        institute.setInstituteId(instituteId);

        Program program = new Program();
        program.setProgramId(programId);
        program.setInstitute(institute);

        // Create a valid cohort request
        CreateCohortRequest cohortRequest = new CreateCohortRequest();
        cohortRequest.setName("Spring 2023 Cohort");
        cohortRequest.setStartDate(LocalDate.parse("2023-04-01"));
        cohortRequest.setEndDate(LocalDate.parse("2023-09-30"));

        // Configure the mock repository behavior
        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.save(any(Cohort.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(cohortMapper.createCohortRequestToCohort(any(CreateCohortRequest.class))).thenAnswer(invocation -> {
            CreateCohortRequest request = invocation.getArgument(0);
            Cohort cohort = new Cohort();
            cohort.setName(request.getName());
            cohort.setStartDate(request.getStartDate());
            cohort.setEndDate(request.getEndDate());
            return cohort;
        });

        when(cohortMapper.cohortToCreateCohortResponse(any(Cohort.class))).thenAnswer(invocation -> {
            Cohort cohort = invocation.getArgument(0);
            CreateCohortResponse response = new CreateCohortResponse();
            response.setName(cohort.getName());
            response.setStartDate(cohort.getStartDate());
            response.setEndDate(cohort.getEndDate());
            return response;
        });
        // Act: Create the cohort
        CreateCohortResponse result = cohortService.createCohort(instituteId, programId, cohortRequest);

        // Assert
        assertNotNull(result);
        assertEquals(cohortRequest.getName(), result.getName());
        assertEquals(cohortRequest.getStartDate(), result.getStartDate());
        assertEquals(cohortRequest.getEndDate(), result.getEndDate());
    }

    @Test
    public void testViewAllCohortsValidProgramFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        int pageNumber = 0;
        int pageSize = 10;

        // Create a valid ViewCohortsRequest

        // Create the Institute and Program
        Institute institute = new Institute();
        institute.setInstituteId(instituteId);

        Program program = new Program();
        program.setProgramId(programId);
        program.setInstitute(institute);

        // Create two cohorts for the program
        Cohort firstCohort = new Cohort();
        firstCohort.setCohortId("789");
        firstCohort.setProgram(program);

        Cohort secondCohort = new Cohort();
        secondCohort.setCohortId("654");
        secondCohort.setProgram(program);

        List<Cohort> cohorts = new ArrayList<>();
        cohorts.add(firstCohort);
        cohorts.add(secondCohort);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.getAllCohortsByProgramId(programId, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(cohorts));


        // Act: Retrieve all cohorts for the specified program
        ViewCohortsResponse result = cohortService.viewAllCohorts(instituteId, programId, pageNumber, pageSize);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(firstCohort.getCohortId(), result.getContent().get(0).getCohortId());
        assertEquals(secondCohort.getCohortId(), result.getContent().get(1).getCohortId());
    }

    @Test
    public void testViewAllCohortsProgramNotFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";

        int pageNumber = 0;
        int pageSize = 10;

        // Create the Institute and Program
        Institute institute = new Institute();
        institute.setInstituteId(instituteId);

        Program program = new Program();
        program.setProgramId(programId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> cohortService.viewAllCohorts( instituteId, programId, pageNumber, pageSize));
    }



    @Test
    public void testViewCohortValidCohortFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        String cohortId = "789";

        Cohort cohort = new Cohort();
        cohort.setCohortId(cohortId);
        cohort.setName("Luminaries");

        Program program = new Program();
        program.setProgramId(programId);
        cohort.setProgram(program);

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.getCohort(cohortId)).thenReturn(Optional.of(cohort));

        when(cohortMapper.cohortToCohortResponse(any(Cohort.class))).thenAnswer(invocation -> {
            Cohort requestedCohort = invocation.getArgument(0);
            CohortResponse response = new CohortResponse();
            response.setName(requestedCohort.getName());
            //
            return response;
        });
        // Act
        CohortResponse result = cohortService.viewCohort(instituteId, programId, cohortId);

        // Assert
        assertEquals(cohort.getName(), result.getName());
    }

    @Test
    public void testViewCohortCohortNotFound() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        String cohortId = "789";

        Cohort cohort = new Cohort();
        cohort.setCohortId(cohortId);

        Program program = new Program();
        program.setProgramId(programId);
        cohort.setProgram(program);

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> cohortService.viewCohort(instituteId, programId, "987"));
    }


    @Test
    public void testEditCohortWithValidData() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        String cohortId = "789";

        // Create a cohort with initial properties
        Cohort initialCohort = new Cohort();
        initialCohort.setCohortId(cohortId);
        initialCohort.setName("Initial Cohort Name");
        LocalDate initialStartDate = LocalDate.parse("2023-01-01");
        LocalDate initialEndDate = LocalDate.parse("2023-06-30");
        initialCohort.setStartDate(initialStartDate);
        initialCohort.setEndDate(initialEndDate);

        Program program = new Program();
        program.setProgramId(programId);
        initialCohort.setProgram(program);

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.getCohort(cohortId)).thenReturn(Optional.of(initialCohort));

        doAnswer(invocation -> {
            CohortRequest request = invocation.getArgument(0);
            Cohort cohort = invocation.getArgument(1);

            // Update the cohort based on the request
            cohort.setName(request.getName());
            cohort.setStartDate(request.getStartDate());
            cohort.setEndDate(request.getEndDate());

            // No return is needed for void methods
            return null;
        }).when(cohortMapper).updateCohortFromEditRequest(any(CohortRequest.class), any(Cohort.class));

        when(cohortMapper.cohortToCohortResponse(any(Cohort.class))).thenAnswer(invocation -> {
            Cohort cohort = invocation.getArgument(0);
            CohortResponse response = new CohortResponse();
            response.setName(cohort.getName());
            response.setStartDate(cohort.getStartDate());
            response.setEndDate(cohort.getEndDate());
            // Add more mappings as needed
            return response;
        });

        // Create an editRequest with updated properties
        CohortRequest editRequest = new CohortRequest();
        editRequest.setName("Edited Cohort Name");
        LocalDate editedStartDate = LocalDate.parse("2023-02-01");
        LocalDate editedEndDate = LocalDate.parse("2023-07-31");
        editRequest.setStartDate(editedStartDate);
        editRequest.setEndDate(editedEndDate);

        // Mock the cohortRepository save method
        when(cohortRepository.save(any(Cohort.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CohortResponse editedCohort = cohortService.editCohort(instituteId, programId, cohortId, editRequest);

        // Assert
        assertEquals("Edited Cohort Name", editedCohort.getName());
        assertEquals(editedStartDate, editedCohort.getStartDate());
        assertEquals(editedEndDate, editedCohort.getEndDate());
    }


//    @Test
//    public void testEditCohortWithNullEditRequestProperties() {
//        // Arrange
//        String instituteId = "123";
//        String programId = "456";
//        String cohortId = "789";
//
//        // Create a cohort with initial properties
//        Cohort initialCohort = new Cohort();
//        initialCohort.setCohortId(cohortId);
//        initialCohort.setName("Initial Cohort Name");
//        LocalDate initialStartDate = LocalDate.parse("2023-01-01");
//        LocalDate initialEndDate = LocalDate.parse("2023-06-30");
//        initialCohort.setStartDate(initialStartDate);
//        initialCohort.setEndDate(initialEndDate);
//
//        Program program = new Program();
//        program.setProgramId(programId);
//        initialCohort.setProgram(program);
//
//        Institute institute = new Institute();
//        institute.setInstituteId(instituteId);
//        program.setInstitute(institute);
//
//        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
//        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
//        when(cohortRepository.getCohort(cohortId)).thenReturn(Optional.of(initialCohort));
//        when(cohortRepository.save(any(Cohort.class))).thenReturn(initialCohort);
//
//        doAnswer(invocation -> {
//            CohortRequest request = invocation.getArgument(0);
//            Cohort cohort = invocation.getArgument(1);
//
//            // Update the cohort based on the request
//            cohort.setName(request.getName());
//            cohort.setStartDate(request.getStartDate());
//            cohort.setEndDate(request.getEndDate());
//
//            // Return the updated cohort
//            return cohort;
//        }).when(cohortMapper).updateCohortFromEditRequest(any(CohortRequest.class), any(Cohort.class));
//
//        when(cohortMapper.cohortToCohortResponse(any(Cohort.class))).thenAnswer(invocation -> {
//            Cohort cohort = invocation.getArgument(0);
//            CohortResponse response = new CohortResponse();
//            response.setName(cohort.getName());
//            response.setStartDate(cohort.getStartDate());
//            response.setEndDate(cohort.getEndDate());
//            // Add more mappings as needed
//            return response;
//        });
//
//        // Create an editRequest with properties set to null
//        CohortRequest editRequest = new CohortRequest();
//
//        // Act
//        CohortResponse editedCohort = cohortService.editCohort(instituteId, programId, cohortId, editRequest);
//
//        // Assert
//        assertEquals("Initial Cohort Name", editedCohort.getName());
//        assertEquals(initialStartDate, editedCohort.getStartDate());
//        assertEquals(initialEndDate, editedCohort.getEndDate());
//    }

    @Test
    public void testEditCohortWithDifferentStartAndEndDates() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        String cohortId = "789";

        // Create a cohort with initial properties
        Cohort initialCohort = new Cohort();
        initialCohort.setCohortId(cohortId);
        initialCohort.setName("Initial Cohort Name");
        LocalDate initialStartDate = LocalDate.parse("2023-01-01");
        LocalDate initialEndDate = LocalDate.parse("2023-06-30");
        initialCohort.setStartDate(initialStartDate);
        initialCohort.setEndDate(initialEndDate);

        Program program = new Program();
        program.setProgramId(programId);
        initialCohort.setProgram(program);

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.getCohort(cohortId)).thenReturn(Optional.of(initialCohort));
        when(cohortRepository.save(any(Cohort.class))).thenReturn(initialCohort);

        // Create an editRequest with different start and end dates
        CohortRequest editRequest = new CohortRequest();
        LocalDate editedStartDate = LocalDate.parse("2023-02-01");
        LocalDate editedEndDate = LocalDate.parse("2023-07-31");
        editRequest.setStartDate(editedStartDate);
        editRequest.setEndDate(editedEndDate);

        doAnswer(invocation -> {
            CohortRequest request = invocation.getArgument(0);
            Cohort cohort = invocation.getArgument(1);

            // Update the cohort based on the request
            cohort.setName(request.getName());
            cohort.setStartDate(request.getStartDate());
            cohort.setEndDate(request.getEndDate());

            // No return is needed for void methods
            return null;
        }).when(cohortMapper).updateCohortFromEditRequest(any(CohortRequest.class), any(Cohort.class));


        when(cohortMapper.cohortToCohortResponse(any(Cohort.class))).thenAnswer(invocation -> {
            Cohort cohort = invocation.getArgument(0);
            CohortResponse response = new CohortResponse();
            response.setName(cohort.getName());
            response.setStartDate(cohort.getStartDate());
            response.setEndDate(cohort.getEndDate());
            // Add more mappings as needed
            return response;
        });

        // Act
        CohortResponse editedCohort = cohortService.editCohort(instituteId, programId, cohortId, editRequest);

        // Assert
        assertEquals(editedStartDate, editedCohort.getStartDate());
        assertEquals(editedEndDate, editedCohort.getEndDate());
    }

    @Test
    public void testDeleteCohortWithValidCohortAndVerifyRepository() {
        // Arrange
        String instituteId = "123";
        String programId = "456";
        String cohortId = "789";

        Cohort cohort = new Cohort();
        cohort.setCohortId(cohortId);
        cohort.setName("Cohort to Delete");

        Program program = new Program();
        program.setProgramId(programId);
        cohort.setProgram(program);

        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        program.setInstitute(institute);

        when(instituteRepository.findInstituteById(instituteId)).thenReturn(Optional.of(institute));
        when(programRepository.findProgramById(programId)).thenReturn(Optional.of(program));
        when(cohortRepository.getCohort(cohortId)).thenReturn(Optional.of(cohort));
        when(traineeRepository.getAllTraineesByCohortId(cohortId)).thenReturn(Collections.emptyList());


        // Mock the cohortRepository.delete method to capture the cohort being deleted
        final Cohort[] capturedCohort = {null};
        doAnswer(invocation -> {
            capturedCohort[0] = invocation.getArgument(0);
            return null;
        }).when(cohortRepository).delete(any(Cohort.class));

        // Act
        String result = cohortService.deleteCohort(instituteId, programId, cohortId);

        // Assert
        assertEquals("Cohort Cohort to Delete has been deleted successfully", result);

        // Verify that cohortRepository.delete was called
        verify(cohortRepository, times(1)).delete(any(Cohort.class));

        // Check that the cohort passed to cohortRepository.delete matches the expected cohort
        assertEquals("Cohort to Delete", capturedCohort[0].getName());
        assertEquals(cohortId, capturedCohort[0].getCohortId());
    }

}