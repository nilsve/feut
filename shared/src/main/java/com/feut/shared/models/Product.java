package com.feut.shared.models;

import java.util.Date;

public class Product extends Model {
    int productId;
    int huisId;
    String beschrijving;
    int aantal;
    int gekocht;
    Date datumAanvraag;
    Date datumGekocht;
    int gekochtDoorId;
    int aangevraagdDoorId;
}
