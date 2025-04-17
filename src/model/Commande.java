package model;

import java.util.Date;

public class Commande {
    private int id;
    private int clientId;
    private Date dateCommande;
    private String statut;
    private double total;

    public Commande() {
        // Constructeur par défaut
    }

    public Commande(int id, int clientId, Date dateCommande, String statut, double total) {
        this.id = id;
        this.clientId = clientId;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.total = total;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}