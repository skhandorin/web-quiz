package engine.controller;

import engine.QuizNotFoundException;
import engine.model.QuizCompletion;
import engine.model.User;
import engine.repositories.QuizCompletionRepository;
import engine.repositories.QuizRepository;
import engine.model.QuizAnswer;
import engine.model.Quiz;
import engine.model.QuizFeedback;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/quizzes")
public class WebQuizController {

    @Autowired
    private QuizRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizCompletionRepository completionRepository;

    @GetMapping
    public Page<Quiz> all(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    )
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return repository.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Quiz addQuiz(@Valid @RequestBody Quiz newQuiz, Authentication authentication) {
        String username = authentication.getName();
        newQuiz.setUser(userRepository.findByEmail(username));
        return repository.save(newQuiz);
    }

    @GetMapping("/{id}")
    public Quiz getById(@PathVariable Long id) {
        Quiz quiz = repository.findById(id).orElseThrow(
                () -> new QuizNotFoundException(id));
        return quiz;
    }

//    @ExceptionHandler(QuizNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String quizNotFoundHandler(QuizNotFoundException ex) {
//        return ex.getMessage();
//    }

    @PostMapping(value = "/{id}/solve")
    public QuizFeedback solveQuiz(@PathVariable Long id,
                                  @RequestBody QuizAnswer quizAnswer,
                                  Authentication authentication)
    {
        Quiz quiz = this.getById(id);
        Set<Integer> answerSet = Set.copyOf(List.of(quizAnswer.getAnswer()));
        Set<Integer> quizAnswerSet = Set.of(quiz.getAnswer());

        if (answerSet.equals(quizAnswerSet)) {
            User user = userRepository.findByEmail(authentication.getName());
            QuizCompletion completion = new QuizCompletion(quiz.getId(), user);
            completionRepository.save(completion);

            return new QuizFeedback(true, "Congratulations, you're right!");
        } else {
            return new QuizFeedback(false, "Wrong answer! Please, try again.");
        }
    }

    @GetMapping("/completed")
    public Page<QuizCompletion> getCompleted( @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                                              @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
                                              Authentication authentication)
    {
        User user = userRepository.findByEmail(authentication.getName());

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("completedAt").descending());

        return completionRepository.findByUser(pageable, user);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable("id") Long id, Authentication authentication) {
        Quiz quiz = repository.findById(id).orElseThrow(
                () -> new QuizNotFoundException(id)
        );

        String username = authentication.getName();
        User authenticatedUser = userRepository.findByEmail(username);

        if (!authenticatedUser.equals(quiz.getUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        repository.delete(quiz);

        return ResponseEntity.noContent().build();
    }
}
