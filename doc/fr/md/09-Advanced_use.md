# Utilisation avancé de Course Generator

Ce chapitre va vous présenter des utilisations plus complèxe de **Course Generator**.

## Rechercher un point GPS

Il est possible de rechercher un point GPS sur le parcours via le menu "Edition>Recherche d'un point" ou avec le raccourci clavier [Ctrl+F].

![](./images/CG40_Search.png)

Dans la boite de dialogue, il est nécessaire de saisir la latitude et la longitude du point puis d'appuyer sur le bouton recherche ![](./images/Toolbar/search.png).

**Course Generator** va rechercher le point le plus proche des coordonnées saisies. Il indiquera la ligne correspondant au point trouvé ainsi que la distance entre le point trouvé et les coordonnées saisies. La ligne contenant le point trouvé est alors sélectionnée.

## Déterminer les points hauts et bas du parcours

Le menu "Outils>Détermination du mini/maxi" permet de déterminer automatiquement les points haut et bas du parcours.  
Chaque position trouvée est marquée avec un indicateur ![](./images/Tags/high_point.png) (point haut) ou ![](./images/Tags/low_point.png) (point bas).

## Modifier l'altitude d'un point du parcours

Il peut arriver qu'une altitude du parcours soit incohérente. Cela peut arriver si vous, ou un site internet type Openrunner, avez fait une correction automatique des altitudes. La base de donnée SRTM sur lequel se base ces sites comporte des zones sans altitude. La base donnée renvoi alors une altitude de 32768m. Afin de palier à ce problème vous pouvez éditer l'altitude du ou des points concerné.

Pour cela mettez-vous sur la cellule concernée et ouvrez l'éditeur de ligne en faisant un double clique.

La boite dialogue suivante s'affiche:  
![](./images/CG40_Line_Editor.png)

Le champ "Altitude" permet de saisir la nouvelle altitude.

Il sera nécessaire de relancer un calcul avec le bouton ![](./images/Toolbar/refresh_data.png) afin de prendre en compte la nouvelle altitude.

## Fusionner deux parcours

**Course Generator** offre la possibilité de fusionner deux parcours.

La procédure est la suivante:

* Chargez le premier parcours dans **Course Generator** (au format GPX ou CGX).
* Importez le second parcours en utilisant "Fichier>Importer un fichier GPX" ou "Fichier>Importer un fichier CGX".
* La boite de dialogue ci-dessous apparaît:  
![](./images/CG40_Import_Track.png)
* Sélectionnez "Insérer au début" si vous voulez insérer le parcours avant celui présent en mémoire sinon sélectionnez "Ajouter à la fin" et le parcours sera ajouté après celui présent en mémoire.
* La boite de dialogue de sélection de fichier apparaît. Faite votre choix puis cliquez sur "Ouvrir"
* La fusion est alors réalisée

Si vous avez d'autres parcours à fusionner au nouveau parcours, il vous suffit de recommencer la procédure.

Après la fusion du ou des parcours vous faudra reprendre les réglages du coefficient de fatigue et relancer un calcul (bouton ![](./images/Toolbar/refresh_data.png) ou [F5]) car les données temporelles seront fausses.

Une fois que vous avez terminé la fusion et relancé un calcul, vous pourrez sauvegarder le parcours.

## Sauver une partie du parcours

**Course Generator** offre la possibilité de sauver une partie d'un parcours. Cela permet, par exemple, de scinder un parcours en plusieurs parties.

La procédure est la suivante:

* Dans le tableau sélectionnez la première ligne que vous souhaitez sauver,
* En maintenant la touche SHIFT actionnée, sélectionnez la dernière ligne à sauver.
* Sélectionnez "Fichier>Sauver une partie du parcours en xxx" (xxx correspond au format du fichier que vous désirez).
* La boite de dialogue de sauvegarde de fichier apparaît. Saisissez le nom du fichier et valider.
* C'est fini!

## Exporter les tags en waypoints

