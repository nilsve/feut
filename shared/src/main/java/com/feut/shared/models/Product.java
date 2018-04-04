package com.feut.shared.models;

public class Product {
    String product_naam = "aadrappdpdle";
    VoorraadMutatie[] mutaties;

    int getVoorraad() {
        int totaleVoorraad = 0;
        for (VoorraadMutatie mutatie: mutaties) {
            totaleVoorraad += mutatie.aantalVeranderd;
        }

        return totaleVoorraad;
    }
}
