package com.feut.shared.models;

import java.util.Date;

public class Product extends Model {
    public int productId;
    public int huisId;
    public String beschrijving;
    public int aantal;
    public int gekocht;
    public Date datumAanvraag;
    public Date datumGekocht;
    public int gekochtDoorId;
    public int aangevraagdDoorId;
}
