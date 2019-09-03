package com.alekseysamoylov.reactor;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main {

  public static void main(String[] args) {
    DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(LocalDate.parse("2018-01-22", localDateFormatter));
    System.out.println(new SimpleDateFormat().format( new Date()));
  }

}


