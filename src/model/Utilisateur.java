package model;

import java.time.LocalDateTime;

public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private String role; // "client" ou "admin"
    private LocalDateTime derniereConnexion;

    public Utilisateur(int id, String nomUtilisateur, String motDePasse, String role) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.derniereConnexion = LocalDateTime.now();
    }

    //test

    public Utilisateur(String nomUtilisateur, String motDePasse, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.derniereConnexion = LocalDateTime.now();
    }
//test
    //test
    public int getId() {
        return id;
    }//please verify

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }//to be verified

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }//test

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }//test
    //test

    public void setRole(String role) {
        this.role = role;
    }//test

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public boolean estAdmin() {
        return "admin".equals(role);
    }

    @Override
    public String toString() {
        return nomUtilisateur + " (" + role + ")";
    }
}