package com.ken.gcp.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ken.gcp.quiz.dao.QuizCommentDao;
import com.ken.gcp.quiz.model.QuizComment;

@Service
public class QuizCommentService {
	@Autowired
	private QuizCommentDao commentDao;

	public void addQuizComment(String category, Long id, String author, String comment) {
		final QuizComment quizComment = new QuizComment() //
				.quizId(category + '_' + id) //
				.author(author) //
				.comment(comment);

		commentDao.addComment(quizComment);
	}

}
