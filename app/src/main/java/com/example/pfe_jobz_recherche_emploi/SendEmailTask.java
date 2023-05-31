package com.example.pfe_jobz_recherche_emploi;

import android.os.AsyncTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendEmailTask extends AsyncTask<Void, Void, Void> {
    private Message message;

    public SendEmailTask(Message message) {
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}


