package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.repository.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProgramManager {
    private final ProgramRepository programRepository;

    public ProgramManager(ProgramRepository programRepository){
        this.programRepository = programRepository;
    }


    public void saveProgram(Program program) {
        programRepository.save(program);
    }

    public Optional<Program> findProgramByProgramIdAndInstitute(String programId, Institute institute){
        return programRepository.findProgramByProgramIdAndInstitute(programId, institute);
    }

    public boolean findByName(String programName) {
        return programRepository.findByName(programName);
    }

    public List<Program> findAll() {
        return programRepository.findAll();
    }

    public List<String> findProgramNameByInstituteId(String instituteId) {
        return programRepository.findNameByInstituteInstituteId(instituteId);
    }

    public Program createProgram(Program instituteProgram) {
        return programRepository.save(instituteProgram);
    }

    public Program save(Program instituteProgram) {
        return programRepository.save(instituteProgram);
    }

    public Optional<Program> findProgramById(String id) {
        return programRepository.findById(id);
    }

    public Optional<Program> findById(String id) {
        return  programRepository.findById(id);
    }

    public List<Program> findAllProgramByInstituteId(String id) {
        return programRepository.findAllProgramsByInstituteInstituteId(id);
    }
}