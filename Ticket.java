package rmi.test;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Ticket {
    private int local;
    private Date date;

    public Ticket(int l, String d, int h) {
        try {
            SimpleDateFormat dateFormatted = new SimpleDateFormat("dd/MM/yy HH:mm");
            local = l;
            date = dateFormatted.parse(d + String.format(" %d:00", h));
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public int getLocal() {
        return local;
    }

    public Date getDate() {
        return date;
    }
}