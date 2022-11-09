package edu.uncc.hw07;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Comment implements Serializable {

                    /*
                    commentData.put("text", defaultComment);
                     commentData.put("comment_by_uid", createForumAuth.getCurrentUser().getUid());
                    commentData.put("comment_by_name", createForumAuth.getCurrentUser().getDisplayName());
                    commentData.put("comment_date", new Date().toString());
 */




    public String comm_text, comment_id, comment_by_uid, comment_by_username;
    public Timestamp commentedAtTime;


    public Comment(){}


}
