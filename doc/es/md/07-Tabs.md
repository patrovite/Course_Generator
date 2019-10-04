# Pestañas

El área superior contiene una serie de pestañas que permiten la elección de tipo de datos a mostrar.

## Pestaña "Datos GPS" 

Thita pestaña muestra todos los datos de la ruta.  
![Pestaña de datos GPS](./images/Tabs/CG40_Tab_Data.png)

Al hacer doble clic en una de las líneas se abre la ventana del editor.

Los detalles de las columnas son descritos a continuación:

* **Num** : Este es el número de la fila de la tabla. Permite encontrar rápidamente una línea en la tabla.
* **Lat** : Contiene el punto de la latitud en grados.
* **Lon** : Contiene el punto de la longitud en grados.
* **Alt** : Contiene el punto de la altitud en metros/pies.
    * A la izquierda de la altitud una flecha indica la inclinación del terreno.
        * Hacia arriba, esto indica que hemos subido desde el punto anterior.
        * Hacia abajo, esto indica que hemos descendido desde el punto anterior.
        * Hacia la derecha, indica que el terreno es llano.
    * El color del fondo indica los grados de desnivel del terreno.
        * Marrón si está ascendiendo (desnivel positivo). Cuanto más alto es el desnivel, más oscuro es el color.
        * Blanco si el terreno es llano.
        * Verde si está descendiendo (desnivel negativo).Cuanto más alto es el desnivel, más oscuro es el color.
* **Tag** : Muestra las marcas asociadas con el punto.
    * ![](./images/Tags/high_point.png) : Indica un punto alto.
    * ![](./images/Tags/low_point.png) : Indica un punto bajo.
    * ![](./images/Tags/eat.png) : Indica un punto de abastecimiento.
    * ![](./images/Tags/drink.png) : Indica un punto de agua.
    * ![](./images/Tags/flag.png) : Indica que el punto ha sido marcado.
    * ![](./images/Tags/photo.png) : Indica un lugar de vista.
    * ![](./images/Tags/note.png) : Indica una nota.
    * ![](./images/Tags/info.png) : Indica información.
    * ![](./images/Tags/roadbook.png) : Indica el comienzo o el final de una sección del libro de ruta.
    * ![](./images/Tags/dropbag.png) : Indica un "drop bag".
    * ![](./images/Tags/crew.png) : Indica la presencia de público.
    * ![](./images/Tags/first_aid.png) : Indica una primera ayuda.
* **Dist** : Contiene la distancia, en metros/millas, desde el punto anterior.
* **Total** : Contiene la distancia, en kilómetros/millas, que han sido recorridos hasta ese punto.
* **Dif** : Contiene la dificultad del terrno entre el punto anterior y este punto. El valor inicial es 100, esto corresponde a un terreno llano. Cunato más bajo es el valor, más escabroso es el terreno. Si introduce 80, esto indica que comparado con un terreno plano necesitará un 20% más de tiempo en cubrir la distancia.
El botón ![](./images/Toolbar/fill_diff.png) permite rellenar rápidamente una posición. Encontrará debajo los valores ​​ utilizado por Softrun (www.softrun.fr). Gracias a Rémi Poisvert por estas informaciones.
    * "Terreno sencillo" = 98
    * "Terreno medio" (terreno normal de montaña) = 95
    * "Terrno duro" = 88
    * "Terreno nuy duro" = 80
    * "Terreno extremadamente duro" = < 80
