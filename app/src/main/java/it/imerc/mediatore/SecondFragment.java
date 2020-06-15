package it.imerc.mediatore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import it.imerc.mediatore.Game.Carta;
import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.util.Utility;
import it.imerc.mediatore.wsClient.Client;
import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.AddGiocatoreOperation;
import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.DaiCarteOperation;
import it.imerc.mediatore.wsClient.operations.GetMazzoGiocatore;
import it.imerc.mediatore.wsClient.operations.Ping;
import it.imerc.mediatore.wsClient.operations.SetMonteOperation;

public class SecondFragment extends Fragment {

    private Activity activity;
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
        draw(view);
    }

    private boolean draw(View root) {
        final LinearLayout layout = root.findViewById(R.id.image_container);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Integer[] v = new Integer[1];
        new CreaPartitaOperation().doCall("a", new MediatoreOperation.IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                v[0] = response;
                final Integer id = v[0];
                final Giocatore[] giocatores = new Giocatore[4];
                new AddGiocatoreOperation().doCall(id, "b", new MediatoreOperation.BooleanCallback() {
                    @Override
                    public void onResponse(Boolean response) {
                        new AddGiocatoreOperation().doCall(id, "c", new MediatoreOperation.BooleanCallback() {
                            @Override
                            public void onResponse(Boolean response) {
                                new AddGiocatoreOperation().doCall(id, "d", new MediatoreOperation.BooleanCallback() {
                                    @Override
                                    public void onResponse(Boolean response) {
                                        new SetMonteOperation().doCall(id, true, new MediatoreOperation.BooleanCallback() {
                                            @Override
                                            public void onResponse(Boolean response) {
                                                new DaiCarteOperation().doCall(id, new MediatoreOperation.MazzoCallback() {
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
                                                            new GetMazzoGiocatore().doCall(id, "y", new MediatoreOperation.MazzoCallback() {
                                                                @Override
                                                                public void onResponse(Mazzo response) {

                                                                }

                                                                @Override
                                                                public void onError() {
                                                                    super.onError();
                                                                    //ERRORE!!!
                                                                }
                                                            });
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