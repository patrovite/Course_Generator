# Les onglets

La zone haute contient une série d'onglet qui permet de choisir le type de donnée à afficher.

## L'onglet "Données GPS"
Cet onglet affiche toutes les données du parcours.  
![](./images/Tabs/CG40_Tab_Data.png)

Un double clique sur une des ligne ouvre la fenêtre d'édition.

Le détail des colonnes est décrit ci-dessous:

* **N°** : C'est le numéro de la ligne du tableau. Il permet de se repérer rapidement dans le tableau.
* **Lat** : Contient la latitude du point en degré.
* **Lon** : Contient la longitude du point en degré.
* **Altitude** : Contient l'altitude du point en mètre.
    * A gauche de l'altitude une flèche indique l'inclinaison du terrain.
        * Vers le haut, cela indique que l'on a monté depuis le dernier point.
        * Vers le bas, cela indique que l'on a descendu depuis le dernier point.
        * Vers la droite, cela indique que le terrain en plat.
    * La couleur de fond indique le degré de pente du terrain.
        * Marron si on monte (pente positif). Plus la pente est élevée et plus la couleur est sombre.
        * Blanc si on est sur du plat.
        * Vert si on descend (pente négative). Plus la pente est élevée et plus la couleur est sombre.
* **Tag** : Affiche les marques associées au point.
    * ![](./images/Tags/high_point.png) : Indique un point haut.
    * ![](./images/Tags/low_point.png) : Indique un point bas.
    * ![](./images/Tags/eat.png) : Indique un point de ravitaillement.
    * ![](./images/Tags/drink.png) : Indique un point d'eau.
    * ![](./images/Tags/flag.png) : Indique que le point a été marqué.
    * ![](./images/Tags/photo.png) : Indique un lieu remarquable.
    * ![](./images/Tags/note.png) : Indique une note.
    * ![](./images/Tags/info.png) : Indique une information.
    * ![](./images/Tags/roadbook.png) : Indique le début ou la fin d'une section de roadbook.
* **Dist** : Contient la distance, en mètre/miles, avec le point précédent.
* **Total** : Contient la distance, en kilomètre/miles, qui a été parcouru jusqu'à ce point.
* **Diff** : Contient la difficulté du terrain entre le point précédent et ce point. La valeur initiale est de 100, cela correspond à une route plate goudronnée. Plus la valeur diminue, plus le terrain est accidenté. Si vous mettez 80, cela indique que par rapport à une route goudronnée plate vous allez mettre 20% de temps en plus pour parcourir la distance. Le bouton ![](./images/Toolbar/fill_diff.png) permet de remplir rapidement un ensemble de position. Vous trouverez ci-dessous les valeurs utilisés par Softrun (www.softrun.fr). Merci à Rémi Poisvert pour ces informations.
    * "Terrain facile" = 98
    * "Terrain moyen" (chemin normal de montagne) = 95
    * "Terrain difficile" = 88
    * "Terrain très difficile" = 80
    * "Terrains extrêmement difficiles" = en-dessous de 80
