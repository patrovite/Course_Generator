<<<<<<< HEAD
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
=======
# Tabs

The top area contains a series of tabs that allow you to choose the type of data to display.

## "GPS data" tab

This tab displays all the data of the track.  
![GPS data tab](./images/Tabs/CG40_Tab_Data.png)

A double click on one of the lines opens the edit window.

The details of the columns are described below:

* **Num** : This is the row number of the table. It allows you to find quickly a line in the table.
* **Lat** : Contains the latitude of the point in degree.
* **Lon** : Contains the longitude of the point in degree.
* **Alt** : Contains the altitude of the point in meters/feet.
    * On the left of the altitude an arrow indicates the inclination of the field.
        * Upwards, this indicates that we have climbed since the last point.
        * Donwards, this indicates that we have descended from the last point.
        * To the right, this indicates that the field is flat.
    * The background color indicates the degree of slope of the field.
        * Brown if climbing (positive slope). The higher the slope, the darker the color.
        * White if the field is flat.
        * Green if going down (negative slope). The higher the slope, the darker the color.
* **Tag** : Displays the marks associated with the point.
    * ![](./images/Tags/high_point.png) : Indicates a high point.
    * ![](./images/Tags/low_point.png) : Indicates a low point.
    * ![](./images/Tags/eat.png) : Indicates a refueling point.
    * ![](./images/Tags/drink.png) : Indicates a water point.
    * ![](./images/Tags/flag.png) : Indicates that the point has been marked.
    * ![](./images/Tags/photo.png) : Indicates a view place.
    * ![](./images/Tags/note.png) : Indicates a note.
    * ![](./images/Tags/info.png) : Indicates information.
    * ![](./images/Tags/roadbook.png) : Indicates the beginning or the end of a roadbook section.
    * ![](./images/Tags/dropbag.png) : Indicates a drop bag
    * ![](./images/Tags/crew.png) : Indicates the presence of crew
    * ![](./images/Tags/first_aid.png) : Indicates a first aid
* **Dist** : Contains the distance, in meters/miles, since the previous point.
* **Total** : Contains the distance, in kilometers/miles, that has been traveled to this point.
* **Diff** : Contains the field difficulty between the previous point and this point. The initial value is 100, this corresponds to a flat road. The lower the value, the more rugged it is. If you enter 80, this indicates that compared to a flat road you will spend 20% more time to cover the distance. The button ![](./images/Toolbar/fill_diff.png) allows you to quickly fill a set of position. You will find below the values ​​used by Softrun (www.softrun.fr). Thanks to Rémi Poisvert for these informations.
    * "Easy field" = 98
    * "Average field" (normal mountain trail) = 95
    * "Hard field" = 88
    * "Very hard field" = 80
    * "Extremely hard field" = < 80
* **Coeff** : Contains the fatigue coefficient to be applied between the previous point and this point. The value is between 1 and 200. The initial value is 100, this corresponds to your initial state (fresh state). If you enter 80, it indicates that you will spend 20% more time to travel the distance.
The button ![](./images/Toolbar/fill_coeff.png) allows to define a rule of global evolution of the fatigue coefficient.
* **Recovery** : Contains the recovery coefficient that will be added to the fatigue coefficient. It is a relative value that is between 0 and 100. Initial value is 0 (not displayed). The sum "Coeff" + "Recovery" is limited to 100%.
This parameter is used to indicate recovery after a break (refueling, nap at a aid station...). Once the value entered, it's necessary to restart an overall calculation of the coefficient of fatigue with the button ![](./images/Toolbar/fill_coeff.png).
* **Time** : Contains the total time needed to reach this point from the start.
* **Cut-off** : Contains the cut-off time at this point of the track. This is the time since the start and not the hour (this avoids the problems related to the departure times shift). If at a point in the track the time is greater than the cut-off time then an indicator appears in the status bar. A click on the indicator will select the first line where the cut-off time has been exceeded.
* **Hours** : Contains the day and time of passage at this point. Start date and time are adjustable in the track parameters.
If the background is green it indicates that the travel is made during day time. If the background is blue then the trip is done during night time.
* **Aid station** : Contains the total refueling time you expect to spend at this point.
If no refueling is planned for this point (time equal to 00:00.00) then the cell is empty.
* **Name** : Contains the name of the point. It is used in profile view, reports and mini roadbook.
* **Comment** : Contains a comment on the point. It is used in reports and the mini roadbook.

## "Profile" tab

