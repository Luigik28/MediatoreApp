package it.imerc.mediatore.gui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import it.imerc.mediatore.Game.GameManager;
import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.R;
import it.imerc.mediatore.wsClient.operations.AddGiocatoreOperation;
import it.imerc.mediatore.wsClient.operations.GetGiocatoriOperation;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoreCallback;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoriCallback;
import it.imerc.mediatore.wsClient.operations.callback.StringCallback;

public class FirstFragment extends Fragment {

    final GameManager gameManager = GameManager.getGameManager();
    private ProgressBar progressBar;
    private TextView textProgress;
    private final String textGiocatori = "Numero Giocatori: ";

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
        final EditText editTextHost = view.findViewById(R.id.host);
        final EditText editTextGame = view.findViewById(R.id.game);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().show();
        progressBar = view.findViewById(R.id.progressBar);
        textProgress = view.findViewById(R.id.textViewProgress);
        final Spinner playersSpinner = view.findViewById(R.id.numberSpinner);
        final View thisView = view;
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO: Aggiungere controlli di inizio partita prima di effettuare la chiamata (es. stringa vuota)
                hideKeyboard(requireActivity());
                progressBar.setVisibility(View.VISIBLE);
                final String giocatore = editTextHost.getText().toString();
                final String partita = editTextGame.getText().toString();
                gameManager.creaPartita(partita, giocatore, new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        gameManager.setIdPartita(response);
                        gameManager.setnGiocatori(Integer.parseInt(playersSpinner.getSelectedItem().toString()));
                        onGameCreated();
                    }

                    @Override
                    public void onError(String message) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(thisView, "Timeout connessione al server", Snackbar.LENGTH_LONG)
                                .setAction("Riprova", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        gameManager.creaPartita(partita, giocatore, new StringCallback() {
                                            @Override
                                            public void onResponse(String response) {
                                                gameManager.setIdPartita(response);
                                                gameManager.setnGiocatori(Integer.parseInt(playersSpinner.getSelectedItem().toString()));
                                                onGameCreated();
                                            }

                                            @Override
                                            public void onError(String message) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(FirstFragment.this.requireContext(), "Errore di connessione", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).show();
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

    private void onGameCreated() {
        progressBar.setVisibility(View.VISIBLE);
        textProgress.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TaskGetGiocatori(textProgress),500,1000);
        createGameTest();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class TaskGetGiocatori extends TimerTask {

        TextView editText;

        public TaskGetGiocatori(TextView editText) {
            this.editText = editText;
        }

        @Override
        public void run() {
            new GetGiocatoriOperation().doCall(gameManager.getIdPartita(), new GiocatoriCallback() {
                @Override
                public void onResponse(LinkedList<Giocatore> response) {
                    System.out.println("n giocatori sel: " + gameManager.getnGiocatori());
                    System.out.println("size           : " + response.size());
                    StringBuilder textGiocatori = new StringBuilder("ID Partita: ")
                            .append(gameManager.getIdPartita())
                            .append("\n")
                            .append(FirstFragment.this.textGiocatori)
                            .append(response.size());
                    for (Giocatore g : response) {
                        textGiocatori.append("\n").append(g.getNome());
                    }
                    editText.setText(textGiocatori.toString());
                    if(response.size() == gameManager.getnGiocatori()) {
                        TaskGetGiocatori.this.cancel();
                        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, gameManager.getBundle());
                    }
                }
            });
        }
    }


    private void createGameTest() {
        for(int i = 1; i < gameManager.getnGiocatori(); i++)
            new AddGiocatoreOperation().doCall(gameManager.getIdPartita(), "Giocatore" + (i + 1), new GiocatoreCallback() {
                @Override
                public void onResponse(Giocatore response) {

                }
            });
    }
}