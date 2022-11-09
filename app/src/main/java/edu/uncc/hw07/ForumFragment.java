package edu.uncc.hw07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

import edu.uncc.hw07.databinding.FragmentForumBinding;


public class ForumFragment extends Fragment {


    private static final String PARAM_FF = "PARAM_FF";

    private Forum currentForum;

    public ForumFragment() {
        // Required empty public constructor
    }


    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_FF, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentForum = (Forum) getArguments().getSerializable(PARAM_FF);
        }
    }


    FragmentForumBinding fragForumBinder;

    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragForumBinder = FragmentForumBinding.inflate(inflater, container, false);

        return fragForumBinder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Forum");

        fragForumBinder.textViewForumCreatedBy.setText(currentForum.getCreated_by_name());
        fragForumBinder.textViewForumText.setText(currentForum.getForum_description());
        fragForumBinder.textViewForumTitle.setText(currentForum.getForum_name());

        fragForumBinder.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUse = fragForumBinder.editTextComment.getText().toString();

                if (textUse.isEmpty()){
                    Toast.makeText(getActivity(), "Comment must contain text", Toast.LENGTH_LONG).show();
                }
                else{

                    /*DocumentReference commCollection = docRef.collection("comments").document();
                    String defaultComment = "";*/

                    HashMap<String, Object> commentData = new HashMap<>();
                    commentData.put("comm_text", textUse);
                    commentData.put("comment_by_uid", fireAuth.getCurrentUser().getUid());
                    commentData.put("comment_by_username", fireAuth.getCurrentUser().getDisplayName());
                    commentData.put("commentedAtTime", FieldValue.serverTimestamp());


                    /*commCollection.set(commentData);

                    docRef.set(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mListener.goToForums();
                            }else{
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("QWAZ", "Error" + task.getException().getMessage());
                            }
                        }
                    });
                     */
                    DocumentReference forumDocRef = FirebaseFirestore.getInstance()
                            .collection("Forums")
                            .document(currentForum.getForum_id())
                            .collection("comments").document();

                    commentData.put("comment_id", forumDocRef.getId());


                    FirebaseFirestore.getInstance().collection("Forums").document(currentForum.getForum_id()).collection("")

                    forumDocRef.set(commentData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                fragForumBinder.editTextComment.setText("");
                            }
                            else{
                                Toast.makeText(getActivity(), "Comment add error", Toast.LENGTH_SHORT);
                            }
                        }
                    });

                }
            }
        });


    }
}