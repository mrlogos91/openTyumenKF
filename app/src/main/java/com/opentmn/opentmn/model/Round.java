package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import retrofit2.http.Part;

/**
 * Created by kost on 26.12.16.
 */

public class Round implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("current")
    private int current;

    @SerializedName("category")
    private Category category;

    @SerializedName("questions")
    private List<Question> questions;

    public int getId() {
        return id;
    }

    public int getCurrent() {
        return current;
    }

    public Category getCategory() {
        return category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isRoundFinished(boolean isCreator, boolean all) {
        if(questions == null)
            return false;
        if(all) {
            for(Question question: questions) {
                String answer = isCreator ? question.getCreatorAnswer() : question.getFollowerAnswer();
                if(answer == null)
                    return false;
            }
            return true;
        } else {
            for(Question question: questions) {
                String answer = isCreator ? question.getCreatorAnswer() : question.getFollowerAnswer();
                if(answer != null)
                    return true;
            }
            return false;
        }
    }

    public int countAnswered(boolean isCreator) {
        int count = 0;
        for(Question question: questions) {
            String answer = isCreator ? question.getCreatorAnswer() : question.getFollowerAnswer();
            if(answer != null)
                count++;
        }
        return count;
    }
}
