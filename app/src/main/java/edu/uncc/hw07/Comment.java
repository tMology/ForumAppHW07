
package edu.uncc.hw07;

import com.google.firebase.Timestamp;

public class Comment {

    public String commentText, ownerName, ownerId, commentId;
    public Timestamp createdAt;

    public Comment() {
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}


/* initial comment class that wouldnt work package edu.uncc.hw07;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Comment implements Serializable {





    public String comm_text, comment_id, comment_by_uid, comment_by_username;
    public Timestamp commentedAtTime;


    public Comment(){}


    public String getComm_text() {
        return comm_text;
    }

    public String getComment_by_uid() {
        return comment_by_uid;
    }

    public String getComment_by_username() {
        return comment_by_username;
    }

    public String getComment_id() {
        return comment_id;
    }

    public Timestamp getCommentedAtTime() {
        return commentedAtTime;
    }

    public void setComm_text(String comm_text) {
        this.comm_text = comm_text;
    }

    public void setComment_by_uid(String comment_by_uid) {
        this.comment_by_uid = comment_by_uid;
    }

    public void setComment_by_username(String comment_by_username) {
        this.comment_by_username = comment_by_username;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public void setCommentedAtTime(Timestamp commentedAtTime) {
        this.commentedAtTime = commentedAtTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comm_text='" + comm_text + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", comment_by_uid='" + comment_by_uid + '\'' +
                ", comment_by_username='" + comment_by_username + '\'' +
                ", commentedAtTime=" + commentedAtTime +
                '}';
    }
}
*/