**Course Generator** offre la possibilité de sauver les points GPS contenant tags (ou indicateur) sous forme de waypoint. Ces waypoints pourront être ajouté à votre GPS afin d'avoir, par exemple, l'affichage du parcours avec des informations complémentaires comme les cols, les ravitaillements et les villes.

La procédure est la suivante:

* Sélectionnez "Fichier>Exporter les tags en waypoint".
* La boite de dialogue ci-dessous apparaît:  
![](./images/CG40_Export_Waypoints.png)
* Sélectionnez les types de tags que vous voulez exporter.
* La boite de dialogue de sauvegarde de fichier apparaît. Saisissez le nom du fichier et valider.
* Les waypoints seront sauvés dans un fichier qui le nom saisi et comme extension GPX

L'exploitation de ce fichier pourra se faire, par exemple, avec le logiciel Basecamp de Garmin.

## Copier le contenu d'une cellule

Le contenu d'une cellule du tableau de donnée peut être copié dans le presse papier afin d'être exploité dans un autre logiciel. Pour réaliser cette action il vous suffit de sélectionner "Edition>Copier". Le contenu de la cellule sélectionnée est copié dans le presse papier au format texte.

## Générer un mini roadbook

**Course Generator** offre la possibilité de générer un mini roadbook. Celui contiendra le profil du parcours et des informations sur vos points de passage. Le mini roadbook est accessible via "Affichage>Mini roadbook".

Le mini-roadbook est au final une image. Cette image pourra être imprimée en utilisant un logiciel de dessin comme Paint, Photoshop ou The Gimp. Elle pourra aussi être exploitée dans d'autre logiciel comme Word, Excel, Inkscape, Illustrator...

Les réglages effectués pour générer le mini roadbook sont sauvés dans le fichier CGX du parcours. Si vous échangez un parcours au format CGX, vous allez échanger le parcours avec tous ses renseignements (ravitaillements, qualité du terrain, barrières horaire...) et le mini roadbook.

Pour pouvoir utiliser le mini roadbook il faut avoir préalablement renseigné votre parcours. Cela inclus:

* Avoir renseigné la difficulté du terrain (utile pour le type "Route/Sentier").
* Avoir renseigné le coefficient de fatigue, les temps de ravitaillements, les barrières horaires, les noms des points importants.
* Avoir le calcul de vos temps de passage à jour (touche F5).
* Avoir marqué les positions importantes avec l'indicateur ![](./images/Tags/roadbook.png).
* Avoir marqué les points importants avec un ou plusieurs des indicateurs suivant: ![](./images/Tags/high_point.png) ![](./images/Tags/low_point.png) ![](./images/Tags/eat.png) ![](./images/Tags/drink.png) ![](./images/Tags/flag.png)


### Présentation

"Affichage>Mini roadbook" ou le bouton ![](./images/Toolbar/mrb.png) affiche la fenêtre suivante:  
![](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)

Elle contient les éléments suivants:

* En haut: la barre d'icônes permettant de réaliser des actions
* Au milieu à droite: Contient un tableau avec toutes les lignes du tableau général qui contenaient l'indicateur ![](./images/Tags/roadbook.png) et un ou plusieurs des indicateurs suivant: ![](./images/Tags/high_point.png) ![](./images/Tags/low_point.png) ![](./images/Tags/eat.png) ![](./images/Tags/drink.png) ![](./images/Tags/flag.png). Chaque ligne génère une étiquette dans le mini roadbook
* Au milieu à gauche: Cette zone permet de modifier le contenu de la ligne du tableau qui actuellement sélectionnée
* En bas: Contient le mini roadbook avec les étiquettes

Lorsqu'une ligne est sélectionnée alors l'étiquette correspondante change de couleur (couleur saumon).

### Les types de mini roadbook

Dans la barre du haut, la liste déroulante "Type de profil" permet de choisir parmi les 3 types de profil:

