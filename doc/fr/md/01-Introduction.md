# Introduction

**Course Generator** permet de traiter vos fichiers GPS afin de :

* Calculer votre temps de parcours en ayant, au préalable, ajusté les paramètres correspondant à la nature du terrain et à vos capacités,
* Calculer vos temps de passage à chaque point du parcours,
* D'ajouter sur le parcours la nature du terrain, votre coefficient de fatigue dans le temps, les temps de ravitaillements ou de repos et des commentaires,
* De définir des barrières horaires (un indicateur vous indiquera un dépassement),
* De définir des coefficients de récupération,
* De définir les périodes de nuits ainsi que la baisse d'efficacité associée,
* De sélectionner la prise en compte de la baisse de performance en fonction de l'altitude,
* De visualiser votre parcours sur une carte OpenStreetMap,
* De générer un mini-roadbook avec le profil de votre parcours et des annotations sur les points de passage (nom, temps, heure, altitude, D+…),
* De générer un rapport au format texte (CSV),
* D'obtenir des statistiques sur le parcours avec sauvegarde des données au format HTML,
* D'inverser le sens du parcours,
* De définir un nouveau point de départ pour un circuit en boucle,
* D’insérer un parcours au début ou à la fin d’un autre parcours,
* D'extraire une partie du parcours,
* Déterminer à partir d'un cible la courbe vitesse/pente correspondante,
* De sauvegarder le tracé modifié, contenant les temps de parcours calculés, au format GPX. Cela vous permettra, par exemple, d'utiliser la fonction "Partenaire virtuel" des GPS GARMIN,
* De sauvegarder le tracé au format CGX qui est le format de sauvegarde de **Course Generator** afin de pouvoir échange des tracés tout en conservant les données sur le terrain, les commentaires...

Et beaucoup d'autres choses...


Des conventions d'écritures ont été adoptées dans ce manuel. Elles sont détaillées ci-dessous.

* *"Fichier>Chargement fichier GPX"* indique qu'il faut sélectionner le menu "Fichier" puis sélectionner, dans ce menu, "Chargement fichier GPX". Cela permet de décrire simplement une suite de manipulation à faire à la souris.
* *"[CTRL+O]"* indique une série de touches à actionner afin de déclencher une action. Par exemple [CTRL+O] indique qu'il faut appuyer sur la touche CTRL et tout en la maintenant actionnée, il faut appuyer sur la touche O.

## Mot de l'auteur

J'ai créé **Course Generator** en 2008 afin de m'aider à préparer mon premier 100km en Ultra-trail. Je l'ai complété au fur et à mesure de mes besoins. Un petit article dans Ultrafondu m'a permis de commencer à le faire connaître et je l'ai ensuite fait grandir avec le retour des utilisateurs.
Il a énormément évolué et plusieurs fois, je l'ai réécris afin de l'adapter à mes besoins. A chaque fois cela été un défi et comme les courses que j'ai couru, cela été une aventure qui m'a fait grandir (connaissances, remise en cause, ouverture d'esprit...).

**Course Generator** n'a pas la prétention de vous donner des résultats totalement justes. Trop de choses dépendent de vous et de conditions extérieures. Considérez ce logiciel comme une aide vous permettant de préparer vos prochaines aventures.

Vous remarquerez que je n'emploie pas le terme "course" car pour moi l'approche "aventure" d'une course, aussi dure soi-elle, m'a toujours permis d'aller au bout grâce à son approche positive (sans oublier le chrono quand même:) ).

Le développement de **Course Generator** a été une aventure et continue à l'être.

Si appréciez ce logiciel vous pouvez contribuer de différentes façons:

* Par un don, en allant sur le site de **Course Generator**. Cela me permet de payer l'hébergement du site, des outils et des livres me permettant de continuer l'aventure. Le développement de l'application est fait sur mon temps libre.  
* Par de la publicité. Par choix, je suis peu actif sur les forums et réseaux sociaux afin d'utiliser mon temps libre afin de faire grandir le logiciel. Si vous avez la possibilité, n'hésitez pas à parler de **Course Generator**. Twitter, Facebook, forums et aussi Reddit qui s'il est peu utilisé en France est un outil très utilisé dans les pays anglophones.
* Par des retours sur le logiciel. Bugs, corrections de la documentation et demandes d'améliorations sont les bienvenus.
* Par la participation à la traduction du logiciel dans une autre langue. C'est simple, je vous envoie un fichier texte avec les textes en anglais et vous les traduisez dans la langue cible en suivant quelques règles simples.
* Par la participation au développement du logiciel. Rien de très complexe, il faut connaître le langage Java, Git et Github. Depuis la version 4, j'ai mis **Course Generator** en Open source sur Github (github.com/patrovite/Course_Generator) afin que d'autres personnes puissent faire grandir le logiciel avec moi. Le sujet est vaste, il y a encore plein de chose à faire.


Partez à l'aventure avec **Course Generator**.

Pierre DELORE

## Protection des données personnelles

Le logiciel collecte dans les logs des informations sur votre configuration matérielle et logicielle. Ces données sont dans le répertoire 'logs' qui est accessible via le menu "Outils>Afficher le répertoire contenant les logs". Rien de sort de votre ordinateur. C'est uniquement en cas de problème que je vais vous demander de m'envoyer les fichiers 'logs'.
