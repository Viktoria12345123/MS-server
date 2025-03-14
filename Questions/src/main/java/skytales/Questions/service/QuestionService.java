package skytales.Questions.service;

import org.springframework.stereotype.Service;
import skytales.Questions.dto.AnswerRequest;
import skytales.Questions.dto.PostQuestionRequest;
import skytales.Questions.model.Question;
import skytales.Questions.model.Status;
import skytales.Questions.repository.QuestionRepository;


import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {


    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> fetchQuestions(UUID userId) {
        return questionRepository.findByAuthorAndAnswerIsNotNull(userId);
    }

    public void createQuestion(PostQuestionRequest request, UUID userId) {

        Question question = Question.builder()
                .text(request.text())
                .author(userId)
                .status(Status.PENDING)
                .build();

        questionRepository.save(question);
    }

    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    public void sendAnswer(UUID questionId, AnswerRequest answerRequest, UUID adminId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Not a valid ID"));

        question.setAnswer(answerRequest.text());
        question.setStatus(Status.ANSWERED);
        question.setAdmin(adminId);

        questionRepository.save(question);

        questionRepository.save(question);
    }
}
