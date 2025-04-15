package model;

public class ArticlePanier {
    private ProduitMarque produitMarque;
    private int quantite;
    private double prixTotal;

    public ArticlePanier(ProduitMarque produitMarque, int quantite) {
        this.produitMarque = produitMarque;
        this.quantite = quantite;
        this.calculerPrixTotal();
    }

    public ProduitMarque getProduitMarque() {
        return produitMarque;
    }

    public void setProduitMarque(ProduitMarque produitMarque) {
        this.produitMarque = produitMarque;
        calculerPrixTotal();
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        calculerPrixTotal();
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public double getReduction() {
        return produitMarque.calculerReduction(quantite);
    }

    private void calculerPrixTotal() {
        this.prixTotal = produitMarque.calculerPrix(quantite);
    }

    @Override
    public String toString() {
        return produitMarque.toString() + " x " + quantite;
    }
}