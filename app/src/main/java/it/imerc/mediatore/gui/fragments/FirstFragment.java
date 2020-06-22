package it.imerc.mediatore.gui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import it.imerc.mediatore.Game.GameManager;
import it.imerc.mediatore.R;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class FirstFragment extends Fragment {

    final GameManager gameManager = new GameManager();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final EditText editTextHost = view.findViewById(R.id.host);
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Aggiungere controlli di inizio partita prima di effettuare la chiamata (es. stringa vuota)
                progressBar.setVisibility(View.VISIBLE);
                final String nome = editTextHost.getText().toString();
                gameManager.creaPartita(nome, new IntegerCallback() {
                    @Override
                    public void onResponse(Integer response) {
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment, gameManager.getBundle());
                    }
                });

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SharedPreferences p = requireContext().getSharedPreferences(getString(R.string.preferencesKey),Context.MODE_PRIVATE);
        Log.d("AddNewRecord", "getAll: " + p.getAll());
        Log.d("AddNewRecord", "Size: " + p.getAll().size());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences p = requireContext().getSharedPreferences(getString(R.string.preferencesKey),Context.MODE_PRIVATE);
        Log.d("AddNewRecord", "getAll: " + p.getAll());
        Log.d("AddNewRecord", "Size: " + p.getAll().size());
    }

    //    public ActionBar getSupportActionBar() {
//        return ((AppCompatActivity) requireActivity()).getSupportActionBar();
//    }
}