This tab displays the track profile.  
![Profile tab](./images/Tabs/CG40_Tab_Profil.png)

If a mark has been positioned in the table then a point appears on the curve as well as its serial number. This number corresponds to the table row of the Summary tab.

A left click on the curve makes it possible to position a cursor and to obtain information on the point (information coming from the data table).

A right click on the curve displays a menu to adjust the display of the curve and save the display as an image (PNG format).

The button ![](./images/Tabs/profil_marker.png) displays the row position of the "Summary" table.


## "Statistics" tab

This tab provides statistics on the track.  
![Statistics tab](./images/Tabs/CG40_Tab_Stat.png)

Statistics contain among other things:

* Average speed, distance and time for various slope areas
* Average speed, distance and time for various altitude ranges
* Average speed, distance and time for the daylight period
* Average speed, distance and time for the night period
* The average percentage of slope of the ascent and descent
* The distance traveled uphill, on the flat and downhill
* The temperature difference between the low point of the course and the high point. This value is purely indicative and theoretical. It does not take into account the wind chill effect and local phenomena. The basis of calculation is 0.6°C per 100m of elevation gain.

The button ![](./images/Tabs/save_html.png) saves this data in HTML format.  
The button ![](./images/Tabs/refresh.png) refreshes the data.

## "Analysis" tab
This tab allows to obtain an analysis of your track. It contains 3 sub-tabs which are detailed below.

### "Analysis>Time/Distance" tab

This tab allows you to analyze your speed over time.

![Time/Distance tab](./images/Tabs/CG40_Tab_Analyze_Dist_Time.png)

Two curves are displayed:

* A curve of the altitude over the distance
* A curve of the time (in seconds) over the distance

The study of the curve of time/distance allows to see its evolution on the track (acceleration, slowdown, stopped).
The change of slope of the red curve indicates a variation of speed.

The following cases are possible:

* The slope of the curve becomes steeper. This indicates a slowdown (due to the terrain or fatigue).
* The slope of the curve becomes less steep. This indicates an acceleration.
* The slope changes abruptly. This indicates a stop.

### "Analysis>Speed" tab

This tab allows you to get an analysis of the speed on your track.  
![Speed tab](./images/Tabs/CG40_Tab_Analyze_Speed.png)

Two curves are present:

* A curve of the speed over the distance
* A regression curve of speed

A right click on the curve display a menu allowing to save the curve as an image.

### "Analysis>Speed​​/Slope" tab

This tab is used to extrapolate a speed curve over the slope.  
![Speed/slope tab](./images/Tabs/CG40_Tab_Analyze_Speed_Slope.png)

This curve is useful for creating its own speed/slope curves according to a previous track.

Two curves are present:

* A scatter plot that includes all the points acquired during your race
* A speed/slope curve (extrapolated)


The button ![](./images/Tabs/save_curve.png) saves the result curve (red curve) in the speed/slope curve library. It will be useable in the curves dialog.  

The following dialog appears:  
![](./images/Tabs/CG40_Tab_Analyze_Speed_Slope_Save.png)  
The "Name" input field is used to enter the name of the curve.  
The "Comment" input field is used to enter a comment.

The button ![](./images/Tabs/correction.png) corrects the speed/slope curve with the track parameters (terrain difficulty and fatigue).  
The button ![](./images/Tabs/speed.png) allows to filter the speed of the speed/slope curve.


### "Summary" tab

This tab displays a table containing all the lines of the track containing a mark.  
![Summary tab](./images/Tabs/CG40_Tab_Resume.png)

For each line you have:

* A number.
* The name of the point.
* The line of the data table where is the point.
* The altitude of the point.
* The accumulated elevation gain up to the point.
* The accumulated elevation loss up to the point.
* The distance traveled up to the point.
* The time at the point.
* The hour at the point.
* The travel time since the last point.
* The cut-off expressed in time since the start.
* The refueling time.
* The distance from the last point.
* The elevation gain since the last point.
* The elevation loss since the last point.
* The climb speed since the last point.
* The descent speed since the last point.
* The average slope of the climbs since the last point.
* The average slope of the descents since the last point.
* The average speed since last point.
* The comment on this point.

The button ![](./images/Tabs/save_csv.png) saves the table data in CSV format in order to be used in a spreadsheet (Excel, OpenOffice Calc ...).  
The button ![](./images/Tabs/refresh.png) allows you to refresh the data.  

A double clic on a line open's the line editor for the corresponding point.
>>>>>>> upstream/master
