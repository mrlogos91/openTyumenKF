package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kost on 26.12.16.
 */

public class Question implements Serializable {

    public Question() {}

    public Question(Question question) {
        id = question.getId();
        name = question.getName();
        author = question.getAuthor();
        picture = question.getPicture();
        userAnswer = question.getUserAnswer();
        answers = new ArrayList<>();
        if(question.getAnswers() != null) {
            for(Answer answer : question.getAnswers())
                answers.add(new Answer(answer));
        }
    }

    public Question(String name, String source, String author, String[] answers, String ref, String rights) {
        this.name = name;
        this.source = source;
        this.author = author;
        this.answerArr = answers;
        this.ref = ref;
        this.rights = rights;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name = "";

    @SerializedName("author")
    private String author = "";

    @SerializedName("picture")
    private String picture;

    @SerializedName("answers")
    private List<Answer> answers;

    @SerializedName("creator_answer")
    private String creatorAnswer;

    @SerializedName("follower_answer")
    private String followerAnswer;

    private String source;

    private String ref;

    private String[] answerArr;

    private String rights;

    private String userAnswer;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPicture() {
        return picture;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCreatorAnswer() {
        return creatorAnswer;
    }

    public String getFollowerAnswer() {
        return followerAnswer;
    }

    public String getSource() {
        return source;
    }

    public String getRef() {
        return ref;
    }

    public String[] getAnswerArr() {
        return answerArr;
    }

    public String getRights() {
        return rights;
    }
}
