package edu.uncc.hw07;


import edu.uncc.hw07.databinding.ForumRowItemBinding;
import edu.uncc.hw07.databinding.FragmentForumsBinding;


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

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.sql.Timestamp;
import java.util.HashMap;


/*
a. Assignment -- inClass11
b. File Name -- InClass11
c. Aaron Hill & Mitchell Habovick
*/


public class ForumsFragment extends Fragment {
    public ForumsFragment() {
        // Required empty public constructor
    }


    FragmentForumsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForumsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    FirebaseAuth pAuth = FirebaseAuth.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DecimalFormat df = new DecimalFormat("#.#####");

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logout();
            }
        });


        binding.buttonCreateForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.createForum();
            }
        });

        getActivity().setTitle(R.string.forums_label);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ForumsAdapter = new ForumsAdapter();
        binding.recyclerView.setAdapter(ForumsAdapter);

        getActivity().setTitle("Forums");

        Log.d("completeLogin", "onViewCreated: " + pAuth.getCurrentUser().getDisplayName());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Forums").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                mForums.clear();


                for (QueryDocumentSnapshot doc : value){

                    Forum forum = doc.toObject(Forum.class);
                    forum.setupLikeMachine(pAuth.getCurrentUser().getUid());
                        mForums.add(forum);

                }

                ForumsAdapter.notifyDataSetChanged();
            }
        });



    }

    ForumsAdapter ForumsAdapter;
    ArrayList<Forum> mForums = new ArrayList<>();



    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumsViewHolder> {
        @NonNull
        @Override
        public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ForumRowItemBinding binding = ForumRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForumsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsViewHolder holder, int position) {
            Forum forum = mForums.get(position);
            holder.setupUI(forum);
        }


        @Override
        public int getItemCount() {
            return mForums.size();
        }

        class ForumsViewHolder extends RecyclerView.ViewHolder {
            ForumRowItemBinding mBinding;
            Forum mForums;
            public ForumsViewHolder(ForumRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Forum forum){

                mForums = forum;
                mBinding.textViewForumCreatedBy.setText(forum.created_by_name);

                if(mForums.createdForumAtTime != null){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:m a");
                    mBinding.textViewForumLikesDate.setText(simpleDateFormat.format(forum.getCreatedForumAtTime().toDate()));
                }

                mBinding.textViewForumText.setText(forum.forum_description);
                mBinding.textViewForumTitle.setText(forum.getForum_name());

                if (pAuth.getCurrentUser().getUid().contentEquals(mForums.created_by_uid)){
                    mBinding.imageViewDelete.setVisibility(View.VISIBLE);

                    mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FirebaseFirestore.getInstance().collection("Forums").document(mForums.getForum_id()).delete();

                        }
                    });
                }
                else{

                    mBinding.imageViewDelete.setVisibility(View.INVISIBLE);

                }

                if(mForums.isHaveILiked()){
                    mBinding.imageViewLike.setImageResource(R.drawable.like_favorite);
                }
                else{
                    mBinding.imageViewLike.setImageResource(R.drawable.like_not_favorite);
                }

                mBinding.imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> likeData = new HashMap<>();

                        if(mForums.isHaveILiked()){
                            likeData.put("userLikes", FieldValue.arrayRemove(pAuth.getCurrentUser().getUid()));
                        }
                        else{
                            likeData.put("userLikes", FieldValue.arrayUnion(pAuth.getCurrentUser().getUid()));
                        }
                        FirebaseFirestore.getInstance().collection("Forums").document(mForums.getForum_id()).update(likeData);
                    }
                });
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToForumAndComments(mForums);
                }
            });

            }
        }

    }

    ForumsListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumsListener) context;
    }

    interface ForumsListener{
        void logout();
        void createForum();
        void goToForumAndComments(Forum forum);
    }

}