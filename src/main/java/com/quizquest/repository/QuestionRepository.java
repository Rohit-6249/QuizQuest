package com.quizquest.repository;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Questions eligible for a quiz. Subject and difficulty are optional filters: pass null to
     * include every subject / every difficulty (a "mixed" quiz).
     */
    @Query("""
            select q from Question q
            where q.active = true
              and q.classLevel = :classLevel
              and (:subject is null or q.subject = :subject)
              and (:difficulty is null or q.difficulty = :difficulty)
            """)
    List<Question> findForQuiz(@Param("classLevel") int classLevel,
                               @Param("subject") String subject,
                               @Param("difficulty") Difficulty difficulty);

    /**
     * Admin browse: every filter is optional. Pass null to ignore that filter.
     */
    @Query("""
            select q from Question q
            where (:classLevel is null or q.classLevel = :classLevel)
              and (:subject is null or q.subject = :subject)
              and (:difficulty is null or q.difficulty = :difficulty)
            order by q.classLevel asc, q.subject asc, q.id asc
            """)
    List<Question> search(@Param("classLevel") Integer classLevel,
                          @Param("subject") String subject,
                          @Param("difficulty") Difficulty difficulty);

    @Query("select distinct q.subject from Question q where q.active = true order by q.subject")
    List<String> findDistinctSubjects();

    @Query("select distinct q.classLevel from Question q where q.active = true order by q.classLevel")
    List<Integer> findDistinctClassLevels();

    long countByActiveTrue();
}
