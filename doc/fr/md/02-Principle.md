# Le principe

Pour fonctionner **Course Generator** a besoin, en entrée, de données contenant une suite de point GPS représentant un parcours.

Ces données peuvent être :

* Un fichier au format GPX. Ce fichier contient les données d'un parcours qui a été créé ou récupéré sur des sites interent (par exemple: Openrunner, Skitour...),
* Un fichier au format CGX qui est le format d'enregistrement de **Course Generator**.

## Un fichier GPX c’est quoi ?

Le format GPX est un format standard d’échange de donnée cartographique créé par GARMIN. Ces données sont soit issue d’un GPS soit issue d’un logiciel ou d’un site internet.

Il permet d’échanger :

* Des points de passage (waypoints en Anglais). Ce sont des points GPS, contenant la latitude, la longitude et l'altitude, auquel on associe des informations comme un nom ou un symbole. Le nombre de waypoint est en général limité sur les GPS (souvent 500 waypoints sur les GPS GARMIN).
* Des routes. Elles sont constituées d’un ensemble de waypoint. Elles sont donc limitées par le nombre de waypoint que peut contenir un GPS.
* Des traces (tracks en Anglais). Une trace est constituée d’un ensemble de point GPS (et non de waypoint). Chaque point GPS contient au moins la latitude et la longitude du point. L’altitude et l’heure d’enregistrement sont en générale inclus dans chaque point.

![Image Wikipedia](./images/CG40_GPX.png)  
Source Wikipédia

Lors de l’ouverture d’un fichier GPX, **Course Generator** n’exploite que les traces. Les autres constituants sont ignorés.

## Le format CGX

Le format CGX est le format de sauvegarde propre à **Course Generator**. Il permet en plus de la latitude, de la longitude et de l'altitude, de stocker l’ensemble des données spécifiques à **Course Generator**. Cela comprend par exemple : la difficulté du terrain, les points de ravitaillement, les commentaires, les données du mini-roadbook...
Ce format permet d'échanger un parcours avec un ensemble complet de renseignements sur celui-ci.  

## Le principe de fonctionnement de Course Generator

Le diagramme ci-dessous montre le principe de fonctionnement de **Course Generator**.

![Principe de fonctionnement](./images/CG40_Principe.png)

## Les cas d’utilisations

Les cas d’utilisation suivant pourraient être envisagés (liste non exhaustive) :

* Préparer une course. Après avoir téléchargé le parcours vous allez ajuster vos paramètres, ajuster la "qualité" du terrain, indiqué les lieux de ravitaillements et les temps d'arrêts prévu, ajouter des commentaires et bien d'autres choses. Au final, **Course Generator** va calculer votre temps de passage pour chaque point. Cela vous permettra d'avoir vos temps de passage, des statistiques (par exemple temps passé à plus de 2000m de nuit) et de générer un mini-roadbook.  
* La diffusion par l'organisateur d'une course d'un parcours dans lequel il aurait indiqué pour la « qualité » du terrain, les ravitaillements, les cols ainsi que les barrières horaires.
* La génération d’un fichier GPX avec les données temporelles pré-calculées afin d’utiliser le partenaire virtuel des GPS GARMIN. Cela permet d'avoir un partenaire virtuel qui court avec vous. Si vous avez choisi les bons paramètres, il vous sera possible de courir à ses côtés. Cette fonction permet aussi d'afficher votre position et celle partenaire sur le parcours et sur le profil du parcours. Elle vous donne le temps de parcours restant ainsi que le kilométrage restant. C'est très pratique pour gérer votre effort. La capture d’écran ci-dessous vous montre l’affichage du profil en mode partenaire virtuel sur un Forerunner 205/305. Le point foncé c'est vous et le point clair c'est le partenaire virtuel.

![](./images/CG40_Virtual_Partner.jpg)


> __Faut-il obligatoirement avoir GPS Garmin pour utiliser **Course Generator**?__
>
> Non! Mais c'est un plus si vous voulez utiliser la fonctionnalité partenaire virtuel. C'est ce qui m'a poussé à créer **Course Generator** (même si maintenant je n'utilise presque plus cette fonctionnalité).