Le type "Simple":  
![](./images/MRB/CG40_MRB_Simple_Label.png)    
Le profil du parcours ne contient aucune information supplémentaire.

Le type "Route/sentier":  
![](./images/MRB/CG40_MRB_Road_Track_Label.png)  
Le profil met en évidence les portions de route et de sentier par un code de couleur.
Les routes sont les points du tableau général dont le coefficient de terrain est égal à 100%. Les autres sont considérés comme des sentiers.

Le type "Pente":  
![](./images/MRB/CG40_MRB_Slope_Label.png)  
Le profil met en évidence le degré de pente par un code de couleur.

Les couleurs utilisées dans les mini roadbook sont paramétrable dans la fenêtre de configuration accessible via le bouton ![](./images/MRB/Toolbar/settings.png).

### La barre d'icônes

![](./images/MRB/CG40_MRB_Toolbar.png)  


* ![](./images/MRB/Toolbar/save.png) : Permet de sauver le mini roadbook sous forme d'image. Le format disponible est PNG.
* ![](./images/MRB/Toolbar/settings.png) : Permet d'ouvrir la fenêtre de configuration du mini roadbook
* ![](./images/MRB/Toolbar/pipette.png) : Permet de copier la mise en forme d'une étiquette afin de la reproduire sur une ou plusieurs autres étiquettes. Cette fonction est aussi accessible avec le raccourci clavier CTRL+C
* ![](./images/MRB/Toolbar/replicate.png) :  Permet de coller la mise en forme sur l'étiquette sélectionnée. Seules les propriétés sélectionnées dans la fenêtre de configuration de la fonction seront collées. Cette fonction est aussi accessible avec le raccourci clavier CTRL+V
* ![](./images/MRB/Toolbar/replicate_config.png) : Ouvre une fenêtre permettant de configurer la fonction de duplication de la mise en forme. Cela ouvre la fenêtre suivante:  
![](./images/MRB/CG40_MRB_Replicate_Dlg.png)    
Sélectionnez les paramètres que vous voulez utiliser lors de la copie.

* ![](./images/MRB/Toolbar/label_to_bottom.png) : Permet de spécifier que les étiquettes doivent être connectées au bas du profil.
* ![](./images/MRB/Toolbar/label_to_profil.png) : Permet de spécifier que les étiquettes doivent être connectées au profil.
* ![](./images/MRB/Toolbar/night_day.png) : Permet de spécifier si on veut faire apparaître les zones de jour et de nuit sur le profil.
* "Type de profil" : Permet de sélectionner le type de mini roadbook.
    * Simple
    * Avec route/sentier
    * Avec pente
* ![](./images/MRB/Toolbar/favoris1.png) ![](./images/MRB/Toolbar/favoris2.png) ![](./images/MRB/Toolbar/favoris3.png) ![](./images/MRB/Toolbar/favoris4.png)
![](./images/MRB/Toolbar/favoris5.png) : Ces 5 boutons permettent de mémoriser des formats d'affichage.
    * Un clique droit sur un  bouton mémorise dans le bouton le réglage affiché dans le champ format.
    * Un clique gauche sur un bouton restitue dans le champ format, le format mémorisé dans le bouton.
* "Largeur" : Permet de sélectionner la largeur du profil (Valeur maximum 4000).
* "Hauteur" : Permet de sélectionner la hauteur du profil (Valeur maximum 2000).


### Définir la taille du mini roadbook

La taille maximale du mini roadbook est de 4000x2000 pixels (Largeur x Hauteur). C'est aussi la taille de l'image finale.

Le réglage de la taille est faite par les champs "Largeur" et "Hauteur" situé dans la barre d'icône.

### Configurer le mini roadbook

L'icône ![](./images/MRB/Toolbar/settings.png) permet d'ouvrir la fenêtre de configuration.

L'onglet "Général" permet de:  
![](./images/MRB/CG40_MRB_Settings1.png)

