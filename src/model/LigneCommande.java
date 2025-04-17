package model;

public class LigneCommande {
    private int id;
    private int commandeId;
    private int produitMarqueId;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {
        // Constructeur par défaut
    }

    public LigneCommande(int id, int commandeId, int produitMarqueId, int quantite, double prixUnitaire) {
        this.id = id;
        this.commandeId = commandeId;
        this.produitMarqueId = produitMarqueId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getProduitMarqueId() {
        return produitMarqueId;
    }

    public void setProduitMarqueId(int produitMarqueId) {
        this.produitMarqueId = produitMarqueId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}