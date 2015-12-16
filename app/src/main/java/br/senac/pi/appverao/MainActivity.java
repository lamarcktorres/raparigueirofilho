package br.senac.pi.appverao;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private Button b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private double tempoInicial = 0;
    private double tempoFinal = 0;
    private android.os.Handler myHandler = new android.os.Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekBar;
    private TextView tx4,tx2,tx3;

    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.button);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        iv = (ImageView)findViewById(R.id.imageView);

        tx2 = (TextView)findViewById(R.id.textView2);
        tx3 = (TextView)findViewById(R.id.textView3);
        tx4 = (TextView)findViewById(R.id.textView4);
        tx4.setText("O estourado da mídia. ");

        mediaPlayer = MediaPlayer.create(this, R.raw.mysong);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        b2.setEnabled(false);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tocando a música", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                tempoFinal = mediaPlayer.getDuration();
                tempoInicial = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekBar.setMax((int) tempoFinal);
                    oneTimeOnly = 1;
                }
                tx3.setText(String.format("%d min, %d seg",
                                TimeUnit.MILLISECONDS.toMinutes((long) tempoFinal),
                                TimeUnit.MILLISECONDS.toSeconds((long) tempoFinal) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tempoFinal)))
                );

                tx2.setText(String.format("%d min, %d seg",
                                TimeUnit.MILLISECONDS.toMinutes((long) tempoInicial),
                                TimeUnit.MILLISECONDS.toSeconds((long) tempoInicial) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tempoInicial)))
                );

                seekBar.setProgress((int) tempoInicial);
                myHandler.postDelayed(UpdateSongTime, 100);
                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Som Pausado" ,Toast.LENGTH_LONG).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp =(int)tempoInicial;

                if((temp+forwardTime)<=tempoFinal){
                    tempoInicial = tempoInicial + forwardTime;
                    mediaPlayer.seekTo((int) tempoInicial);
                    Toast.makeText(getApplicationContext(),"Você saltou 5 segundos",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Não é possível saltar 5 segundos",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)tempoInicial;

                if ((temp-backwardTime)>0){
                    tempoInicial = tempoInicial - backwardTime;
                    mediaPlayer.seekTo((int)tempoInicial);
                    Toast.makeText(getApplicationContext(),"Você saltou 5 segundos",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Não é possível saltar 5 segundos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            tempoInicial = mediaPlayer.getCurrentPosition();
            tx2.setText(String.format("%d min, %d seg",

                            TimeUnit.MILLISECONDS.toMinutes((long) tempoInicial),
                            TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) tempoInicial)))
            );
            seekBar.setProgress((int) tempoInicial);
            myHandler.postDelayed(this, 100);
        }
    };

}
