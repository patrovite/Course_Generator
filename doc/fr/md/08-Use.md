# Utilisation de Course Generator

La procédure normale d'utilisation de **Course Generator** est:

* Chargez un fichier GPS
* Paramétrez du parcours (Nom, date et heure de départ)
* Choisissez la courbe "Vitesse/pente"
* Paramétrez le parcours (qualité du terrain, coefficient de fatigue, ravitaillements...)
* Lancez le calcul
* Sauvez le parcours au format CGX ou GPX

Les sous-chapitres vont décrire l'ensemble de ces manipulations (et bien plus encore).

## Charger d'un parcours

Deux types de fichier peuvent être ouverts par **Course Generator**.

* Le format GPX contient une trace GPS issue d'un enregistrement fait avec un GPS, d'un logiciel de cartographie ou d'un site internet. Ce format ne contient pas les données spécifiques de **Course Generator**. Ce format est à utiliser si vous n'avez pas de fichier CGX de votre parcours.
* Le format CGX, qui est le format de **Course Generator**, stocke toutes les données spécifiques du logiciel. Il est à privilégier si vous voulez conserver votre travail.

Le chargement d'un parcours se fait par le menu "Fichier>Ouvrir un fichier GPX" ou "Fichier>Ouvrir un fichier CGX" en fonction du format choisi.

Si votre parcours contient une forte densité de point GPS, le logiciel vous demandera si vous voulez appliquer un filtre afin de réduire le nombre de point du parcours. Une trop forte densité de points peut pertuber le calcul des distances.

Si vous chargez un fichier GPX qui n'a pas de donnée temporelle alors **Course Generator** mettra les temps de passage pour chaque à 0. Il sera nécessaire de cliquer sur le bouton ![refresh button](./images/Toolbar/refresh.png) pour lancer le calcul temps pour chaque point du parcours.

## Paramétrer un parcours

Le paramétrage du parcours est réalisé avec "Paramètres>Paramètres du parcours" ou [F9].  

La fenêtre de configuration ci-dessous est alors affichée.  
![](./images/CG40_Track_Param.png)

Vous pouvez alors:

* Renseigner le nom de la course (maximum 15 caractères).
* Ajouter un descriptif du parcours. Ce descriptif apparaîtra dans le roadbook.
* Définir la date et l'heure de départ.
* Sélectionner la prise en compte de l'altitude.
* Sélectionner la prise en compte de la nuit. Il faut alors renseigner l'heure de début et de fin de nuit ainsi que le coefficient de correction à apporter à chaque position. Le bouton "Détermination automatique" permet d'ouvrir la boite de dialogue pour calculer automatiquement l'heure de couchée et de levée du soleil. Le calcul est fait à partir des informations du premier point GPS du tableau (coordonnées GPS, date et heure).

La fenêtre suivante s'affiche:  
![](./images/CG40_Track_Param_Sun_Dlg.png)

Saisissez le fuseau horaire (1 pour la France). L'heure de levée et de couché du soleil sont alors affichées.

Une fois le réglage terminé, appuyez sur "Valider" afin de valider votre saisie.    
Si vous avez sélectionné la prise en compte de la nuit, vous pourrez constater que la colonne "Heure" à un fond bleu durant les périodes de nuit et vert durant les périodes de jour.

## Paramétrer la courbe de vitesse/pente

Afin d'avoir une durée de parcours cohérente avec votre niveau, il faut choisir ou créer une courbe de vitesse par rapport à la pente. Cette courbe va de -50% de pente (descente) à +50% de pente (montée). Le choix de la courbe se fait par le choix de la vitesse que l'on veut "tenir" lorsque la pente est nulle (0%) sur un terrain goudronné. Un ensemble de courbes ont été créée afin de couvrir la plupart des utilisations en course à pied.

Le menu "Paramètres>Courbes Vitesse/Pente..." ou le bouton ![](./images/Toolbar/chart_curve.png) permettent d'afficher la boite de dialogue permettant de sélectionner et gérer les courbes.  
![](./images/Curve/CG40_Dlg_Curves.png)

