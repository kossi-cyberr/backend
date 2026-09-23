-- Ajoute sur la table ventes :
--   - nom_client     : nom du client (optionnel), information enregistrée sur le ticket/la vente
--   - vendeur_id     : utilisateur ayant enregistré la vente (renseigné côté serveur)
-- Compatible avec :
--   - une base fraîche (la table n'existe pas encore, le bloc est ignoré ;
--     Hibernate crée ensuite le schéma avec les colonnes),
--   - une base existante (ajout des colonnes si absentes).
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'ventes') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name = 'ventes' AND column_name = 'nom_client') THEN
            ALTER TABLE ventes ADD COLUMN nom_client varchar(255);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name = 'ventes' AND column_name = 'vendeur_id') THEN
            ALTER TABLE ventes ADD COLUMN vendeur_id bigint;
        END IF;
    END IF;
END $$;