* **Coef** : Contiene el coeficiente de fatiga para ser aplicado entre el punto anterior y este punto. El valor está entre 1 y 200. El valor inicial es 100, esto corresponde con su estado inicial (estado a punto) Si introduce un 80, esto indica que gastará un 20% más de tiempo en recorrer la distancia.
El botón ![](./images/Toolbar/fill_coeff.png) permite definir la regla de la evolución global del coeficinete de fatiga.
* **Recuperación** : Contiene el coeficiente de recuperación que será añadido al coeficiente de fatiga. Este es un valor relativo que está entre 0 y 100. El valor inicial es 0 (no mostrado). La suma de "Coeff" + "Recuperación" está limitado a 100%. Este parámetro es utilizado para indicar la recuperación después de un descanso (abituallamiento, siesta en una estación de asistencia, etc). Una vez que el valor es introducido, es necesario reiniciar un cálculo general del coeficiente de fatiga mediante el botón ![](./images/Toolbar/fill_coeff.png).
* **Tiempo** : Contiene el tiempo total necesario para llegar a ese punto desde el comienzo.
* **Cut-off** : Contiene el tiempo de corte en ese punto de la carrera. Este es el tiempo desde el comienzo y no la hora (esto evita los problemas relacionados con los cambios de tiempos de salida). Si en un punto de la ruta el tiempo es mucho mayor que el tiempo de corte entonces aparece un indicador en la barra de estado. Haciendo clic sobre el indicador seleccionará la primera línea donde el tiempo de corte ha sido excedido.
* **Horas** : Contiene el día y el tiempo de paso en ese punto. La fecha de comienzo y el tiempo son ajustables en los parámetros de la ruta.
Si el fondo es verde esto indica que el viaje se hace durante el día. Si el fondo es azul, el recorrido es realizado durante
la noche.
* **Estación de ayuda** : Contiene el tiempo total de abastecimiento que espera pasar en ese punto.Si no se planea repostar en ese punto (tiempo igual a 00:00.00) entonces la casilla está vacía.
* **Nombre** : Contiene el nombre del punto. Es utilizado en la vista del perfil, informes y mini libro de ruta.
* **Comentario** : Contiene un comentario sobre el punto. Es utilizado en los informes y en el mini libro de ruta.

## Pestaña "Perfil"

Esta pestaña muestra el perfil de la ruta.
![Pestaña de perfil](./images/Tabs/CG40_Tab_Profil.png)

Si una marca ha sido posicionada en la tabla entonces aparece un punto en la curva y también su número de serie. Este número
corresponde a la fila de la tabla en la pestaña del Sumario.

Haciendo clic con el botón izquierdo sobre la curva, esto hace posible posicionar un cursor y obtener información del punto (información que proviene de la tabla de datos).

Al hacer clic con el botón derecho en la curva, esto muestra un menú para ajustar el marcador de la curva y guardar ese marcador como una imagen (en formato PNG).

El botón ![](./images/Tabs/profil_marker.png) muestra la posición de la fila en la tabla "Sumario".


## Pestaña "Estadísticas"

Esta pestaña ofrece estadísticas de la ruta.
![Pestaña de estadísticas](./images/Tabs/CG40_Tab_Stat.png)

Las estadísticas contienen entre otras cosas:

* Velocidad media, distancia y tiempo para varias áreas de pendientes
* Velocidad media, distancia y tiempo para varios rangos de altitud
* Velocidad media, distancia y tiempo para el periodo de día
* Velocidad media, distancia y tiempo para el periodo de noche
* El porcentage medio de desnivel tanto de ascenso como de descenso
* La distancia recorrida cuesta arriba, en llano y cuesta abajo
* Las diferencias de temperatura entre el punto más bajo de la carrera y el punto más alto. Este valor es puramente indicativo y teórico. No tiene en cuenta la velocidad del viento y los fenómenos locales. La base para calcularlo es de 0.6°C por 100m de ganancia de altitud.

El botón ![](./images/Tabs/save_html.png) guarda estos datos en formato HTML.
El botón ![](./images/Tabs/refresh.png) actualiza los datos.

## Pestaña "Análisis" 
Esta pestaña permite obtener una análisis de su ruta. Contiene 3 sub pestañas que son detalladas a continuación.

### Pestaña de "Análisis>Tiempo/Distancia"

Esta pestaña permite analizar su velocidad durante el tiempo.

