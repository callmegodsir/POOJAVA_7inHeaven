package model;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private List<ArticlePanier> articles;
    private Client client;

    public Panier(Client client) {
        this.client = client;
        this.articles = new ArrayList<>();
    }

    public void ajouterArticle(ProduitMarque produitMarque, int quantite) {
        // Vérifier si l'article existe déjà dans le panier
        for (ArticlePanier article : articles) {
            if (article.getProduitMarque().getId() == produitMarque.getId()) {
                article.setQuantite(article.getQuantite() + quantite);
                return;
            }
        }

        // Sinon ajouter un nouvel article
        articles.add(new ArticlePanier(produitMarque, quantite));
    }

    public void modifierQuantite(ProduitMarque produitMarque, int nouvelleQuantite) {
        for (ArticlePanier article : articles) {
            if (article.getProduitMarque().getId() == produitMarque.getId()) {
                if (nouvelleQuantite <= 0) {
                    articles.remove(article);
                } else {
                    article.setQuantite(nouvelleQuantite);
                }
                return;
            }
        }
    }

    public void supprimerArticle(ProduitMarque produitMarque) {
        articles.removeIf(article -> article.getProduitMarque().getId() == produitMarque.getId());
    }

    public List<ArticlePanier> getArticles() {
        return articles;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getTotal() {
        return articles.stream().mapToDouble(ArticlePanier::getPrixTotal).sum();
    }

    public double getTotalReduction() {
        return articles.stream().mapToDouble(ArticlePanier::getReduction).sum();
    }

    public int getNombreArticles() {
        return articles.stream().mapToInt(ArticlePanier::getQuantite).sum();
    }

    public void vider() {
        articles.clear();
    }
}