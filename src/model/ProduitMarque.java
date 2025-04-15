package model;

public class ProduitMarque {
    private int id;
    private Produit produit;
    private Marque marque;
    private double prixUnitaire;
    private double prixGroupe;
    private int quantiteGroupe;
    private int stock;
    private String imageUrl;

    public ProduitMarque() {

        this.imageUrl = "/logo.png"; // Par défaut
    }

    public ProduitMarque(Produit produit, Marque marque, double prixUnitaire, double prixLot, int quantiteLot, int stock) {
        this.produit = produit;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixGroupe = prixLot;
        this.quantiteGroupe = quantiteLot;
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

    public Double getPrixGroupe() { return prixGroupe; }
    public void setPrixGroupe(Double prixGroupe) { this.prixGroupe = prixGroupe; }

    public Integer getQuantiteGroupe() { return this.quantiteGroupe; }
    public void setQuantiteGroupe(Integer quantiteGroupe) { this.quantiteGroupe = quantiteGroupe; }

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
        if (quantiteGroupe <= 1 || prixGroupe <= 0) {
            return quantite * prixUnitaire;
        }

        int nombreLots = quantite / quantiteGroupe;
        int unites = quantite % quantiteGroupe;

        return (nombreLots * prixGroupe) + (unites * prixUnitaire);
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