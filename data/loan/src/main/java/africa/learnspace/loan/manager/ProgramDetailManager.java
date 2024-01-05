package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.program.ProgramLoanDetail;
import africa.learnspace.loan.repository.ProgramLoanDetailRepository;
import org.springframework.stereotype.Component;

@Component
public class ProgramDetailManager {

    private final ProgramLoanDetailRepository programLoanDetailRepository;

    public ProgramDetailManager(ProgramLoanDetailRepository programLoanDetailRepository) {
        this.programLoanDetailRepository = programLoanDetailRepository;
    }

    public ProgramLoanDetail createProgramDetail(ProgramLoanDetail programLoanDetail) {
        return programLoanDetailRepository.save(programLoanDetail);
    }

}
