package com.example.filingo.api;

import java.util.ArrayList;
public class APIResponse {
    public String word;
    public String phonetic;
    public ArrayList<Phonetic> phonetics;
    public ArrayList<Meaning> meanings;
    public License license;
    public ArrayList<String> sourceUrls;
}
