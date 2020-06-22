package it.imerc.mediatore.gui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import it.imerc.mediatore.Game.Carta;
import it.imerc.mediatore.Game.GameManager;
import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.gui.activities.MainActivity;
import it.imerc.mediatore.R;
import it.imerc.mediatore.util.Utility;
import it.imerc.mediatore.wsClient.operations.AddGiocatoreOperation;
import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.DaiCarteOperation;
import it.imerc.mediatore.wsClient.operations.SetMonteOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoreCallback;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;
import it.imerc.mediatore.wsClient.operations.callback.MazzoCallback;

public class SecondFragment extends Fragment {

    private Activity activity;
    private GameManager gameManager;
    final Mazzo[] mio = new Mazzo[1];

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
        gameManager = GameManager.getGameManager(getArguments());
        Toast.makeText(requireContext(), String.valueOf(gameManager.getIdPartita()) + " " + gameManager.getHost().getNome(), Toast.LENGTH_LONG).show();
        //draw(view);
    }

    private boolean draw(@NonNull View root) {
        final LinearLayout layout = root.findViewById(R.id.image_container);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Integer[] v = new Integer[1];
        new CreaPartitaOperation().doCall("a", new IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                v[0] = response;
                final Integer id = v[0];
                final Giocatore[] giocatores = new Giocatore[4];
                new AddGiocatoreOperation().doCall(id, "b", new GiocatoreCallback() {
                    @Override
                    public void onResponse(Giocatore response) {
                        new AddGiocatoreOperation().doCall(id, "c", new GiocatoreCallback() {
                            @Override
                            public void onResponse(Giocatore response) {
                                new AddGiocatoreOperation().doCall(id, "d", new GiocatoreCallback() {
                                    @Override
                                    public void onResponse(Giocatore response) {
                                        new SetMonteOperation().doCall(id, true, new BooleanCallback() {
                                            @Override
                                            public void onResponse(Boolean response) {
                                                new DaiCarteOperation().doCall(id, new MazzoCallback() {
                                                    @Override
                                                    public void onResponse(Mazzo response) {
                                                        mio[0] = response;
                                                        Mazzo m = mio[0];
                                                        for (Carta c : m.getCarteCoperte()) {
                                                            layoutParams.setMargins(20, 20, 20, 20);
                                                            layoutParams.gravity = Gravity.CENTER;
                                                            ImageView imageView = new ImageView(getActivity());
                                                            imageView.setImageResource(Utility.mapCarte.get(c.getId()));
                                                            //imageView.setOnClickListener(documentImageListener);
                                                            imageView.setLayoutParams(layoutParams);

                                                            layout.addView(imageView);
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        super.onError(message);
                        Toast.makeText(SecondFragment.this.requireContext(),message,Toast.LENGTH_LONG).show();
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