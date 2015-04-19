package com.teamawesome.swap;

import java.util.GregorianCalendar;

/**
 * Created by llam on 4/18/15.
 */
public class CardSimplified {
    public int icon;
    public String title;
    public String author;
    public GregorianCalendar date;

    public CardSimplified() {
        super();
    }

    public CardSimplified(int icon, String title, String author, GregorianCalendar date) {
        super();
        this.icon = icon;
        this.title = title;
        this.author = author;
        this.date = date;
    }
}
