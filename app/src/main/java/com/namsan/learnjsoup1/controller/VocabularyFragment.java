package com.namsan.learnjsoup1.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.namsan.learnjsoup1.view.HomeActivity;
import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.view.SavedWordAdapter;
import com.namsan.learnjsoup1.dao.SavedWordHandler;
import com.namsan.learnjsoup1.entity.WordExtend;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyFragment extends Fragment {
    View view;

    ArrayList<WordExtend> wordExtendArrayList;

    Spinner sortBy;

    RecyclerView recyclerView;

    SavedWordAdapter savedWordAdapter;

    SavedWordHandler savedWordHandler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VocabularyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocabularyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocabularyFragment newInstance(String param1, String param2) {
        VocabularyFragment fragment = new VocabularyFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        String sort= "new";
        loadSavedWords(sort);
    }

    public void loadSavedWords(String sort){
        recyclerView = view.findViewById(R.id.vocab_frag_recycler_view);

        savedWordHandler = new SavedWordHandler(getActivity());
        sortBy = view.findViewById(R.id.spinner);
        wordExtendArrayList = savedWordHandler.readSavedWords(sort);
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        loadSavedWords("new");

                        break;
                    case 1:
                        loadSavedWords("old");
                        break;
                    case 2:
                        loadSavedWords("az");
                        break;
                    case 3:
                        loadSavedWords("za");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                wordExtendArrayList = savedWordHandler.readSavedWords("new");
            }
        });

        savedWordAdapter = new SavedWordAdapter(wordExtendArrayList, getActivity(), new SavedWordAdapter.ClickHandler() {
            @Override
            public void deleteButtonClick(int position) {
                deleteWord(position);
            }

            @Override
            public void onWordCLick(int position) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("inputSearch", wordExtendArrayList.get(position).getUrl());
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                deleteWord(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(savedWordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void deleteWord(int position){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.delete_warning_dialog,null, false);
        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Warning!!!")
                .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        recyclerView.setAdapter(savedWordAdapter);
                    }
                }).setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                savedWordHandler.delete(wordExtendArrayList.get(position).getId());
                wordExtendArrayList.remove(position);
                savedWordAdapter.notifyItemRemoved(position);
            }
        }).show();
    }
}