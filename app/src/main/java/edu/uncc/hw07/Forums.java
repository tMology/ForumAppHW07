package edu.uncc.hw07;
import java.io.Serializable;


public class Forums implements Serializable {

    public String forum_id, created_by_uid, created_by_name, forum_name, forum_description;


    public Forums() {
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

    @Override
    public String toString() {
        return "Post{" +
                "forum_id='" + forum_id + '\'' +
                ", created_by_uid='" + created_by_uid + '\'' +
                ", created_by_uid='" + created_by_uid + '\'' +
                ", created_by_name='" + created_by_name + '\'' +
                ", forum_name='" + forum_name + '\'' +
                ", forum_description='" + forum_description + '\'' +
                '}';
    }

}