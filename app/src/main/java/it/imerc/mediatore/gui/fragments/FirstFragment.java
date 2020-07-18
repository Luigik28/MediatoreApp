package it.imerc.mediatore.gui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import it.imerc.mediatore.Game.GameManager;
import it.imerc.mediatore.R;
import it.imerc.mediatore.wsClient.operations.NumeroGiocatoriOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class FirstFragment extends Fragment {

    final GameManager gameManager = GameManager.getGameManager();
    private ProgressBar progressBar;
    private TextView textProgress;
    private Spinner playersSpinner;

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
        progressBar = view.findViewById(R.id.progressBar);
        textProgress = view.findViewById(R.id.textViewProgress);
        playersSpinner = view.findViewById(R.id.numberSpinner);
        final View thisView = view;
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO: Aggiungere controlli di inizio partita prima di effettuare la chiamata (es. stringa vuota)
                hideKeyboard(requireActivity());
                progressBar.setVisibility(View.VISIBLE);
                final String nome = editTextHost.getText().toString();
                gameManager.creaPartita(nome, new IntegerCallback() {
                    @Override
                    public void onResponse(Integer response) {
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
                                        gameManager.creaPartita(nome, new IntegerCallback() {
                                            @Override
                                            public void onResponse(Integer response) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("ID PARTITA: " + gameManager.getIdPartita());
        builder.setMessage("Ciao! Hai create la partita con id " + gameManager.getIdPartita() + ", " +
                "fai vedere questo numero ai tuoi amici per iniziare ad arrassarti!\n" +
                "Nel frattempo, vuoi giocare con il monte?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameManager.setMonte(false, getPostMonteCallback());
            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameManager.setMonte(true, getPostMonteCallback());
            }
        });
        builder.show().getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(null);
    }

    private BooleanCallback getPostMonteCallback() {
        return new BooleanCallback() {
            @Override
            public void onResponse(Boolean response) {
                progressBar.setVisibility(View.VISIBLE);
                textProgress.setVisibility(View.VISIBLE);
                textProgress.setText("Numero giocatori: 1");
                Timer timer = new Timer();
                timer.schedule(new TaskGetGiocatori(textProgress),0,1000);
            }
        };
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
            System.out.println("calcolo");
                    new NumeroGiocatoriOperation().doCall(GameManager.getGameManager().getIdPartita(), new IntegerCallback() {
                        @Override
                        public void onResponse(Integer response) {
                    editText.setText("ID Partita: " + gameManager.getIdPartita() + "\nNumero giocatori: " + response);
                    if(response.equals(Integer.valueOf(FirstFragment.this.playersSpinner.getSelectedItem().toString()))) {
                        TaskGetGiocatori.this.cancel();
                        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, gameManager.getBundle());
                    }
                }
            });
        }
    }
}