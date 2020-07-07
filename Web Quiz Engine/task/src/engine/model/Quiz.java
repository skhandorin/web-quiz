package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String text;

    @NotNull
    @Size(min = 2)
    @ElementCollection
    @OrderColumn
    private List<String> options = new ArrayList<>();

    // answer: an array of indexes of correct options, optional, since all options can be wrong.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    @OrderColumn
    private Integer[] answer = new Integer[0]; // if there was not answer in the POST request

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // it's needed for Spring
    public Quiz() {
    }

    public Quiz(String title, String text, List<String> options, Integer[] answer, User user) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer[] getAnswer() {
        return answer;
    }

    public void setAnswer(Integer[] answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