* Régler la valeur du filtre à appliquer sur les données lors de l'affichage du profil
* Régler la taille de zone haute du profil. Cette taille est en pixel.

L'onglet "Simple" permet de régler les couleurs de la représentation "Simple" du profil.  
![](./images/MRB/CG40_MRB_Settings2.png)  
Le bouton "Couleurs par défaut" permet de remplacer les couleurs courantes par les couleurs par défaut.

L'onglet "Route/Sentier" permet de régler les couleurs de la représentation "Route/Sentier" du profil.  
![](./images/MRB/CG40_MRB_Settings3.png)  
Le bouton "Couleurs par défaut" permet de remplacer les couleurs courantes par les couleurs par défaut.

L'onglet "Pente" permet de régler les couleurs de la représentation "Pente" du profil.  
![](./images/MRB/CG40_MRB_Settings4.png)  
Le bouton "Couleurs par défaut" permet de remplacer les couleurs courantes par les couleurs par défaut.

### Configurer chaque étiquette

Chaque ligne du tableau représente une étiquette. Elle peut être paramétrée par les éléments de réglages situés à gauche du tableau.  
![](./images/MRB/CG40_MRB_Editor.png)

* Sélection : Permet d'indiquer si l'on veut prendre en compte cette étiquette dans le profil. Si la case est décochée alors la ligne du tableau est grisée et l'étiquette n'est pas affichée dans le mini roadbook.
* Position : Permet de régler la position verticale de l'étiquette. Le chiffre indique le nombre de pixel par rapport à la position la plus haute de l'étiquette.
* Alignement : Permet de définir la position de l'étiquette par rapport au trait la reliant au profil. A gauche du trait, centré sur le trait ou à droite du trait.
* Format : Permet de spécifier le contenu de l'étiquette ([voir plus bas](#format)).
* Taille : Permet de définir la taille de la police de caractère utilisée dans les étiquettes.
* Étiquettes : Permet d'indiquer si l'on veut que les indicateurs soient affichés dans les étiquettes.
* Commentaire : Permet de saisir un commentaire spécifique à l'étiquette. Ce commentaire est différent du commentaire du tableau principal.

### Configurer le format d'affichage des étiquettes {#format}

Afin d'assurer une flexibilité optimale le format des étiquettes utilise des balises. Ces balises représentées par le signe "%" plus un ou plusieurs caractères, permettent de spécifier le type de donnée à afficher. Par exemple "%N" représente le nom du point. Au moment de l'affichage de l'étiquette les balises sont remplacées par leurs significations.

Les balises disponibles sont les suivantes:

* %N : Représente le nom du point.
* %A : Représente l'altitude du point. L'unité est le mètre.
* %D : Représente la distance depuis le départ. L'unité est le kilomètre.
* %T : Représente le temps de parcours depuis le départ. Le format est "hh:mm".
* %Tl : Représente le temps de parcours depuis le départ au format long "hh:mm:ss".
* %Ts : Représente le temps de parcours depuis le départ au format court "hh:mm".
* %H : Représente l'heure de passage au point. Le format est "ddd hh:mm" (ddd = Jour en abrégé).
* %h : Représente l'heure de passage au point. Le format est "hh:mm".
* %hl :Représente l'heure de passage au point au format long "hh:mm:ss".
* %hs :Représente l'heure de passage au point au format court "hh:mm".
* %B : Représente la barrière horaire au point (temps). Le format est "hh:mm".
* %b : Représente la barrière horaire au point (heure). Le format est "hh:mm".
* %C : Représente le commentaire spécifique à l'étiquette (Saisie dans le champ "Commentaire" de l'étiquette).
* %c : Représente le commentaire venant du tableau principal.
* %L : Insère un retour à la ligne.
* %R : Représente le temps de ravitaillement au point. Le format est "hh:mm".
* %Rl : Représente le temps de ravitaillement au point au format long "hh:mm:ss".
* %Rs : Représente le temps de ravitaillement au point au format court "hh:mm".
* %+ : Représente le cumul du D+ depuis le départ. L'unité est le mètre.
* %- : Représente le cumul du D- depuis le départ. L'unité est le mètre.

