package it.imerc.mediatore.gui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.Objects;

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
import it.imerc.mediatore.wsClient.operations.GetTrionfoOperation;
import it.imerc.mediatore.wsClient.operations.NumeroGiocatoriOperation;
import it.imerc.mediatore.wsClient.operations.callback.CartaCallback;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoriCallback;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;
import it.imerc.mediatore.wsClient.operations.callback.MazzoCallback;

public class SecondFragment extends Fragment {

    private Activity activity;
    private GameManager gameManager;

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
        draw(view);

    }

    private boolean draw(@NonNull final View root) {
        final LinearLayout layout = root.findViewById(R.id.my_card);
        final LinearLayout layout2 = root.findViewById(R.id.my_card2);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final ImageView trionfo = root.findViewById(R.id.trionfo);
        ((TextView)root.findViewById(R.id.textHost)).setText(gameManager.getHost().getNome());
        final TextView textGiocatore2 = (TextView) root.findViewById(R.id.textGiocatore2);
        final TextView textGiocatore3 = (VerticalTextView) root.findViewById(R.id.textGiocatore3);
        final TextView textGiocatore4 = (TextView) root.findViewById(R.id.textGiocatore4);
        new DaiCarteOperation().doCall(gameManager.getIdPartita(), new MazzoCallback() {
            @Override
            public void onResponse(Mazzo response) {
                for (Carta c : response.getCarteCoperte()) {
                    gameManager.getHost().mettiInMano(c);
                    layoutParams.setMargins(10, 10, 10, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.height = 300;
                    layoutParams.width = 180;
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(Utility.mapCarte.get(c.getId()));
                    //imageView.setOnClickListener(documentImageListener);
                    imageView.setLayoutParams(layoutParams);
                    if(gameManager.getHost().getMano().getCarteInMano() - 1 < (response.getCarteCoperte().size()) / 2 )
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
                                Giocatore g2 = response.get(1);
                                Giocatore g3 = response.get(2);
                                Giocatore g4 = null;
                                if(gameManager.getnGiocatori() == 4)
                                    g4 = response.get(3);
                                textGiocatore2.setText(g2.getNome().toString());
                                textGiocatore3.setText(g3.getNome().toString());
                                if(g4 != null)
                                    textGiocatore4.setText(g4.getNome().toString());
                            }
                        });
                    }
                });
            }
        });
        return true;
    }

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

}