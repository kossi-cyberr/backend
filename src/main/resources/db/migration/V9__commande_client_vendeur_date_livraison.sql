-- Ajoute sur la table commande_client :
--   - vendeur_id      : utilisateur (vendeur) ayant créé la commande (renseigné côté serveur)
--   - date_livraison  : date de livraison réelle, renseignée au passage à l'état LIVREE
-- Compatible avec une base fraîche (table absente : bloc ignoré, Hibernate crée le schéma)
-- et une base existante (colonnes ajoutées seulement si absentes).
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'commande_client') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name = 'commande_client' AND column_name = 'vendeur_id') THEN
            ALTER TABLE commande_client ADD COLUMN vendeur_id bigint;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name = 'commande_client' AND column_name = 'date_livraison') THEN
            ALTER TABLE commande_client ADD COLUMN date_livraison timestamp;
        end if;
    END IF;
END $$;