![Pestaña Tiempo/Distancia](./images/Tabs/CG40_Tab_Analyze_Dist_Time.png)

Dos curvas son mostradas:

* Una curva de altitud sobre la distancia
* Una curva de tiempo (en segundos) sobre la distancia

El estudio de la curva del tiempo/distancia ermite ver su evolución en la carrera (aceleración, descenso, parada).
El cambio de pendiente de la curva roja indica una variación de la velocidad.

Se pueden dar los siguientes casos posibles:

* La pendiente de la curva se hace más empinada. Esto indica un descenso (debido al terreno o fatiga).
* La pendiente de la curva se hace menos empinada. Esto indica una aceleración.
* La pendiente cambia abruptamente. Esto indica una parada. 

### Pendiente "Análisis>Velocidad"

Este pestaña permite obtener un análisis de la velocidad de su ruta.
![Pestaña velocidad](./images/Tabs/CG40_Tab_Analyze_Speed.png)

Hay presentes dos curvas:

* Una curva de velocidad sobre la distancia
* Una curva de regresión de la velocidad

Haciendo clic derecho en la curva muestra un menú que permite guardar la curva como una imagen.

### Pestaña "Análisis>Velocidad​​/Pendiente"

Esta pestaña es utilizada para extrapolar una curva de velocidad sobre la pendiente.
![Pestaña velocidad/pendiente](./images/Tabs/CG40_Tab_Analyze_Speed_Slope.png)

Esta curva es útil para crear sus propias curvas de velocidad/pendiente de acuerdo con rutas anteriores.

Dos curvas están presentes:

* Un gráfico de dispersión que incluye todos los puntos adquiridos durante su carrera
* Una curva de velocidad/pendiente (extrapolada)

El botónT ![](./images/Tabs/save_curve.png) guarda el resultado de la curva (la curva roja) en la librería de curva de velocidad/pendiente. Será utilizable en los diálogos de curvas.

Aparece el siguiente diálogo:
![](./images/Tabs/CG40_Tab_Analyze_Speed_Slope_Save.png)  
El campo de entrada "Nombre" es utilizado para introducir el nombre de la curva.
El campo de entrada "Comentario" es utilizado para introducir un comentario.

El botón ![](./images/Tabs/correction.png) corrige la curva dde velocidad/pendiente con los parámetros de la ruta (dificultad del terreno y fatiga).
El botón ![](./images/Tabs/speed.png) permite filtrar la velocidad de la curva de velocidad/pendiente.

### Pestaña "Sumario"
Esta pestaña muestra una tabla que contiene todas las líneas de la ruta que contienen una marca.
![Pestaña sumario](./images/Tabs/CG40_Tab_Resume.png)

PAra cada línea tiene:

* Un número.
* El nombre del punto.
* La línea de la tabla de datos donde está el punto.
* La altitud del punto.
* La ganancia de elevación acumulada hasta el punto.
* La pérdida de elevación acumulada hasta el punto.
* La distancia recorrida hasta el punto.
* El tiempo hasta el punto.
* La hora hasta el punto.
* El tiempo de recorrido desde el punto anterior.
* El corte expresado en tiempo desde el comienzo.
* El tiempo de abastecimiento.
* La distancia desde el punto anterior.
* La ganancia de elevación desde el punto anterior.
* La pérdido de elevación desde el punto anterior. 
* La velocidad de ascenso desde el punto anterior.
* La velocidad de descenso desde el punto anterior.
* El desnivel medio de subida desde el punto anterior.
* El desnivel medio de descenso desde el punto anterior.
* La velocidad media desde el punto anterior.
* El comentario en ese punto.

El botón ![](./images/Tabs/save_csv.png) guarda la tabla de datos en formato CSV para ser utilizada en una hoja de cálculo (Excel, OpenOffice Calc ...).  
El botón ![](./images/Tabs/refresh.png) permite actualizar los datos.

Al hacer doble clic en una línea abre el editor de líneas para el punto correspondiente.
