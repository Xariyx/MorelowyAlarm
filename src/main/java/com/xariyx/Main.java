package com.xariyx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Morelowy Alarm Cenowy finder!");
        System.out.println();
        System.out.println("Write by what list will be sorted: ");
        System.out.println("1. Name");
        System.out.println("2. NewPrice");
        System.out.println("3. OldPrice");
        System.out.println("4. DiscountFlat");
        System.out.println("5. DiscountPercent (Default)");
        String sortBy = scanner.nextLine();
        System.out.println();
        System.out.println("Write order which list will be sorted by:");
        System.out.println("1. Asc (Default)");
        System.out.println("2. Desc");
        String sortOrder = scanner.nextLine();

        long start = System.currentTimeMillis();
        String url = "https://lp.morele.net/alarmcenowy/";

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error while connecting to site!");
            return;
        }
        Element offerWrapper = doc.getElementsByClass("product-list-container").first();
        if (offerWrapper == null) {
            System.out.println("There was an error while parsing offer wrapper");
            return;
        }

        Elements offerElements = offerWrapper.children();

        ArrayList<Offer> offers = new ArrayList<>(offerElements.size());
        for (Element offerElement : offerElements) {
            offers.add(new Offer(offerElement));
        }

        long fetchTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        sort(offers, sortBy, sortOrder);
        long sortTime = System.currentTimeMillis() - start;
        for (Offer offer : offers) {
            System.out.println(offer.toString());
        }

        System.out.println("Showing " + offers.size() + " offers");
        System.out.println("Fetching time was " + fetchTime + "ms");
        System.out.println("Sorting time was " + sortTime + "ms");

    }

    private static void sort(ArrayList<Offer> toSort, String sortBy, String sortOrder) {
        sortBy = sortBy.toLowerCase();
        sortOrder = sortOrder.toLowerCase();
        if (!(sortOrder.equals("desc") || sortOrder.equals("asc"))) {
            sortOrder = "asc";
        }

        switch (sortBy) {
            case "name" -> toSort.sort(new OfferComparator.Name());
            case "newprice" -> toSort.sort(new OfferComparator.NewPrice());
            case "oldprice" -> toSort.sort(new OfferComparator.OldPrice());
            case "discountflat" -> toSort.sort(new OfferComparator.DiscountFlat());
            default -> toSort.sort(new OfferComparator.DiscountPercent());
        }


        if (sortOrder.equals("desc")) {
            Collections.reverse(toSort);
        }

    }


}
