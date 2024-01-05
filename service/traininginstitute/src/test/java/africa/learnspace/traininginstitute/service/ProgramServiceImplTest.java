package africa.learnspace.traininginstitute.service;

import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.manager.ProgramManager;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.traininginstitute.dto.request.ProgramRequest;
import africa.learnspace.traininginstitute.dto.response.ApiResponse;
import africa.learnspace.traininginstitute.exception.InstituteProgramException;
import africa.learnspace.traininginstitute.util.InstituteProgramConstant;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceImplTest {
    @Mock
    private ProgramManager programManager;
    @Mock
    private UserManager userManager;
    @Mock
    private InstituteManager instituteManager;
    @InjectMocks
    private ProgramServiceImpl programService;

//    @Test
//    public void testCreateProgram_Success() {
//        // Create a mock User and ProgramRequest
//        String email = "test@example.com";
//        User user = new User();
//        Mockito.when(userManager.findByEmail(email)).thenReturn(user);
//
//        ProgramRequest programRequest = new ProgramRequest();
//        programRequest.setInstituteId("institute123");
//        programRequest.setProgramName("New Program");
//        programRequest.setProgramDescription("Description");
//        programRequest.setProgramDuration(10);
//        programRequest.setProgramMode("fulltime");
//        programRequest.setProgramType("VOCATIONAL");
//        programRequest.setDeliveryType("ONLINE");
//
//        // Mock InstituteManager to return a found Institute
//        Institute foundInstitute = new Institute();
//        Mockito.when(instituteManager.findById(programRequest.getInstituteId())).thenReturn(foundInstitute);
//
//        // Mock ProgramManager to return an empty list of programs
//        Mockito.when(programManager.findProgramNameByInstituteId(programRequest.getInstituteId())).thenReturn(new ArrayList<>());
//
//        // Mock the saveProgram method of ProgramManager
//        Mockito.doNothing().when(programManager).saveProgram(Mockito.any());
//
//        // Create an instance of ProgramServiceImpl and call createProgram
////        ProgramServiceImpl programService = new ProgramServiceImpl(programManager, instituteManager);
//        ApiResponse response = programService.createProgram(email, programRequest);
//
//        // Verify that the response is successful
//
//        assertTrue(response.isSuccessful());
//
//    }

    @Test
    public void testCreateProgram_ProgramNameExists() {
        // Create a mock User and ProgramRequest
        User user = new User();
        ProgramRequest programRequest = new ProgramRequest();
        programRequest.setInstituteId("institute123");
        programRequest.setProgramName("Existing Program"); // Name that already exists
        programRequest.setProgramDescription("Description");
        programRequest.setProgramDuration(10);
        programRequest.setProgramMode("Online");
        programRequest.setProgramType("Regular");
        programRequest.setDeliveryType("Online");

        // Mock InstituteManager to return a found Institute
        Institute foundInstitute = new Institute();
        Mockito.when(instituteManager.findInstituteById(programRequest.getInstituteId())).thenReturn(Optional.of(foundInstitute));

        // Mock ProgramManager to return a list with an existing program name
        List<String> programNames = new ArrayList<>();
        programNames.add("Existing Program");
        Mockito.when(programManager.findProgramNameByInstituteId(programRequest.getInstituteId())).thenReturn(programNames);

        // Verify that the method throws an InstituteProgramException with the expected message

        assertThrows(InstituteProgramException.class, () -> programService.createProgram(user.getEmail(), programRequest));

    }

//    @Test
//    public void testFindAllPrograms() {
//        // Mock ProgramManager to return a list of programs
//        List<Program> programs = new ArrayList<>();
//        programs.add(new Program());
//        programs.add(new Program());
//        Mockito.when(programManager.findAll()).thenReturn(programs);
//
//        // Verify that the method returns the list of programs
//        List<Program> result = programService.findAllPrograms();
//        assertEquals(programs, result);
//    }



    @Test
    public void testFindProgram_ValidProgram() {

        when(instituteManager.existsByInstituteId("validInstituteId")).thenReturn(true);
        Program program = Program.builder().institute(Institute.builder().instituteId("validInstituteId").build()).programId("validProgramId").build();
        when(programManager.findById("validProgramId")).thenReturn(Optional.of(program));

        // Act
        ApiResponse response = programService.findProgram("validInstituteId", "validProgramId");

        // Assert
        assertNotNull(response);
    }

    @Test
    public void testFindProgram_InvalidProgram() {

        when(instituteManager.existsByInstituteId("validInstituteId")).thenReturn(true);
        when(programManager.findById("invalidProgramId")).thenReturn(Optional.empty());


        try {
            programService.findProgram("validInstituteId", "invalidProgramId");
            fail("Expected ProgramManagerException was not thrown");
        } catch (InstituteProgramException e) {
            assertEquals(InstituteProgramConstant.PROGRAM_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    public void testFindProgram_InstituteMismatch() {

        when(instituteManager.existsByInstituteId("validInstituteId")).thenReturn(true);
        Program program = Program.builder().institute(Institute.builder().instituteId("validInstituteId").build()).programId("validProgramId").build();
        when(programManager.findById("validProgramId")).thenReturn(Optional.of(program));


        try {
            programService.findProgram("validInstituteId", "validProgramId");
        } catch (InstituteProgramException e) {
            assertEquals(InstituteProgramConstant.PROGRAM_NOT_FOUND, e.getMessage());
        }
    }
}
