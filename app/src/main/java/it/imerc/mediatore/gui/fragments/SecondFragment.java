package it.imerc.mediatore.gui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import it.imerc.mediatore.Game.Carta;
import it.imerc.mediatore.Game.GameManager;
import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.gui.activities.MainActivity;
import it.imerc.mediatore.R;
import it.imerc.mediatore.util.Utility;
import it.imerc.mediatore.util.VerticalTextView;
import it.imerc.mediatore.wsClient.operations.DaiCarteOperation;
import it.imerc.mediatore.wsClient.operations.GetGiocatoriOperation;
import it.imerc.mediatore.wsClient.operations.GetMazzoGiocatore;
import it.imerc.mediatore.wsClient.operations.GetTempoRimasto;
import it.imerc.mediatore.wsClient.operations.GetTrionfoOperation;
import it.imerc.mediatore.wsClient.operations.GiocaOperation;
import it.imerc.mediatore.wsClient.operations.UpdateGiocatoriOperation;
import it.imerc.mediatore.wsClient.operations.callback.CartaCallback;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoriCallback;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;
import it.imerc.mediatore.wsClient.operations.callback.MazzoCallback;

public class SecondFragment extends Fragment {

    private Activity activity;
    private View root;
    private GameManager gameManager;
    private TextView textGiocatore;
    private VerticalTextView textGiocatore2;
    private TextView textGiocatore3;
    private VerticalTextView textGiocatore4;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSupportActionBar().hide();
        gameManager = GameManager.getGameManager(getArguments());
        root = view;
        draw();
    }

    private boolean draw() {
        final LinearLayout layout = root.findViewById(R.id.my_card);
        final LinearLayout layout2 = root.findViewById(R.id.my_card2);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final ImageView trionfo = root.findViewById(R.id.trionfo);
        ((TextView)root.findViewById(R.id.textHost)).setText(gameManager.getGiocatore().getNome());
        textGiocatore = (TextView) root.findViewById(R.id.textHost);
        textGiocatore2 = (VerticalTextView) root.findViewById(R.id.textGiocatore2);
        textGiocatore3 = (TextView) root.findViewById(R.id.textGiocatore3);
        textGiocatore4 = (VerticalTextView) root.findViewById(R.id.textGiocatore4);
        final ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progressBarTempo);
        progressBar.setMax(gameManager.getMaxTempo());
        MazzoCallback mioMazzoCallback = new MazzoCallback() {
            @Override
            public void onResponse(Mazzo response) {
                for (Carta c : response.getCarteCoperte()) {
                    gameManager.getGiocatore().mettiInMano(c);
                    layoutParams.setMargins(10, 10, 10, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.height = 300;
                    layoutParams.width = 180;
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(Utility.mapCarte.get(c.getId()));
                    //imageView.setOnClickListener(documentImageListener);
                    imageView.setLayoutParams(layoutParams);
                    if (gameManager.getGiocatore().getMano().getCarteInMano() - 1 < (response.getCarteCoperte().size()) / 2)
                        layout2.addView(imageView);
                    else
                        layout.addView(imageView);
                }
                new GetTrionfoOperation().doCall(gameManager.getIdPartita(), new CartaCallback() {
                    @Override
                    public void onResponse(Carta response) {
                        trionfo.setImageResource(Utility.mapCarte.get(response.getId()));
                        new GetGiocatoriOperation().doCall(gameManager.getIdPartita(), new GiocatoriCallback() {
                            @Override
                            public void onResponse(LinkedList<Giocatore> response) {
                                //TODO: get giocatori in base al VERO id
                                Giocatore g2 = response.get(1);
                                Giocatore g3 = response.get(2);
                                Giocatore g4 = null;
                                if (gameManager.getnGiocatori() == 4)
                                    g4 = response.get(3);
                                textGiocatore2.setText(g2.getNome().toString());
                                textGiocatore3.setText(g3.getNome().toString());
                                if (g4 != null)
                                    textGiocatore4.setText(g4.getNome().toString());
                                startGame(progressBar);
                            }
                        });
                    }
                });
            }
        };
        if(gameManager.isMyTurn()) {
            new DaiCarteOperation().doCall(gameManager.getIdPartita(), mioMazzoCallback);
        } else {
            new GetMazzoGiocatore().doCall(gameManager.getIdPartita(), gameManager.getGiocatore().getNome(), mioMazzoCallback);
        }
        View.OnClickListener listenerAzioni = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameManager.isMyTurn()) {
                    root.findViewById(R.id.buttonPasso).setVisibility(View.GONE);
                    root.findViewById(R.id.buttonChiama).setVisibility(View.GONE);
                    root.findViewById(R.id.buttonSola).setVisibility(View.GONE);
                    gioca(v.getId());
                } else {
                    //TODO: dichiarare prima come si vuole giocare
                }
            }
        };
        root.findViewById(R.id.buttonPasso).setOnClickListener(listenerAzioni);
        root.findViewById(R.id.buttonChiama).setOnClickListener(listenerAzioni);
        root.findViewById(R.id.buttonSola).setOnClickListener(listenerAzioni);
        return true;
    }

    private void drawUpdate(LinkedList<Giocatore> giocatori) {
        for(int i = 0; i < giocatori.size(); i++) {
            disegnaMossaGiocatore(i,giocatori.get(i).getUltimaMossa());
        }
    }

    private void disegnaMossaGiocatore(int idGiocatore, int mossa) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (activity == null && context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setHasOptionsMenu(false);
        }
        super.onDetach();
    }

    private void startGame(ProgressBar progressBar) {
        cambiaGiocatoreAttivo(gameManager.getGiocatoreAttivo());
        Timer t = new Timer();
        t.schedule(new SecondFragment.TaskGetPlayersUpdate(progressBar),0,1000);
    }

    private void gioca(int mossa) {
        root.findViewById(R.id.buttonPasso).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.buttonChiama).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.buttonSola).setVisibility(View.INVISIBLE);
        switch (mossa) {
            case R.id.buttonPasso:
                mossa = GameManager.PASSA;
                break;
            case R.id.buttonChiama:
                mossa = GameManager.CHIAMA;
                break;
            case R.id.buttonSola:
                mossa = GameManager.SOLA;
                break;
            case GameManager.TIMEOUT:
                return; 
        }
        new GiocaOperation().doCall(gameManager.getIdPartita(), mossa, new IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                cambiaGiocatoreAttivo(response);
            }
        });
    }

    private void timeout() {
        if(gameManager.isMyTurn())
            gioca(GameManager.TIMEOUT);
    }

    private int getColor(boolean isActive) {
        if(isActive)
            return ContextCompat.getColor(requireContext(), R.color.design_default_color_surface);
        else
            return ContextCompat.getColor(requireContext(), R.color.design_default_color_on_secondary);
    }

    private void cambiaGiocatoreAttivo(Integer response) {
        switch (response) {
            case 0:
                textGiocatore.setTextColor(getColor(true));
                textGiocatore2.setTextColor(getColor(false));
                textGiocatore3.setTextColor(getColor(false));
                textGiocatore4.setTextColor(getColor(false));
                break;
            case 1:
                textGiocatore.setTextColor(getColor(false));
                textGiocatore2.setTextColor(getColor(true));
                textGiocatore3.setTextColor(getColor(false));
                textGiocatore4.setTextColor(getColor(false));
                break;
            case 2:
                textGiocatore.setTextColor(getColor(false));
                textGiocatore2.setTextColor(getColor(false));
                textGiocatore3.setTextColor(getColor(true));
                textGiocatore4.setTextColor(getColor(false));
                break;
            case 3:
                textGiocatore.setTextColor(getColor(false));
                textGiocatore2.setTextColor(getColor(false));
                textGiocatore3.setTextColor(getColor(false));
                textGiocatore4.setTextColor(getColor(true));
                break;
        }
        gameManager.setGiocatoreAttivo(response);
    }

    private class TaskGetPlayersUpdate extends TimerTask {

        ProgressBar progressBar;

        public TaskGetPlayersUpdate(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            new UpdateGiocatoriOperation().doCall(gameManager.getIdPartita(), new GiocatoriCallback() {
                @Override
                public void onResponse(LinkedList<Giocatore> response) {
                    Log.d("Debug","Giocatore attivo " + gameManager.getGiocatoreAttivo());
                    for(int i = 0; i < response.size(); i++) {
                        Giocatore g = response.get(i);
                        Log.d("Debug","Giocatore " + i + ": " + g.isActive());
                        if(g.isActive()) {
                            progressBar.setProgress(g.getTempoRimasto());
                            if(i != gameManager.getGiocatoreAttivo())
                                cambiaGiocatoreAttivo(i);
                        }

                    }
                    drawUpdate(response);
                }
            });
        }
    }


}