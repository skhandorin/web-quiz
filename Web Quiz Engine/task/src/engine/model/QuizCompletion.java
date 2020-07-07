package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;

@Entity
public class QuizCompletion {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "quiz_id")
//    private Quiz quiz;

    @JsonProperty(value = "id")
    @Column(name = "quiz_id")
    private Long quizId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime completedAt;

    public QuizCompletion() {
    }

    public QuizCompletion(Long quizId, User user) {
        this.quizId = quizId;
        this.user = user;
        completedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Quiz getQuiz() {
//        return quiz;
//    }
//
//    public void setQuiz(Quiz quiz) {
//        this.quiz = quiz;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
