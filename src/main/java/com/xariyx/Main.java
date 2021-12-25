package com.xariyx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Main {
    public static void main(String[] args) {

        if(args[0].equalsIgnoreCase("--help")){
            System.out.println("Showing help for this command.");
            System.out.println("Use switches:");
            System.out.println("    --help - shows this message");
            System.out.println("    --sort-by - sorts by specified variable:");
            System.out.println("        name");
            System.out.println("        newPrice");
            System.out.println("        oldPrice");
            System.out.println("        discountFlat");
            System.out.println("        discountPercent (Default)");
            System.out.println("    --sort-order - sorts by specified order:");
            System.out.println("        asc (Default)");
            System.out.println("        desc");
            System.out.println("    --show-sold - show sold products:");
            System.out.println("        yes");
            System.out.println("        no (Default)");
            return;

        }
        //Name
        //NewPrice
        //OldPrice
        //DiscountFlat
        //DiscountPercent (Default)
        String sortBy = "discountPercent";

        //Asc (Default)
        //Desc
        String sortOrder = "asc";

        //Yes
        //No (Default)
        String showSold = "no";

        boolean skip = false;
        for (int i = 0; i < args.length; i++) {
            if(skip){
                skip = false;
                continue;
            }
            if (args[i].equalsIgnoreCase("--sort-by")){
                sortBy = args[i+1];
                skip = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("--sort-order")){
                sortOrder = args[i+1];
                skip = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("--show-sold")){
                showSold = args[i+1];
                skip = true;
            }
        }


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

        long fetchTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        Elements offerElements = new Elements();
        if(showSold.equalsIgnoreCase("yes")) {
            offerElements = offerWrapper.children();
        }else {
            for (Element child : offerWrapper.children()) {
                if (child.getElementsByClass("product-sold-text").first() == null) {
                    offerElements.add(child);
                }
            }
        }


        ArrayList<Offer> offers = new ArrayList<>(offerElements.size());
        for (Element offerElement : offerElements) {
            offers.add(new Offer(offerElement));
        }

        long constructorTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        sort(offers, sortBy, sortOrder);
        long sortTime = System.currentTimeMillis() - start;
        for (Offer offer : offers) {
            System.out.println(offer.toString());
        }

        System.out.println("Showing " + offers.size() + " offers");
        System.out.println("Fetching time was " + fetchTime + "ms");
        System.out.println("Constructing time was " + constructorTime + "ms");
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
