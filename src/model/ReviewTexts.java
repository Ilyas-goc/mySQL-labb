package model;

import java.sql.Date;

public class ReviewTexts {

    private String reviewText;
    private Date revDate;

    public ReviewTexts(String reviewText, Date revDate) {
        this.reviewText = reviewText;
        this.revDate = revDate;
    }

    public String getText() {
        return this.reviewText;
    }

    public Date getDate() {
        return this.revDate;
    }

    @Override
    public String toString() {
        String info = "Text: " + this.getText() + "\nDate: " + this.getDate();
        return info;
    }

}
