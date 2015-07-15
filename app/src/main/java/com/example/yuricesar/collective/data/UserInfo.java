package com.example.yuricesar.collective.data;

import java.util.ArrayList;

public class UserInfo{

    private String id;
    private String name;
    private String email;
    private String picture;

    private ArrayList<String> movies;
    private ArrayList<String> books;
    private ArrayList<String> games;
    private ArrayList<String> music;
    private ArrayList<String> tv;

    public UserInfo(String id, String name, String email, String picture){
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;

    }

    public UserInfo(){}


    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getURLPicture(){
        return picture;
    }

    public ArrayList<String> getMovies(){
        if (movies == null){
            return new ArrayList<String>();
        }
        return movies;
    }

    public ArrayList<String> getBooks(){
        if (books == null){
            return new ArrayList<String>();
        }
        return books;
    }

    public ArrayList<String> getGames(){
        if (games == null){
            return new ArrayList<String>();
        }
        return games;
    }

    public ArrayList<String> getMusic(){
        if (music == null){
            return new ArrayList<String>();
        }
        return music;
    }

    public ArrayList<String> getTv(){
        if (tv == null){
            return new ArrayList<String>();
        }
        return tv;
    }

    public void setId(String id){ this.id = id; }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public void addUserMovies(ArrayList<String> movies){
        this.movies = new ArrayList<String>(movies);
    }

    public void addUserGames(ArrayList<String> games){
        this.games = new ArrayList<String>(games);
    }

    public void addUserBooks(ArrayList<String> books){
        this.books = new ArrayList<String>(books);
    }

    public void addUserMusic(ArrayList<String> music){
        this.music = new ArrayList<String>(music);
    }

    public void addUserTv(ArrayList<String> tv){
        this.tv = new ArrayList<String>(tv);
    }


}
