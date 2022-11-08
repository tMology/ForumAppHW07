package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import edu.uncc.hw07.databinding.FragmentCreateForumBinding;
import edu.uncc.hw07.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateForumFragment extends Fragment {

    FirebaseAuth createForumAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateForumFragment newInstance(String param1, String param2) {
        CreateForumFragment fragment = new CreateForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentCreateForumBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToForums();
            }
        });

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forumTitle = binding.editTextForumTitle.getText().toString();
                String forumDescription = binding.editTextForumDescription.getText().toString();
                if(forumTitle.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid Forum Title!", Toast.LENGTH_SHORT).show();
                } else if (forumDescription.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid Forum Description!", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference docRef = db.collection("Forums").document().collection("comments");


                    HashMap<String, Object> postData = new HashMap<>();

                    postData.put("forum_name", forumTitle);
                    postData.put("created_by_name", createForumAuth.getCurrentUser().getDisplayName());
                    postData.put("created_by_uid", createForumAuth.getCurrentUser().getUid());
                    postData.put("forum_description", forumDescription);
                    postData.put("forum_id", docRef.getId());
/*
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

                    docRef.add(postData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                mListener.goToForums();
                            }else{
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("QWAZ", "Error" + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });


        getActivity().setTitle(R.string.forums_label);
    }



    CreateForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateForumListener) context;
    }

    interface CreateForumListener {
        void  goToForums();
    }

}