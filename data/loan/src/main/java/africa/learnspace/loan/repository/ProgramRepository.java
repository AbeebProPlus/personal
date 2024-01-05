package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


public interface ProgramRepository extends JpaRepository<Program, String> {
    @Query("SELECT p FROM Program p WHERE p.programId = :programId AND p.institute = :institute")
    Optional<Program> findProgramByProgramIdAndInstitute(@Param("programId") String programId, @Param("institute") Institute institute);

    List<Program> findByProgramId(@Param("programId") String programId);

    boolean findByName(String programName);


    @Transactional
    @Modifying
    @Query(value = "SELECT name FROM Program WHERE institute_institute_id = :instituteId", nativeQuery = true)
    List<String> findNameByInstituteInstituteId(@Param("instituteId") String instituteId);

    @Query("SELECT CASE WHEN COUNT(ip) > 0 THEN true ELSE false END FROM Program ip WHERE ip.programId = :programId AND ip.institute = :institute")
    boolean isProgramPartOfInstitute(String programId, Institute institute);

    List<Program> findAllProgramsByInstituteInstituteId(String id);
    Optional<Program>  findProgramByName(String name);
}