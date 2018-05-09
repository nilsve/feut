package com.app.feut.feut;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    int position;
    private ListView chatListView;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        // bundle.putInt("pos", position); Miss ooit.
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // position = getArguments().getInt("pos"); Misschien hebben we dit ooit nodig.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatListView = (ListView) view.findViewById(R.id.chatListView);

        ArrayList<String> chatContent = new ArrayList<>();
        chatContent.add("Indy:\tA");
        chatContent.add("Nils:\tB");
        chatContent.add("Michelle:\tC");
        chatContent.add("Laura:\tD");

        ArrayAdapter<String> chatArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, chatContent);
        chatListView.setAdapter(chatArrayAdapter);

    }
}
