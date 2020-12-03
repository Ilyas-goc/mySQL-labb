/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

public class Reviewer {

    private String reviewerName;
    private ArrayList<ReviewTexts> listOfWrittenTexts;

    public Reviewer(String reviewerName, ArrayList<ReviewTexts> listOfWrittenTexts) {
        this.reviewerName = reviewerName;
        this.listOfWrittenTexts = listOfWrittenTexts;
    }

    public String getReviewerName() {
        return this.reviewerName;
    }

    public ArrayList<ReviewTexts> getListOfTexts() {
        ArrayList<ReviewTexts> tmp = new ArrayList<ReviewTexts>();
        for (int i = 0; i < this.listOfWrittenTexts.size(); i++) {
            tmp.add(listOfWrittenTexts.get(i));
        }
        return tmp;
    }

}
