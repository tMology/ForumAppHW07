package edu.uncc.hw07;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;


public class Forum implements Serializable {

    public String forum_id, created_by_uid, created_by_name, forum_name, forum_description;
    public Timestamp createdForumAtTime;
    public ArrayList<String> userLikes;
    private boolean haveILiked = false;

    public void setupLikeMachine(String uID){
        if (userLikes.contains(uID)){
            haveILiked = true;
        }
        else{
            haveILiked = false;
        }
    }

    public boolean isHaveILiked() {
        return haveILiked;
    }

    public Forum() {
    }

    public ArrayList<String> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(ArrayList<String> userLikes) {
        this.userLikes = userLikes;
    }

    public Timestamp getCreatedForumAtTime() {
        return createdForumAtTime;
    }

    public void setCreatedForumAtTime(Timestamp createdForumAt) {
        this.createdForumAtTime = createdForumAt;
    }

    public String getForum_id() {
        return forum_id;
    }

    public String getCreated_by_uid() {
        return created_by_uid;
    }

    public String getCreated_by_name() {
        return created_by_name;
    }

    public String getForum_name() {
        return forum_name;
    }

    public String getForum_description() {
        return forum_description;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public void setCreated_by_uid(String created_by_uid) {
        this.created_by_uid = created_by_uid;
    }

    public void setCreated_by_name(String created_by_name) {
        this.created_by_name = created_by_name;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public void setForum_description(String forum_description) {
        this.forum_description = forum_description;
    }

}