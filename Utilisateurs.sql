-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:8889
-- Généré le : dim. 27 avr. 2025 à 19:46
-- Version du serveur : 5.7.39
-- Version de PHP : 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `7inheaven`
--

-- --------------------------------------------------------

--
-- Structure de la table `Utilisateurs`
--

CREATE TABLE `Utilisateurs` (
  `id` int(11) NOT NULL,
  `nom_utilisateur` varchar(50) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `role` enum('client','admin') NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Utilisateurs`
--

INSERT INTO `Utilisateurs` (`id`, `nom_utilisateur`, `mot_de_passe`, `role`, `nom`, `prenom`, `email`) VALUES
(1, 'jean.dupont', 'oiseau', 'client', 'Dupont', 'Jean', 'jean.dupont@email.com'),
(2, 'jeanne.leclerc', 'chien', 'admin', 'Leclerc', 'Jeanne', 'admin@7inheaven.com'),
(3, 'marie.dubois', 'chat', 'client', 'Dubois', 'Marie', 'marie.dubois@email.com'),
(4, 'client', '948fe603f61dc036b5c596dc09fe3ce3f3d30dc90f024c85f3c82db2ccab679d', 'client', 'client', 'client', 'client@client.com'),
(5, 'max', '9baf3a40312f39849f46dad1040f2f039f1cffa1238c41e9db675315cfad39b6', 'client', 'ECE', 'max', 'max@ECE.fr'),
(6, 'victor', '99bde068af2d49ed7fc8b8fa79abe13a6059e0db320bb73459fd96624bb4b33f', 'client', 'ECE', 'victor', 'victor@ECE.fr'),
(7, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', 'ECEadmin', 'Maxadmin', 'admin@ECE.fr'),
(8, 'ece', '05832a04f499bc0ffe2b77dc6c62d1fa7e45b6944be20689f071ddf7bccf4d05', 'admin', 'ece', 'ece', 'ece@ECE.fr');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Utilisateurs`
--
ALTER TABLE `Utilisateurs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nom_utilisateur` (`nom_utilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `Utilisateurs`
--
ALTER TABLE `Utilisateurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
