package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {
    private Board board;
    private User questioner;
    private User answerer;
    private User voter;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        board = new Board("Java Programming");
        questioner = board.createUser("Questioner");
        answerer = board.createUser("Answerer");
        voter = board.createUser("Voter");

        // Arrange
        question = questioner.askQuestion("What is Java?");
        answer = answerer.answerQuestion(question, "Java is a programming language.");
    }

    @Test
    public void testSelfDownvoteQuestionThrowsException() {
        // Expect exception
        expectedException.expect(VotingException.class);
        expectedException.expectMessage("You cannot vote for yourself!");

        // Act
        questioner.downVote(question);
    }

    @Test
    public void testSelfUpvoteAnswerThrowsException() {
        // Expect exception
        expectedException.expect(VotingException.class);
        expectedException.expectMessage("You cannot vote for yourself!");

        // Act
        answerer.upVote(answer);
    }

    @Test
    public void testSelfDownvoteAnswerThrowsException() {
        // Expect exception
        expectedException.expect(VotingException.class);
        expectedException.expectMessage("You cannot vote for yourself!");

        // Act
        answerer.downVote(answer);
    }

    @Test
    public void testNonAuthorCannotAcceptAnswer() {
        // Expect exception
        expectedException.expect(AnswerAcceptanceException.class);
        expectedException.expectMessage("Only Questioner can accept this answer as it is their question");

        // Act
        voter.acceptAnswer(answer);
    }

    @Test
    public void testQuestionAuthorCanAcceptAnswer() {
        // Act
        questioner.acceptAnswer(answer);

        // Assert
        assertTrue("Answer should be marked as accepted", answer.isAccepted());
    }


    @Test
    public void testQuestionUpVoteIncreasesQuestionerReputation() {
        // Arrange
        int initialReputation = questioner.getReputation();

        // Act
        voter.upVote(question);

        // Assert
        assertEquals("Questioner's reputation should increase by 5 points", initialReputation + 5, questioner.getReputation());
    }



    @Test
    public void testAnswerUpVoteIncreasesAnswererReputation() {
        // Arrange
        int initialReputation = answerer.getReputation();

        // Act
        voter.upVote(answer);

        // Assert
        assertEquals("Answerer's reputation should increase by 10 points", initialReputation + 10, answerer.getReputation());
    }



    @Test
    public void testAnswerAcceptedChangesReputation() {
        // Arrange
        int initialReputation = answerer.getReputation();

        // Act
        questioner.acceptAnswer(answer);

        // Assert
        assertTrue("Answer should be marked as accepted", answer.isAccepted());
        assertEquals("Answerer's reputation should increase by 15 points", initialReputation + 15, answerer.getReputation());
    }
}
