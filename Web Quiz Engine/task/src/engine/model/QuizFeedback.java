package engine.model;

public class QuizFeedback {
    boolean success;
    String feedback;

    public QuizFeedback(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