Le bouton "...", à coté du champ "Format", permet d'ouvrir une fenêtre permettant de réaliser une saisie simplifiée du contenu de l'étiquette sélectionnée.

![](./images/MRB/CG40_MRB_Label_Editor.png)


### Affichage des zones de jours et de nuits

Si dans les réglages du parcours, la prise en compte de la nuit a été activé alors le bouton ![](./images/MRB/Toolbar/night_day.png) sélectionner l'affichage des zones de jours et de nuits.

![](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)  
Les zones de nuits apparaissent grâce au fond grisé.

## Inverser le sens du parcours

**Course Generator** offre la possibilité d'inverser le sens du parcours. Afin de réaliser cette action il faut sélectionner "Outil>Inversion du parcours".
Après l'opération, il est nécessaire de redéfinir les coefficients de fatigues et les barrières horaires puis de relancer un calcul (bouton ![](./images/Toolbar/refresh_data.png) ou [F5]).

## Définir un nouveau point de départ sur un parcours en boucle

**Course Generator** offre la possibilité, si le parcours chargé en mémoire est une boucle, de définir un nouveau point départ.
La procédure est la suivante:

* Sélectionnez la position qui va devenir votre nouveau point départ.
* Sélectionnez "Outil>Définir un nouveau point départ".
* Une boite de dialogue suivante va apparaître:  
![](./images/CG40_Dlg_Confirm_New_Start.png)
* Confirmer la commande en cliquant sur "Oui".

Après l'opération il est nécessaire de redéfinir les coefficients de fatigues et les barrières horaires puis de relancer un calcul (bouton ![](./images/Toolbar/refresh_data.png) ou [F5]).

## Les paramètres généraux de Course Generator

Le menu "Paramètres>Paramètres de Course Generator" affiche la fenêtre suivante:  
![](./images/CG40_Settings.png)

Les réglages possibles sont les suivants:

* "Langues" : Permet de sélectionner la langue utilisée dans l'interface. "Système" utilise les réglages du système d'exploitation pour déterminer la langue à utiliser. Si la langue système n'est pas géré par **Course Generator** alors l'anglais est sélectionné.
* "Unité" : Permet de choisir entre "km/m" et "Miles/Pieds".
* "Format vitesse" : Permet de choisir le format de la vitesse (Vitesse ou pas).
* "Contrôle mise à jour au démarrage" : Permet de choisir si on veut contrôler la présence d'une mise à jour de l'application au démarrage.

## Importer et exporter les points marqués

Il est nécessaire dans certain cas de devoir sauvegarder uniquement les points marqués du parcours actuel dans un fichier. Cela permet si vous avez une nouvelle version du parcours de pouvoir importer ces points sur ce parcours.

Par exemple:  
Vous avez récupéré le parcours de l'UTMB. Vous avez passez du temps à repérer chaque col, chaque ravitaillement et vous avez saisie des commentaires sur certaines partie du parcours. Malheureusement, le parcours de l'année suivante est légèrement différent mais les points principaux sont les mêmes. La fonction d'import/export des points marqués va vous faire gagner beaucoup de temps.

La procédure est la suivante:

* Prendre le parcours de l'année précédente
* Exporter les points marqués avec "Fichier>Exporter des points"
* Sélectionnez les types de points à exporter dans la boite de dialogue suivante:    
![](./images/CG40_Export_Points.png)
* Validez votre sélection et saisissez le nom du fichier 'extension.CGP)'
* Ouvrez le parcours de l'année suivante
* Importez les importer les points avec "Fichier>Importer des points"
* Sélectionnez le fichier de point (extension .CGP) à importer.
* La boite de dialogue suivante apparaît:    
![](./images/CG40_Import_Points.png)
    * La colonne "Dist" indique la distance en mètre entre le point trouvé dans le parcours et le point à importer. La couleur de fond verte indique que le point trouvé est à moins de 100m, une couleur jaune indique que le point est entre 100m et 1000m et une couleur rouge indique que le point est à plus de 1000m. Si la distance est élevée cela indique que le nouveau parcours ne passe pas par ce point (au-delà de 100m il faut se poser des questions).  
    * La colonne "Ligne" indique la ligne du parcours où se trouve le point trouvé
    * La colonne "Sél." permet de sélectionner les points à importer. "X" indique que la ligne est sélectionné.
