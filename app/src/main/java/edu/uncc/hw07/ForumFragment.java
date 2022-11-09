package edu.uncc.hw07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Forum");

        fragForumBinder.textViewForumCreatedBy.setText(currentForum.getCreated_by_name());
        fragForumBinder.textViewForumText.setText(currentForum.getForum_description());
        fragForumBinder.textViewForumTitle.setText(currentForum.getForum_name());
    }
}