Sur la gauche apparaît la liste des courbes déjà créée. Lors de la création, un nom explicite leur a été donné afin de les retrouver rapidement. Essayez de conserver ce principe si vous créez de nouvelles courbes.

Le bouton ![](./images/Curve/chart_curve_open.png) permet de charger les données du fichier courbe sélectionné dans la liste.  
Le bouton ![](./images/Curve/chart_curve_edit.png) permet de modifier les données de la courbe sélectionnée.  
Le bouton ![](./images/Curve/chart_curve_add.png) permet de créer une nouvelle courbe.  
Le bouton ![](./images/Curve/chart_curve_duplicate.png) permet de dupliquer la courbe sélectionnée.  
Le bouton ![](./images/Curve/chart_curve_delete.png) permet de supprimer la courbe sélectionnée.  

> **Comment choisir sa courbe de vitesse?**  
> Cela va dépendre de vous, de vos objectifs... Vous pouvez vous baser sur un pourcentage de votre VMA.
> Par exemple pour un trail long vous pouvez prendre 60% de votre VMA. Environ 10km/h dans mon cas, je sélectionne alors la courbe de 10km/h.
> Vous pouvez aussi utiliser Softrun (www.softrun.fr) afin d'évaluer votre niveau et donc de pouvoir choisir votre courbe.  
> Il y a aussi l'expérience. Au début vous allez sûrement sous-évaluer ou sur-évaluer votre vitesse. Mais avec le temps votre choix va devenir de plus en plus juste.

Notes:

* Si vous créez de nouvelles courbes qui vous semblent intéressantes, n'hésitez pas à me les envoyer afin que je puisse les rajouter sur le site internet ainsi que dans les prochaines versions.
* Chaque courbe est un fichier dont l'extension est '.par'.  Ces fichiers sont accessible par le menu  "Outils>Afficher le répertoire contenant les fichiers courbes vitesse/pente". Cela va ouvrir le gestionnaire de fichier et afficher le contenu du répertoire.

## Paramétrer la difficulté du terrain

La colonne "Diff" permet de 'quantifier' la difficulté du terrain.

Vous pouvez saisir rapidement la difficulté du terrain pour un ensemble de point. Il vous suffit de sélectionner la première ligne puis tout en maintenant la touche SHIFT enfoncé vous sélectionnez les lignes suivantes (à la souris ou au clavier). Le bouton ![](./images/Toolbar/fill_diff.png) permet d'afficher la boite de dialogue de remplissage automatique.

![](./images/CG40_Dlg_Fill_Diff.png)

La zone "Début" permet de définir la ligne de départ (depuis le début ou depuis un numéro de ligne précis).  
La zone "Fin" permet de définir la ligne de fin (jusqu'à la fin ou jusqu'à un numéro de ligne précis).  
La zone "difficulté" permet de choisir la difficulté du terrain. Vous pouvez soit utiliser les valeurs pré-déterminées, soit saisir votre propre valeur de difficulté.

Cette action peut aussi être faite sur la carte du parcours (voir plus bas).

**Note:**  
Il est parfois impossible de déterminer la qualité du terrain à cause de la méconnaissance de celui-ci. Il peut alors être judicieux de fixer une qualité moyenne du terrain pour l'ensemble du parcours. Par exemple, la Montagn'hard 100 a été qualifiée en terrain moyen sur l'ensemble du parcours. Même si certains passages étaient très difficile (pierriers, zones glissantes...) et d'autres très roulants (routes ou pistes).  
Il ne faut pas prendre en compte lors de votre choix la déclinivité du terrain car cela est pris en compte via la courbe "Vitesse/Pente" et le calcul de la pente fait automatiquement par **Course Generator**.

## Paramétrer le coefficient de fatigue
La colonne "Coeff" permet de 'quantifier' la fatigue dans le temps.

