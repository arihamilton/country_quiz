package edu.uga.cs.countryquiz;

/**
 * This class (a POJO) represents a single job lead, including the id, company name,
 * phone number, URL, and some comments.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Quiz {

    private long   id;
    private String date;
    private String result;

    public Quiz()
    {
        this.id = -1;
        this.date = null;
        this.result = null;
    }

    public Quiz(String date, String result ) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.date = date;
        this.result = result;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String toString()
    {
        return id + ": " + date + " " + result;
    }
}
