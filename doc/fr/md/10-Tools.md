#  Les outils utiles

##  La correction des altitudes

Les mesures d'altitudes faites par un GPS sont basées, comme pour la position, sur une triangulation. Malheureusement cette triangulation est moins précise que pour la position. Sur de longue distance ces erreurs ne sont pas négligeables et peuvent provoquer des erreurs de calcul sur la distance parcouru et sur le dénivelé total.

Il est alors nécessaire de réaliser une opération de calage des altitudes. Cette opération consiste à remplacer, pour chaque point GPS, l'altitude mesurée par la vraie altitude.

Il y a plusieurs outils qui font cette opération. Parmi ceux-là, il y a GPSVisualizer qui à partir de votre fichier au format GPX corrige les altitudes et vous génére, en sortie, un fichier avec les valeurs corrigées.

L'outil se trouve à l'adresse web suivante http://www.gpsvisualizer.com/elevation


> Note:  
> **Course Generator** ne contient pas d'algorithme élaboré permettant de filtrer les altitudes (comme  dans SportTracks). Le seul filtre présent concerne le cumul des altitudes où le cumul ne fonctionne que s'il y a une variation d'altitude supérieure à un seuil déterminé (10m). Cela permet de masquer les petites aspérités du terrain comme un rocher ou un tronc d'arbre. Les GPS étant de plus en plus précis, ces aspérités sont prises en compte dans les calculs et viennent les fausser.

## La suppression des points inutiles

Certain parcours, surtout quand ils ont été enregistré sur le terrain, contiennent plusieurs milliers de points. Cela peut poser des problèmes avec certains logiciels, sites internet ou GPS.

Le site GPSVisualizer permet de réduire de manière intelligente le nombre de points sans perte "d'informations" sur le parcours.

L'outil se trouve à l'adresse web suivante http://www.gpsvisualizer.com/convert_input