* Sélectionnez les points à importer et cliquez sur "Importer"

## Analyser les données après une course

**Course Generator** permet d'analyser les données après avoir réalisé le parcours. Il suffit d'ouvrir le fichier contenant les données GPS (souvent un fichier GPX). Vous trouvez alors dans le tableau toutes vos données. Les rapports vous permettent d'avoir des informations ces données.

Les données vont rester inchangées tant que vous ne demanderez pas un calcul du temps de parcours. Une fenêtre vous demandera alors si vous voulez écraser les données temporelles.

## Utiliser les fonctionnalités de la partie carte

**Course Generator** affiche le parcours sur une carte OpenStreetMap.  
![](./images/Map/CG40_Map_Area.png)

Sur la droite, une barre verticale de boutons permet de réaliser des actions sur cette carte.

* ![](./images/Map/marker.png) : Permet d'ajouter une marque de début d'action à l'endroit sélectionné
* ![](./images/Map/hide_marker.png) : Permet de supprimer la marque
* ![](./images/Map/undo.png) : Permet d'annuler la dernière opération
* ![](./images/Map/track_very_easy.png) : Indique que le terrain compris entre ![](./images/Map/marker.png) et le point courant est un "Terrain très facile"
* ![](./images/Map/track_easy.png) : Indique que le terrain compris entre ![](./images/Map/marker.png) et le point courant est un "Terrain facile"
* ![](./images/Map/track_average.png) : Indique que le terrain compris entre ![](./images/Map/marker.png) et le point courant est un "Terrain moyen"
* ![](./images/Map/track_hard.png) : Indique que le terrain compris entre ![](./images/Map/marker.png) et le point courant est un "Terrain difficile"
* ![](./images/Map/track_very_hard.png) : Indique que le terrain compris entre ![](./images/Map/marker.png) et le point courant est un "Terrain très difficile".
* ![](./images/Map/flag.png) : Permet d'ajouter une marque au point courant.
* ![](./images/Map/eat.png) : Permet d'ajouter un ravitaillement au point courant.
* ![](./images/Map/drink.png) : Permet d'ajouter un point d'eau au point courant.
* ![](./images/Map/select_map.png) : Permet de sélectionner le type de carte à utiliser.

Les commandes à la souris sont les suivantes:

* Un clic droit sur la carte position le marqueur de position au plus près.
* Un clic droit maintenu permet de faire bouger la carte.
* Un double clic sur la carte permet de zoomer sur le point sélectionné.


Pour changer la qualité du terrain pour une partie de parcours il faut:

* Positionner le curseur sur le début de la zone à changer
* Cliquer sur le bouton ![](./images/Map/marker.png) pour mettre le marqueur
* Positionner le curseur sur la fin de la zone à changer
* Cliquer sur le bouton correspondant à la qualité de terrain requise (par exemple ![](./images/Map/track_average.png))

Dans la barre d'état l'indicateur ![](./images/Statusbar/CG40_Statusbar_Map_Size.png) indique la taille disque utilisée par les cartes. Le menu "Outils>Afficher le répertoire contenant les fichiers courbes vitesse/pente" permet d'ouvrir le gestionnaire de fichier et d'afficher le contenu du répertoire contenant les courbes, les logs et le répertoire contenant les cartes. Le répertoire  "OpenStreetMapTileCache" contient les parties de carte. Si nécessaire vous pouvez effacer le contenu afin de gagner de la place.
