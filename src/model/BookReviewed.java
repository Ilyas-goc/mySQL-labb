/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;


public class BookReviewed {
    private String reviewerName,reviewText;
    private Date date;
    
    public BookReviewed(String reviewerName,String reviewText,Date date){
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.date = date;
    }
    
    public String getReviewerName(){return this.reviewerName;}
    public String getReviewText(){ return this.reviewText; }
    public Date getDate(){
        Date tmp = this.date;
        return tmp;}
    
    @Override
    public String toString() {
        String info = "Reviewer name: " + this.getReviewerName() + "\nReview text: " + this.getReviewText()+
                "\nReview date:"+this.getDate();
        return info;
    }
}
