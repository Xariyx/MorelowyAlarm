package com.xariyx;

import org.jsoup.nodes.Element;

public class Offer {

    private final String link;
    private final String name;
    private final float discountPercent;
    private final float discountFlat;
    private final float newPrice;
    private final float oldPrice;

    public Offer(Element element) {
        this.link = element.child(0).attributes().get("href");
        this.name = element.attributes().get("data-product-name").trim();
        this.discountPercent = regexPercent(element.getElementsByClass("price-badge").first().text());
        this.newPrice = regexPrice(element.getElementsByClass("product-slider-price").first().getElementsByClass("price-new").first().text());
        this.oldPrice = regexPrice(element.getElementsByClass("product-slider-price").first().getElementsByClass("price-old").first().text());
        this.discountFlat = round(oldPrice - newPrice, 2);
    }


    public static float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\n" +
                "Link: " + this.link + "\n" +
                "New price: " + this.newPrice + "\n" +
                "Old price: " + this.oldPrice + "\n" +
                "Discount percent: " + this.discountPercent + "\n" +
                "Discount flat: " + this.discountFlat + "\n";

    }

    private float regexPercent(String text) {
        text = text.replace("-", "");
        text = text.replace("%", "");
        text = text.replace(" ", "");
        return Float.parseFloat(text);
    }

    private float regexPrice(String text) {
        text = text.replace("zł", "");
        text = text.replace(" ", "");
        text = text.replace(',', '.');
        return Float.parseFloat(text);
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public float getDiscountFlat() {
        return discountFlat;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public float getOldPrice() {
        return oldPrice;
    }
}
