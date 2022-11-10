package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw07.databinding.CommentRowItemBinding;
import edu.uncc.hw07.databinding.FragmentForumBinding;
import edu.uncc.hw07.databinding.FragmentForumsBinding;

public class ForumFragment extends Fragment {
    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";
    private Forum mForum;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Forum) getArguments().getSerializable(ARG_PARAM_FORUM);
        }
    }

    FragmentForumBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Comment> mComments = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewForumTitle.setText(mForum.getForum_name());
        binding.textViewForumText.setText(mForum.getForum_description());
        binding.textViewForumCreatedBy.setText(mForum.getCreated_by_name());


        binding.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = binding.editTextComment.getText().toString();

                if(commentText.isEmpty()){
                    Toast.makeText(getActivity(), "Comment cannot be empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> data = new HashMap<>();

                    data.put("commentText", commentText);
                    data.put("ownerId", mAuth.getCurrentUser().getUid());
                    data.put("ownerName", mAuth.getCurrentUser().getDisplayName());
                    data.put("createdAt", FieldValue.serverTimestamp());

                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("forums").document(mForum.getForum_id()).collection("comments").document();
                    data.put("commentId", docRef.getId());

                    docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                binding.editTextComment.setText("");
                            } else {
                                Toast.makeText(getActivity(), "Error adding comment!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        getActivity().setTitle("Forum");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsAdapter = new CommentsAdapter();
        binding.recyclerView.setAdapter(commentsAdapter);


        FirebaseFirestore.getInstance().collection("forums").document(mForum.getForum_id()).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mComments.clear();

                for (QueryDocumentSnapshot doc: value) {
                    Comment comment = doc.toObject(Comment.class);
                    mComments.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();

                binding.textViewCommentsCount.setText(mComments.size() + " Comments");

            }
        });
    }

    CommentsAdapter commentsAdapter;

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{


        @NonNull
        @Override
        public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CommentRowItemBinding binding = CommentRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new CommentsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
            holder.setupUI(mComments.get(position));
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        class CommentsViewHolder extends RecyclerView.ViewHolder{
            CommentRowItemBinding mBinding;
            Comment mComment;
            public CommentsViewHolder(CommentRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Comment comment){
                mComment = comment;
                mBinding.textViewCommentText.setText(mComment.getCommentText());
                mBinding.textViewCommentCreatedBy.setText(mComment.getOwnerName());

                if(mComment.getCreatedAt() != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    mBinding.textViewCommentCreatedAt.setText(sdf.format(mComment.getCreatedAt().toDate()));
                }

                if(mAuth.getCurrentUser().getUid().equals(mComment.getOwnerId())){
                    mBinding.imageViewDelete.setVisibility(View.VISIBLE);
                    mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseFirestore.getInstance().collection("forums")
                                    .document(mForum.getForum_id())
                                    .collection("comments")
                                    .document(mComment.getCommentId()).delete();
                        }
                    });
                } else {
                    mBinding.imageViewDelete.setVisibility(View.GONE);
                }

            }
        }
    }


    ForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumListener) context;
    }

    interface ForumListener{
        void doneShowingForum();
    }
}


/* --> my initial forum fragment and comment classes were not working properly, not sure why



import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.uncc.hw07.databinding.CommentRowItemBinding;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragForumBinder = FragmentForumBinding.inflate(inflater, container, false);

        return fragForumBinder.getRoot();
    }

    ArrayList<Comment> commentList = new ArrayList<>();
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();

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

                if (textUse.isEmpty()) {
                    Toast.makeText(getActivity(), "Comment must contain text", Toast.LENGTH_LONG).show();
                } else {

                    /*DocumentReference commCollection = docRef.collection("comments").document();
                    String defaultComment = "";

    //HashMap<String, Object> commentData = new HashMap<>()
                    commentData.put("comm_text", textUse);
                            commentData.put("comment_by_uid", fireAuth.getCurrentUser().getUid());
                            commentData.put("comment_by_username", fireAuth.getCurrentUser().getDisplayName());
                            commentData.put("commentedAtTime", FieldValue.serverTimestamp());

                            DocumentReference forumDocRef = FirebaseFirestore.getInstance().collection("Forums").document(currentForum.getForum_id()).collection("comments").document();

                            commentData.put("comment_id", forumDocRef.getId());


                            forumDocRef.set(commentData).addOnCompleteListener(new OnCompleteListener<Void>() {
@Override
public void onComplete(@NonNull Task<Void> task) {

        if (task.isSuccessful()) {
        fragForumBinder.editTextComment.setText("");
        } else {
        Toast.makeText(getActivity(), "Comment add error", Toast.LENGTH_SHORT);
        }
        }
        });

        }
        }
        });


        fragForumBinder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cA = new CommentsAdapt();
        fragForumBinder.recyclerView.setAdapter(cA);


        FirebaseFirestore.getInstance().collection("Forums").document(currentForum.getForum_id()).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
@Override
public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        commentList.clear();
        for (QueryDocumentSnapshot doc : value) {
        Comment commentHolder = doc.toObject((Comment.class));
        commentList.add(commentHolder);
        }
        cA.notifyDataSetChanged();
        fragForumBinder.textViewCommentsCount.setText(commentList.size() + " Comments");
        }
        });

        }



        CommentsAdapt cA;
class CommentsAdapt extends RecyclerView.Adapter<CommentsAdapt.CommentViewHold>{
    @NonNull
    @Override
    public CommentViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CommentRowItemBinding cRIB = CommentRowItemBinding.inflate(getLayoutInflater(), parent, false);

        return new CommentViewHold(cRIB);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHold holder, int position) {
        holder.commentUI(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CommentViewHold extends RecyclerView.ViewHolder{
        CommentRowItemBinding cRIBinding;
        Comment commentPlacement;
        public CommentViewHold(CommentRowItemBinding cRI){
            super(cRI.getRoot());
            cRIBinding=cRI;
        }
        public void commentUI(Comment comm){
            commentPlacement = comm;
            cRIBinding.textViewCommentCreatedBy.setText(commentPlacement.getComment_by_username());
            cRIBinding.textViewCommentText.setText(commentPlacement.getComm_text());
            if(commentPlacement.getCommentedAtTime() != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:m a");
                cRIBinding.textViewCommentCreatedAt.setText(simpleDateFormat.format(commentPlacement.getCommentedAtTime().toDate()));
            }
        }
    }
}



    ListenForumWhat forEnList;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        forEnList = (ListenForumWhat) context;
    }

interface ListenForumWhat{
    void forumExit();
}

 */