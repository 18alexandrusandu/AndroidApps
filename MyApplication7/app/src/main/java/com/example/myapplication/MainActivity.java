package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.slice.widget.EventInfo;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {
    static final String Tag="MainActivity";
     EventInfo2 event;
     private int[] taskuri;
     private Date currentTime2;
    private MediaPlayer alarm;
    final String[] taskNames = {"proiect pmp","proiect is","proiect pg","proiect ssc","proiect ia","pauza"};
   final int[] colors={Color.RED,Color.YELLOW,Color.GREEN,Color.BLUE,Color.GRAY,Color.MAGENTA};
    private TextView TV;
   private  Button b,b2;

    public int takeRandomTaskMinim(int totalSize)
    {
          Random r=new Random();
           int number_elements=0;
                 int[] nouaLista=new int [totalSize];

          int min=taskuri[0];
          for(int i=1;i<totalSize;i++)
           if(taskuri[i]<min)
                min=taskuri[i];

        for(int i=1;i<totalSize;i++)
            if(taskuri[i]==min)
            {   nouaLista[number_elements]=i;
                number_elements++;

            }
            int randomIndex=r.nextInt(number_elements-1);
            int randomTask=nouaLista[randomIndex];
            return randomTask;


    }
    EventInfo2 buildNewEvent(Date currentTime )
  {
      Date newDate=new Date();
      int newMinute=currentTime.getMinutes()+10;
      int newHour=currentTime.getHours();
      if(newMinute>=60)
      {  newHour= (newHour+1)%24;

          newMinute=newMinute%60;
      }
      newDate.setHours(newHour);
      newDate.setMinutes(newMinute);
      newDate.setSeconds(0);
      EventInfo2 newEvent=new EventInfo2();

      int index=takeRandomTaskMinim(6);

      newEvent.setEvent(taskNames[index]);
      newEvent.setDate(newDate);
      newEvent.setColor(colors[index]);
      return newEvent;
  }
     private boolean OnOff=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskuri=new int[6];
        for(int i=0;i<6;i++)
            taskuri[i]=0;
        event = new EventInfo2();
        Date currentTime = Calendar.getInstance().getTime();
        event=buildNewEvent(currentTime );
        b2= (Button)findViewById(R.id.button2);

  b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       OnOff=!OnOff;
       if(OnOff)
        b2.setText("On");
       else
           b2.setText("Off");





    }
});


        TV=(TextView) findViewById(R.id.textview);
         b= (Button)findViewById(R.id.button);

         b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag,"pressed buuton "+OnOff);
                OnOff=!OnOff;
                if(OnOff)
                {

                }
                else
                 alarm.stop();





            }
        });






        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {



                while (true) {

                    Date currentTime;
                    currentTime = Calendar.getInstance().getTime();


                   TV.post(new Runnable() {
                       @Override
                       public void run() {
                           Date currentTime;
                           currentTime = Calendar.getInstance().getTime();
                           TV.setText(currentTime.toString());
                       }
                   });





                    if (currentTime.getHours() == event.getDate().getHours())
                        if (currentTime.getMinutes() == event.getDate().getMinutes()) {

                             Log.d(Tag,event.getEvent());
                             if(event!=null)
                                if(event.getEvent()!=null ){

                                    b.setText(event.getEvent());
                                    b.setBackgroundColor(event.getColor());
                                }

                                else
                                 b.setText("null event");
                               else
                                   b.setText("null event");
                            EventInfo2 newEvent = buildNewEvent(currentTime);
                            event=newEvent;
                            Log.d(Tag,"new event:"+event.getEvent());;

                            alarm = MediaPlayer.create(MainActivity.this, R.raw.music);
                            alarm.start();

                        }
                }
            }};
        Thread d=new Thread(myRunnable);
          d.start();
    }
}