package model;

public class ProduitMarque {
    private int id;
    private Produit produit;
    private Marque marque;
    private double prixUnitaire;
    private double prixLot;
    private int quantiteLot;
    private int stock;
    private String imageUrl;

    public ProduitMarque(int id, Produit produit, Marque marque, double prixUnitaire, double prixLot, int quantiteLot, int stock) {
        this.id = id;
        this.produit = produit;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixLot = prixLot;
        this.quantiteLot = quantiteLot;
        this.stock = stock;
        this.imageUrl = "/logo.png"; // Par défaut
    }

    public ProduitMarque(Produit produit, Marque marque, double prixUnitaire, double prixLot, int quantiteLot, int stock) {
        this.produit = produit;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixLot = prixLot;
        this.quantiteLot = quantiteLot;
        this.stock = stock;
        this.imageUrl = "/logo.png"; // Par défaut
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Marque getMarque() {
        return marque;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getPrixLot() {
        return prixLot;
    }

    public void setPrixLot(double prixLot) {
        this.prixLot = prixLot;
    }

    public int getQuantiteLot() {
        return quantiteLot;
    }

    public void setQuantiteLot(int quantiteLot) {
        this.quantiteLot = quantiteLot;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double calculerPrix(int quantite) {
        if (quantiteLot <= 1 || prixLot <= 0) {
            return quantite * prixUnitaire;
        }

        int nombreLots = quantite / quantiteLot;
        int unites = quantite % quantiteLot;

        return (nombreLots * prixLot) + (unites * prixUnitaire);
    }

    public double calculerReduction(int quantite) {
        double prixSansRemise = quantite * prixUnitaire;
        double prixAvecRemise = calculerPrix(quantite);
        return prixSansRemise - prixAvecRemise;
    }

    @Override
    public String toString() {
        return produit.getNom() + " (" + marque.getNom() + ")";
    }
}