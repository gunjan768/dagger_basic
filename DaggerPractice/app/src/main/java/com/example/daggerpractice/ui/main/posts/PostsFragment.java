package com.example.daggerpractice.ui.main.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daggerpractice.R;
import com.example.daggerpractice.models.Post;
import com.example.daggerpractice.ui.main.Resource;
import com.example.daggerpractice.util.VerticalSpacingItemDecoration;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PostsFragment extends DaggerFragment
{
    private static final String TAG = "PostsFragments";

    private PostsViewModel viewModel;
    private RecyclerView recyclerView;

    @Inject
    PostsRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    // It's easier to instantiate the recycler view here as we have access to View instance.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.recycler_view);

        viewModel = new ViewModelProvider(this, providerFactory).get(PostsViewModel.class);

        initRecyclerView();
        subscribeObserver();
    }

    private void subscribeObserver()
    {
        viewModel.observePosts().removeObservers(getViewLifecycleOwner());

        viewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>()
        {
            @Override
            public void onChanged(Resource<List<Post>> listResource)
            {
                if(listResource != null)
                {
                    switch(listResource.status)
                    {
                        case LOADING :
                        {
                            Log.d(TAG, "onChanged: LOADING...");

                            break;
                        }

                        case SUCCESS :
                        {
                            Log.d(TAG, "onChanged: got posts...");

                            adapter.setPosts(listResource.data);

                            break;
                        }

                        case ERROR :
                        {
                            Log.e(TAG, "onChanged: ERROR..." + listResource.message );

                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);

        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }
}