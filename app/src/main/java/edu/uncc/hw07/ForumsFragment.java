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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;


import java.sql.Timestamp;


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
                    Forums forum = doc.toObject(Forums.class);
                    if (pAuth.getCurrentUser().getUid().contentEquals(forum.created_by_uid)){
                        mForums.add(forum);
                    }
                }

                ForumsAdapter.notifyDataSetChanged();
            }
        });



    }

    ForumsAdapter ForumsAdapter;
    ArrayList<Forums> mForums = new ArrayList<>();



    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumsViewHolder> {
        @NonNull
        @Override
        public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row_item, parent, false);
            return new ForumsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsViewHolder holder, int position) {
            Forums forum = mForums.get(position);
            holder.setupUI(forum);
        }


        @Override
        public int getItemCount() {
            return mForums.size();
        }

        class ForumsViewHolder extends RecyclerView.ViewHolder {
            Forums mForums;
            TextView createdBy, forumLikesDate, forumsText, forumTitle;
            ImageView delete, like;


            public ForumsViewHolder(@NonNull View itemView) {
                super(itemView);

                createdBy = itemView.findViewById(R.id.textViewForumCreatedBy);
                forumLikesDate = itemView.findViewById(R.id.textViewForumLikesDate);
                forumsText = itemView.findViewById(R.id.textViewForumText);
                forumTitle = itemView.findViewById(R.id.textViewForumTitle);
                delete = itemView.findViewById(R.id.imageViewDelete);
                like = itemView.findViewById(R.id.imageViewLike);


            }

            public void setupUI(Forums forum){

                createdBy.setText(forum.created_by_name);
                forumLikesDate.setText(forum.getCreated_by_name());
                forumsText.setText(forum.forum_description);
                forumTitle.setText(forum.getForum_name());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.goToComments(forum);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseFirestore.getInstance().collection("Forums").document(forum.getForum_id()).delete();
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
        void goToComments(Forums forums);
    }

}