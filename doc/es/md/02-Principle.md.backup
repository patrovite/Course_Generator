# El principio

Para funcionar, **Course Generator** necesita, como entrada, datos que contengan una secuencia de puntos GPS representando una ruta

Estos datos pueden ser:

* Un archivo en formato GPX. Este archivo contiene los datos de la ruta que ha sido creada o descargada desde algún sitio web,
* Un archivo en formato CGX que es el formato de grabación de **Course Generator**.

## ¿Qué es un archivo GPX?

El formato GPX es un dato de intercambio estándar en cartografía creado por GARMIN. Estos datos provienen tanto de un GPS, de un software o de un sitio web.

Permite intercambiar:

* Puntos de referencia. Estos son puntos GPS, que contienen la latitud, longitud y altitud que está asociado con información como un nombre o un símbolo. El número de puntos de referencia está normalmente limitado en el GPS (a menudo solo admite hasta 500 puntos en un GPS GARMIN).
* Rutas. Consisten en un conjunto de puntos de referencia. Están por tanto limitadas por el número de referencia que puede contener un GPS.
* Pistas. Una ruta consiste en una serie de puntos GPS (y no puntos de referencia). Cada punto GPS contiene al menos la latitud y la longitud de cada punto. La altitud y el tiempo de grabación generalmente están incluidos en cada punto.

![Imagen de Wikipedia](./images/CG40_GPX.png) 
Fuente: Wikipedia

Cuando abre un archivo GPS, **Course Generator** solo lee las pistas. Otros tipos de datos son ignorados.

## El formato CGX

El formato CGX es el formato nativo de **Course Generator**. Permite además de la latitud, longitud y altitud, almacenar datos específicos de **Course Generator**. Esto incluye por ejemplo: la dificultad del terreno, estaciones de ayuda, comentarios, datos de un mini libro de ruta, ... Este formato te permite intercambiar una pista con un conjunto completo de información sobre ella.

## El principio de funcionamiento de Course Generator

El siguiente diagrama muestra el principio de funcionamiento de **Course Generator**.

![Principio de funcionamiento](./images/CG40_Principe.png)

## Casos de uso

Se pueden considerar los siguientes casos de uso (no trata de ser una lista exhaustiva):

* Preparar una carrera. Después de descargar las pistas GPS de la carrera, ajustarás tus parámetros, ajustarás la "calidad" del terreno, indicarás las estaciones de ayuda y los tiempos planeados de descanso, podrás añadir comentarios y muchas otras cosas. Finalmente, **Course Generator** calculará el tiempo para pasar por cada punto de la pista. Esto te permitirá obtener tu tiempo en cada punto, estadísticas (por ejemplo el tiempo pasado a más de 2000m por la noche) y generar un mini libro de ruta.
* Para la dirección de la carrera, el poder compartir la ruta en la que podrán indicar la "calidad" del terreno, las estaciones de ayuda y los tiempos límite.
* Generar un archivo GPX con los datos de los tiempos precalculados para utilizar el acompañante virtual del GPS GARMIN. Esto te permite tener un compañero virtual que corra contigo. Si has escogido los parámetros correctos, podrás ser capaz de correr a su lado. Esta función también muestra tu posición y la posición del compañero en la ruta y en los perfiles de la ruta. Te ofrece el tiempo que queda de la ruta y también la distancia que falta. Es muy conveniente a la hora de gestionar tu esfuerzo. La captura de pantalla que se muestra a continuación muestra la vista del perfil en el modo de acompañante virtual en un Forerunner 205/305. El punto oscuro eres tu mismo y el punto claro es el acompañante virtual.

![](./images/CG40_Virtual_Partner.jpg)


> __¿Necesito tener un GPS Garmin para utilizar **Course Generator**?__
>
> ¡No! Pero es un extra si quieres utilizar la característica del acompañante virtual. Eso es lo que me llevó a crear **Course Generator** (incluso aunque ahora no utilice ya esa característica).
