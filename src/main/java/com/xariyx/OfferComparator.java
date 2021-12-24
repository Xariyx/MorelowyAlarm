package com.xariyx;

import java.util.Comparator;

public class OfferComparator {

    public static class Name implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            return o1.getName().compareTo(o2.getName());
        }

    }

    public static class DiscountFlat implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            return (int) (o1.getDiscountFlat() - o2.getDiscountFlat());
        }

    }

    public static class DiscountPercent implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            return (int) (o1.getDiscountPercent() - o2.getDiscountPercent());
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    public static class NewPrice implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            return (int) (o1.getNewPrice() - o2.getNewPrice());
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    public static class OldPrice implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            return (int) (o1.getOldPrice() - o2.getOldPrice());
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }



}