Vous pouvez saisir rapidement le coefficient de fatigue pour un ensemble de point (plus généralement pour l'ensemble du parcours). Il vous suffit de sélectionner la première ligne puis tout en maintenant la touche SHIFT enfoncé vous sélectionnez les lignes suivantes (à la souris ou au clavier). Le bouton ![](./images/Toolbar/fill_coeff.png) permet d'afficher la boite de dialogue de remplissage automatique.

![](./images/CG40_Dlg_Fill_Coeff.png)

La zone "début" permet de sélectionner le début de la zone de prise en compte du coefficient. Le champ "Valeur initiale" permet de saisir la valeur correspondante.

La zone "Fin" permet de sélectionner la fin de la zone de prise en compte du coefficient. Le champ "Valeur finale" permet de saisir la valeur correspondante.

Si la valeur de fin n'est pas égale à la valeur de début alors on aura, pour les lignes intermédiaires, une variation progressive et linéaire de la valeur.  
Les éditions manuelles faites par l'intermédiaire de l'éditeur de ligne seront écrasées.  

Les réglages fait dans cette fenêtre seront globaux et mémorisés dans le fichier CGX lors de la sauvegarde.

La zone "Aide" permet en fonction du temps que vous estimez réaliser de vous donner une valeur approximative du coefficient de fatigue. Cette valeur peut être recopiée dans les zones "> Début" et "> Fin" via les boutons correspondant.


## Saisir les temps de ravitaillement

Afin de coller au mieux à la réalité, vous pouvez saisir pour un point donné le temps que vous prévoyez pour votre ravitaillement (ou votre repos). Pour cela mettez-vous sur la cellule concernée et ouvrez l'éditeur de ligne en faisant un double clique.

La boite dialogue suivante s'affiche:  
![](./images/CG40_Line_Editor.png)

Les champs "Temps de ravitaillement" permettent de saisir le temps d'arrêt à cette emplacement (heures, minutes et secondes). Le bouton "0" permet de remettre le temps à 00h00mm00s.


> **Question fréquente!**  
> Le temps ou l'heure affiché sur la ligne contenant un temps de ravitaillement est le temps ou l'heure à laquelle vous prévoyez de quitter cette position.  
> C'est le mode fonctionnement de **Course Generator** qui impose cette méthode de calcul.
>  
> Pour résumer:  
> [Heure] = [Heure de la position précédente] + [Temps de parcours entre le 2 positions] + [Temps de ravitaillement]  
>  
> [Temps] = [Temps de la position précédente] + [Temps de parcours entre le 2 positions] + [Temps de ravitaillement]  


## Saisir les temps de récupération

Vous pouvez saisir pour un point donné le coefficient de récupération suite à un ravitaillement ou à un repos. Pour cela mettez-vous sur la cellule concernée et ouvrez l'éditeur de ligne en faisant un double clique.

La boite dialogue suivante s'affiche:  
![](./images/CG40_Line_Editor.png)

Le champ "Récupération" permet de saisir le coefficient de récupération (entre 0 et 100). Cette valeur est relative. C'est-à-dire que si vous pensez récupérer 5% de coefficient de fatigue, il faut saisir 5 et non la valeur que vous pensez avoir (par exemple passer de 85% à 90%).

Note :  
Après avoir modifié la colonne "Récup.", il est nécessaire de relancer un calcul global avec le bouton ![](./images/Toolbar/fill_coeff.png) afin que votre saisie soit prise en compte.

## Renseigner les barrières horaires

Vous pouvez saisir pour un point donné la barrière horaire prévue. Cette barrière horaire est exprimée en temps depuis le départ et non en heure de passage. Cela permet de prendre en compte les décalages de départ (par exemple l'UTMB 2011 avec 5h de retard). Pour cela mettez-vous sur la cellule concernée et ouvrez l'éditeur de ligne en faisant un double clique.

La boite dialogue suivante s'affiche:  
![](./images/CG40_Line_Editor.png)

Les champs "Barrière horaire" permettent de saisir la barrière horaire (heures, minutes et secondes). Le bouton "0" permet de remettre le temps à 00h00mm00s.

Afin de prendre en compte la saisie il est nécessaire de lancer un calcul avec le bouton  ![](./images/Toolbar/refresh.png). Après l'exécution du calcul si un des temps de passage dépasse une barrière horaire alors un indicateur rouge "Barrière horaire" apparaîtra dans la barre inférieure. Un clique sur l'indicateur sélectionnera la première ligne du parcours ayant un dépassement de temps.

## Les indicateurs ou tags

Pour chaque point vous pouvez avoir des indicateurs ou tags qui vous indique une particularité du point.

Les différents indicateurs sont les suivants:

* ![](./images/Tags/high_point.png) : Indique un point haut. Cet indicateur sélectionné manuellement ou automatiquement par la fonction "Détermination des mini/maxi".
* ![](./images/Tags/low_point.png) : Indique un point bas. Cet indicateur sélectionné manuellement ou automatiquement par la fonction "Détermination des mini/maxi".
* ![](./images/Tags/eat.png) : Indique un point de ravitaillement (solide et liquide)
* ![](./images/Tags/drink.png) : Indique un point d'eau
* ![](./images/Tags/photo.png) : Indique un point remarquable
* ![](./images/Tags/flag.png) : Indique une étape. Cet indicateur est appelé "Marque" et permet de découper le parcours en étape. Chaque marque ajoute une ligne dans le tableau résumé.
* ![](./images/Tags/note.png) : Indique une note.
* ![](./images/Tags/info.png) : Indique une information
* ![](./images/Tags/roadbook.png) : Indique le début d'une nouvelle étape du roadbook
* ![](./images/Tags/dropbag.png) : Indique la présence d'un sac d'allègement.
* ![](./images/Tags/crew.png) : Indique la présence d'une assistance
* ![](./images/Tags/first_aid.png) : Indique un point de secours
  
Pour sélectionner les indicateurs d'une position il faut sélectionner la cellule concernée et ouvrir l'éditeur de ligne en faisant un double clique.

La boite dialogue suivante s'affiche:  
![](./images/CG40_Line_Editor.png)

Les indicateurs apparaissent en face de "Marques".

Afin de gagner du temps des raccourcis clavier sont disponibles:

* [F6] permet de mettre ou d'enlever une "Marque" sur la ligne sélectionnée.
* [F7] permet de se déplacer rapidement vers la prochaine ligne contenant un indicateur.
* [Ctrl+F7] permet de se déplacer rapidement vers la précédente ligne contenant un indicateur.

## Calculer le temps de parcours

Une fois les paramètres du parcours saisis, il est nécessaire d'appuyer sur le bouton ![](./images/Toolbar/refresh.png) afin de lancer le calcul du temps de passage pour chaque point.
Les colonnes 'Temps' et 'Heure' sont alors mises à jour en fonction des réglages que vous avez réalisés précédemment.
Dans la barre d'état, située en bas de la fenêtre, le temps total est mis à jour.

## Sauver le parcours
**Course Generator** offre la possibilité de sauver votre parcours dans plusieurs formats.

* "Fichier>Sauver GPX" sauve le parcours au format GPX qui est le format standard d'échange de parcours. Le problème de ce format est qu'il ne stocke pas les données spécifiques à **Course Generator**.
* "Fichier>Sauver CGX" sauve le parcours au format CGX qui est le format des fichiers **Course Generator**. Ce format devra être utilisé dès que l'on voudra conserver les paramétrages réalisés sur un parcours.
* "Fichier>Sauver CSV" sauve le parcours au format CSV qui est un format standard permettant de sauver des données sous forme de texte séparé par des points virgules. Ces fichiers peuvent être ouverts par un tableur comme EXCEL, OpenOffice Calc ou Libre Office Calc.