* **Coeff** : Contient le coefficient de fatigue à appliquer entre le point précédent et ce point. La valeur est comprise entre 1 et 200. La valeur initiale est de 100, cela correspond à votre état de départ (en principe en bonne forme). Si vous mettez 80, cela indique que vous allez mettre 20% de temps en plus pour parcourir la distance.
Le bouton ![](./images/Toolbar/fill_coeff.png) permet de définir une règle d'évolution globale du coefficient de fatigue.
* **Récup.** : Contient le coefficient de récupération qui sera ajouté au coefficient de fatigue. C'est une valeur relative qui est comprise entre 0 et 100. La valeur initiale est de 0 (qui n'est pas affichée). La somme "Coeff"+"Récup" est plafonnée à 100%.
Ce paramètre est utilisé pour indiquer la récupération suite à une pause (ravitaillement, sieste dans une base vie...). Une fois la valeur saisie, il faut relancer un calcul global du coefficient de fatigue avec le bouton ![](./images/Toolbar/fill_coeff.png).
* **Temps** : Contient le temps qu'il vous faut pour atteindre ce point depuis le départ.
* **B.H.** : Contient la barrière horaire à ce point du parcours. C'est le temps depuis le départ et non l'heure de passage (cela permet d'éviter les problèmes liés au décalage des heures de départ). Si à un point du parcours le temps est supérieur à la barrière horaire alors un indicateur apparaît dans la barre de d'information située en bas de la fenêtre principale. Un clique sur l'indicateur positionne le tableau sur la première barrière horaire qui a été dépassée.
* **Heure** : Contient le jour et l'heure de passage à ce point. La date et l'heure de départ sont réglables dans les paramètres de course.
Si le fond est vert cela indique que le trajet se fait de jour. Si le fond est bleu alors le trajet se fait de nuit.
* **Ravito** : Contient le temps de ravitaillement que vous prévoyez à ce point.
Si aucun ravitaillement n'est prévu à ce point (temps égale à 00:00.00) alors la cellule est vide.
* **Nom** : Contient le nom du point. Il est utilisé dans l'affichage du profil, les rapports et le mini roadbook.
* **Commentaire** : Contient un commentaire sur le point. Il est utilisé dans les rapports et le mini roadbook.

## L'onglet "Profil"
Cet onglet affiche le profil du parcours.  
![](./images/Tabs/CG40_Tab_Profil.png)

Si une marque a été positionnée dans le tableau alors un point apparaît sur la courbe ainsi que son numéro d'ordre. Ce numéro correspond à la ligne du tableau de l'onglet Résumé.

Un clique gauche sur la courbe permet de positionner un curseur et d'obtenir des informations sur le point (issue du tableau).

Un clique droit sur la courbe permet d'afficher un menu permettant de régler l'affichage de la courbe et de la sauvegarder sous forme d'image (format PNG).

Le bouton ![](./images/Tabs/profil_marker.png) permet d'afficher la position des lignes du tableau "Résumé".


## L'onglet "Statistiques"
Cet onglet permet d'obtenir des statistiques sur le parcours.  
![](./images/Tabs/CG40_Tab_Stat.png)

Les statistiques concernent entre autre:

* La vitesse moyenne, la distance et le temps pour diverses zones de pente
* La vitesse moyenne, la distance et le temps pour diverses zones d'altitude
* La vitesse moyenne, la distance et le temps pour les périodes de jour
* La vitesse moyenne, la distance et le temps pour les périodes de nuit
* Le pourcentage moyen de pente à la montée et la descente
* La distance parcourue en montée, sur le plat et en descente
* La différence de température entre le point bas du parcours et le point haut. Cette valeur est purement indicative et théorique. Elle ne prend pas en compte la température ressentie (du au vent) et les phénomènes locaux. La base de calcul est 0.6°C par 100m de dénivelé.

Le bouton ![](./images/Tabs/save_html.png) permet de sauver ces données au format HTML.  
Le bouton ![](./images/Tabs/refresh.png) permet de rafraîchir les données.

## L'onglet "Analyse"
Cet onglet permet d'obtenir une analyse de votre parcours. Il contient 3 sous-onglets qui sont détaillés ci-dessous.

### L'onglet "Analyse > Temps/Distance"

Cet onglet permet de faire une analyse de votre vitesse dans le temps.
![](./images/Tabs/CG40_Tab_Analyze_Dist_Time.png)

Deux courbes sont présentes:

* Une courbe d'altitude par rapport à la distance
* Une courbe de temps (en seconde) par rapport à la distance

L'étude de la courbe de temps/distance permet de voir son évolution sur le parcours (accélération ralentissement, arrêté).
Le changement de pente de la courbe rouge indique une variation de vitesse.

Les cas suivants sont possibles:

* La pente de la courbe devient plus raide. Cela indique un ralentissement (due au terrain ou à la fatigue).
* La pente de la courbe devient moins raide. Cela indique une accélération.
* La pente change brusquement. Cela indique un arrêt.

### L'onglet "Analyse > Vitesse"
Cet onglet permet d'obtenir de faire une analyse de la vitesse sur votre parcours.  
![](./images/Tabs/CG40_Tab_Analyze_Speed.png)

Deux courbes sont présentes:

* Une courbe de vitesse par rapport à la distance
* Une courbe de régression de la vitesse


### L'onglet "Analyse > Vitesse/Pente"
Cet onglet permet d'extrapoler une courbe vitesse par rapport à la pente.  
![](./images/Tabs/CG40_Tab_Analyze_Speed_Slope.png)

Cette courbe est utile pour créer ses propres courbes vitesse/pente en fonction d'un parcours réalisé.
Deux courbes sont présentes:

* Un nuage de points qui regroupe tous les points acquis lors de votre parcours
* Une courbe vitesse/pente (extrapolé)


Le bouton ![](./images/Tabs/save_curve.png) permet de sauvegarder la courbe résultat (courbe rouge) dans la bibliothèque des courbes vitesse/pente. Elle sera exploitable dans la fenêtre courbe.  

La boite de dialogue suivante apparaît:  
![](./images/Tabs/CG40_Tab_Analyze_Speed_Slope_Save.png)  
Le champ de saisie "Nom" permet de saisir le nom de la courbe.  
Le champ de saisie "Commentaire" permet de saisir un commentaire.

Le bouton ![](./images/Tabs/correction.png) permet de corriger la courbe vitesse/pente avec les paramètres du parcours (difficulté du terrain et fatigue).  
Le bouton ![](./images/Tabs/speed.png) permet de filtrer la vitesse de la courbe vitesse/pente.


### L'onglet "Résumé"
Cet onglet affiche un tableau regroupant toutes les lignes du parcours contenant une marque.  
![](./images/Tabs/CG40_Tab_Resume.png)

Pour chaque ligne vous avez:

* Un numéro.
* Le nom du point marqué.
* La ligne du tableau où se trouve le point marqué.
* L'altitude du point marqué.
* Le dénivelé positif réalisé jusqu'à ce point.
* Le dénivelé négatif réalisé jusqu'à ce point.
* La distance parcourue jusqu'au point marqué.
* Le temps de passage à ce point.
* L'heure de passage à ce point.
* Le temps de parcours depuis le dernier point.
* La barrière horaire exprimée en temps depuis le départ.
* Le temps de ravitaillement.
* La distance depuis le dernier point.
* Le dénivelé positif depuis le dernier point.
* Le dénivelé négatif depuis le dernier point.
* La vitesse de montée depuis le dernier point.
* La vitesse de descente depuis le dernier point.
* La pente moyenne des montées depuis le dernier point.
* La pente moyenne des descentes depuis le dernier point.
* La vitesse moyenne depuis le dernier point.
* Le commentaire sur le point marqué.

Le bouton ![](./images/Tabs/save_csv.png) permet de sauvegarder les données du tableau au format CSV afin de les exploiter dans un tableur (Excel, OpenOffice Calc...).  
Le bouton ![](./images/Tabs/refresh.png) permet de rafraîchir les données.  
