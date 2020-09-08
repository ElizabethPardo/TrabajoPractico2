package com.example.trabajopractico2;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.util.Date;

public class AccesoADatos extends Service {

    int x=0;
    public AccesoADatos() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       final Uri sms= Telephony.Sms.CONTENT_URI;
       final ContentResolver cr= getContentResolver();


        Runnable Leer = new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    Cursor c=cr.query(sms, null, null,null,null);

                    if(c.getCount() > 0)
                    { int i=0;
                        while (c.moveToNext() && i< 5) {
                            String nro = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                            String fecha = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.DATE));
                            Long dataLong = Long.parseLong(fecha);
                            Date date= new Date(dataLong);
                            String contenido= c.getString(c.getColumnIndex(Telephony.Sms.Inbox.BODY));
                            Log.d("Salida ", "Numero " + nro + "Recibido al: " + date.toString()+ "--" + contenido);
                            i++;

                        }

                        try {
                            Thread.sleep(9000);
                            c.moveToFirst();
                        }catch (InterruptedException e)
                        {
                            Log.e("Error",e.getMessage() );
                        }

                    }

                }
            }
        };
        Thread trabajador= new Thread(Leer);
        trabajador.start();
        return  START_STICKY;


    }


}
