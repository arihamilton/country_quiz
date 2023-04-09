package edu.uga.cs.countryquiz;

/**
 * This class (a POJO) represents a single quiz, including the id, date,
 * and result.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Question {

    private String country;
    private String correctContinent;
    private String wrongContinent1;
    private String wrongContinent2;

    public Question()
    {
        this.country = null;
        this.correctContinent = null;
        this.wrongContinent1 = null;
        this.wrongContinent2 = null;
    }

    public Question(String country, String correctContinent, String wrongContinent1, String wrongContinent2 ) {
        this.country = country;
        this.correctContinent = correctContinent;
        this.wrongContinent1 = wrongContinent1;
        this.wrongContinent2 = wrongContinent2;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCorrectContinent()
    {
        return correctContinent;
    }

    public void setCorrectContinent(String correctContinent)
    {
        this.correctContinent = correctContinent;
    }

    public String getWrongContinent1()
    {
        return wrongContinent1;
    }

    public void setWrongContinent1(String wrongContinent1)
    {
        this.wrongContinent1 = wrongContinent1;
    }

    public String getWrongContinent2()
    {
        return wrongContinent2;
    }

    public void setWrongContinent2(String wrongContinent2)
    {
        this.wrongContinent2 = wrongContinent2;
    }

    public String toString()
    {
        return country + ": " + correctContinent;
    }
}
