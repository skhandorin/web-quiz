package engine.repositories;

import engine.model.QuizCompletion;
import engine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizCompletionRepository extends JpaRepository<QuizCompletion, Long> {

    Page<QuizCompletion> findByUser(Pageable pageable, User user);
}
