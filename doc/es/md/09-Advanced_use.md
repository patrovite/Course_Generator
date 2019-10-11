# Uso avanzado de Course Generator

Este capítulo le enseñará a hacer un uso más avanzado de **Course Generator**.

## Encontrar un punto GPS

Es posible buscar un punto GPS en una ruta mediante el menú "Editar>Buscar un punto..." o mediante el atajo de teclado [Ctrl + F]

![Diálogo de búsqueda](./images/CG40_Search.png)

En la caja de diálogo, es necesario introducir la latitud y longitud del punto y presionar el botón de buscar ![search button](./images/Toolbar/search.png).

**Course Generator** buscará el punto más cercano al conjunto de coordenadas proporcionadas. indicará la línea correspondiente al punto encontrado y también la distancia entre el punto encontrado y las coordenadas introducidas. La línea que contiene el punto encontrado será seleccionada.

## Encontrar los puntos más altos y bajos de una ruta

El menú "Herramientas>Encontrar Min/Max" es utilizado para encontrar automáticamente los puntos más altos y bajos de la ruta.
Cada posición encontrada es marcada con un indicador ![indicador punto más alto](./images/Tags/high_point.png) (punto alto) o ![indicador punto más bajo](./images/Tags/low_point.png) (punto bajo).

## Cambiar la altitud de un punto de la ruta

Puede ocurrir que el dato de la elevación de una ruta no sea correcto. Esto puede ocurrir, si usted, o un sitio web como Openrunner, han realizado una corrección automática de las altitudes. La base de datos de SRTM en los cuales estos sitios se basan tienen áreas sin altitud. La base dada entonces devuelve una altitud de 32768m. Para poder solucionar este problema, puede editar la altitud del punto o los puntos erróneos

Para hacer esto, vaya a la casilla correspondiente y abra el editor de línea haciendo doble clic.

Se mostrará el siguiente diálogo:

![Editor de línea](./images/CG40_Line_Editor.png)

El campo "Altitud" es utilizado para introducir una nueva altitud.

Será necesario reiniciar el cálculo de la ruta mediante el botón ![refresh button](./images/Toolbar/refresh.png) para que la nueva altitud sea tomada en cuenta.

## Unir dos rutas

**Course Generator** ofrece la posibilidad de unir dos rutas.

Se debe proceder de la siguiente manera:

* Cargar la primera ruta en **Course Generator** (formato GPX o CGX).
* Importar la segunda ruta mediante "Archivo>Importar un archivo GPX" o "Archivo>Importar un archivo CGX".
* Aparecerá el siguiente diálogo:
![Diálogo de importar ruta](./images/CG40_Import_Track.png)
* Seleccionar "Insertar al principio" si quiere insertar la ruta antes que la actual ya cargada en la memoria. Si no es así, seleccione "añadir" y la ruta será añadida después de la ruta actual.
* Aparecerá la caja de diálogo de selección de archivo. Haga su elección y haga clic en "Abrir".
* La unión será realizada.

Si tiene otras rutas para unir, simplemente repita el proceso.

Después de unir las rutas deberá modificar los ajustes de coeficiente de fatiga y  realizar un nuevo cálculo de la ruta mediante el (botón ![botón refrescar](./images/Toolbar/refresh.png) o [F5]) debido a que los datos del tiempp serán erróneos.

Una vez completada la unión y realizado un nuevo cálculo de la ruta, ya podrá guardar la nueva ruta.

## Guardar parte de la ruta

**Course Generator** permite guardar la sección de una ruta.

Se debe proceder de la siguiente manera:

* En la tabla seleccionar la primera línea que quiere guardar.
* Mientras mantiene presionada la tecla Shift, seleccionar la última línea que quiere guardar.
* Seleccionar "Archivo>Guardar selección como xxx" (donde xxx corresponde al formato de archivo que desea).
* Aparecerá la caja del diálogo de guardar. Introduzca el nombre del archivo y confirme.
* ¡Ya está!

## Exportar etiquetas como puntos de la ruta

**Course Generator** ofrece la posibilidad de guardar puntos GPS que contienen etiquetas (o indicadores) como puntos de la ruta. Estos puntos pueden ser añadidos a su GPS par atener, por ejemplo, la indicación de la ruta con información adicional como pases, estaciones de ayuda o ciudades.

Se debe proceder de la siguiente manera:

* Seleccionar "Archivo>Exportar etiquetas como puntos de la ruta".
* Aparecerá el siguiente diálogo".
* The dialog box below appears:  
![Diálogo de exportar puntos de la ruta](./images/CG40_Export_Waypoints.png)
* Seleccionar los tipos de etiquetas que quiere exportar.
* Aparecerá la caja de diálogo de guardar.
* Los puntos serán guardados como un archivo que tiene el nombre introducido y la extensión GPX.

Este archivo podrá ser utilizado, por ejemplo con el software Basecamp de Garmin.

## Copiar el contenido de una celda

El contenido de una celda en la tabla de la ruta puede ser copiado al portapapeles para utilizarlo en otro software. Para realizar esta operación simplemente seleccione "Editar>Copiar". El contenido de la celda seleccionada es copiado al portapapeles como texto.

## Generar un mini libro de ruta

**Course Generator** ofrece la posibilidad de generar un mini libro de ruta. Este contendrá el perfil de la carrera e informaciones sobre puntos de la ruta. Este libro de ruta está disponible mediante "Mostrar>Generar mini libro de ruta".

Al final del proceso el mini libro de ruta es una imagen. Esta imagen puede ser imprimida utilizando software de dibujo como Paint, Photoshop o Gimp. También puede ser utilizada en otro software como Word, Excel, Inkscape, Illustrator, etc.

Los ajustes realizados para genera el mini libro de ruta son almacenados en un archivo CGX en la ruta. Si intercambia una ruta en formato CGX, intercambiará la ruta como toda su información (repostaje, calidad del terreno, tiempos de corte, etc) y también el mini libro de ruta.

Para poder utilizar el mini libro de ruta es necesario previamente establecer los parámetros de su ruta. Esto incluye:

* Haber introducido la dificultad del terreno (útil para el tipo "Caminos/Senderos").
* Rellenar el coeficiente de fatiga, tiempos de abituallamiento, tiempos de corte, nombres de los puntos importantes.
* Haber actualizado los cálculos (Tecla F5).
* Haber marcado las posiciones importantes con el indicador ![marca del indicador](./images/Tags/roadbook.png).
* Haber marcado los puntos importante con uno o más de los siguientes indicadores: ![punto más alto](./images/Tags/high_point.png) ![punto más bajo](./images/Tags/low_point.png) ![comida](./images/Tags/eat.png) ![bebida](./images/Tags/drink.png) ![bandera](./images/Tags/flag.png) ![](./images/Tags/dropbag.png) ![](./images/Tags/crew.png) ![](./images/Tags/first_aid.png)


### Presentación

"Mostrar>Generar mini libro de ruta" o el botón ![mrb](./images/Toolbar/roadbook.png) muestra la siguiente ventana:
![Diálogo del mini libro de ruta](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)

Contiene los siguiente elementos:

* Parte superior: La barra te herramientas para realizar acciones
* Parte media derecha: Contiene la tabal con todas las filas de la tabla de la ruta que contienen un indicador ![roadbook](./images/Tags/roadbook.png) y uno o más de los siguientes indicadores: ![](./images/Tags/high_point.png) ![](./images/Tags/low_point.png) ![](./images/Tags/eat.png) ![](./images/Tags/drink.png) ![](./images/Tags/flag.png) ![](./images/Tags/dropbag.png) ![](./images/Tags/crew.png) ![](./images/Tags/first_aid.png). Cada línea genera una etiqueta en el mini libro de ruta
* Parte media izquierda: Este área permita cambiar el contenido de la fila actualmente seleccionada
* Parte inferior: Contiene el mini libro de ruta con las etiquetas

Cuando una línea es seleccionada entonces la etiqueta correspondiente cambia de color (color salmón).

### Los tipos de mini libros de ruta

En la barra superior, la lista desplegable de "Tipo de perfil" permite escoger entre 3 tipos de perfiles:

El tipo "Simple":
![Tipo simple](./images/MRB/CG40_MRB_Simple_Label.png)    
El perfir de la ruta no incluye información adicional.

El tipo "Camino/Senderos":
![Tipo Camino/Senderos](./images/MRB/CG40_MRB_Road_Track_Label.png)  
El perfil resalta las partes de las carreteras y sendas con un código de color. Las carreteras son puntos de la tabla de la ruta cuyo coeficiente de campo es igual a 100%. El resto son considerados senderos.

El timpo "Desnivel":
![Tipo desnivel](./images/MRB/CG40_MRB_Slope_Label.png)  
El perfil destaca los grados de desnivel con un código de colores.

Los colores utilizados en el mini libro de ruta puedes ser configurads en la ventana de configuración accesible mediante el botón ![](./images/MRB/Toolbar/setting.png).

### La barra de herramientas

![](./images/MRB/CG40_MRB_Toolbar.png)  


* ![guardar](./images/MRB/Toolbar/save.png) : Guarda el mini libro de ruta como una imagen. La formato disponible es PNG.
* ![ajustes](./images/MRB/Toolbar/setting.png) : Abre la ventana de configuración del mini libro de ruta.
* ![](./images/MRB/Toolbar/pipette.png) : Copia el formato de la etiqueta actual para reproducirla en una o más etiquetas. Esta función está también accesible mediante el atajo de teclado CTRL + C
* ![](./images/MRB/Toolbar/replicate.png) :  Pega el formato en la etiqueta seleccionada. Solo se pegarán las propiedades seleccionadas en la ventana de configuración de la función. Esta función está también accesible mediante el atajo de teclado CTRL + V
* ![](./images/MRB/Toolbar/replicate_config.png) : Abre una ventana par ala configuración de la característica de formato duplicado como se muestra a continuación:
![Diálogo de configuración duplicado](./images/MRB/CG40_MRB_Replicate_Dlg.png)    
Selecciona el ajuste que quiere duplicar cuando copie.

* ![](./images/MRB/Toolbar/label_to_bottom.png) : Permite especificar las etiquetas que serán conectadas en la parte inferior del perfil. 
* ![](./images/MRB/Toolbar/label_to_profil.png) : Allows you to specify that labels must be connected to the profile.
* ![](./images/MRB/Toolbar/night_day.png) : Specify whether to show the day and night areas on the profile.
* "Profile type" : Allows you to select the type of mini roadbook.
    * Simple
    * With roads/trails
    * With slope
* ![](./images/MRB/Toolbar/favori1.png) ![](./images/MRB/Toolbar/favori2.png) ![](./images/MRB/Toolbar/favori3.png) ![](./images/MRB/Toolbar/favori4.png)
![](./images/MRB/Toolbar/favori5.png) : These 5 buttons are used to store display formats.
    * A right click on a button memorizes in the button the setting displayed in the current format field.
    * A left click on a button restores the format stored in the button in the format field.
* "Width" : Selects the width of the profile (Maximum value 4000 pixels).
* "Height" : Allows you to select the height of the profile (Maximum value 2000 pixels).


### Set the size of the mini roadbook

The maximum size of the mini roadbook is 4000x2000 pixels (Width x Height). It is also the size of the final image.

The size setting is made by the "Width" and "Height" fields located in the icon bar.

### Configure the mini roadbook

The icon ![](./images/MRB/Toolbar/setting.png) opens the configuration window.

The "General" tab allows you to:  
![General tab](./images/MRB/CG40_MRB_Settings1.png)

* Set the filter value to apply to the data when displaying the profile
* Set the top area height of the profile. This size is in pixel.

The "Simple" tab is used to adjust the colors of the "Simple" display of the profile.  
![Simple tab](./images/MRB/CG40_MRB_Settings2.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

The "Roads/Trails" tab is used to adjust the colors of the "Roads/Trails" display of the profile.  
![Roads/trails tab](./images/MRB/CG40_MRB_Settings3.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

The "Slope" tab is used to adjust the colors of the "Slope" display of the profile.  
![Slope tab](./images/MRB/CG40_MRB_Settings4.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

### Configure each label

Each line of the table represents a label. It can be configured by the setting items on the left of the table.  
![MRB editor](./images/MRB/CG40_MRB_Editor.png)

* Selected : Indicates if you want to take this label into account in the profile. If the box is unchecked then the table row is grayed out and the label is not displayed in the mini roadbook.
* Position : Adjusts the vertical position of the label. The number indicates the number of pixels relative to the highest position of the label.
* Alignment : Used to define the position of the label relative to the line connecting it to the profile. To the left of the line, centered on the line or on the right of the line.
* Format : Allows you to specify the contents of the label ([see below](#format)).
* Size : Sets the size of the font used in the labels.
* Tags : Indicate if you want the indicators to be displayed in the labels.
* Comment : Enter a specific comment for the label. This comment is different from the main table comment.

### Set the display format for labels {#format}

To ensure optimal flexibility, the format of labels uses tags. These tags, represented by the sign "%" plus one or more characters, allow to specify the type of data to display. For example, "% N" represents the name of the point. When the tag is displayed, the tags are replaced by their meanings.

The following tags are available:

* %N : Represents the name of the point.
* %A : Represents the altitude of the point. The unit is meter/feet.
* %D : Represents the distance since the start. The unit is kilometer/miles.
* %T : Represents the travel time since the start. The format is "hh:mm".
* %Tl : Represents the travel time since the start in long format "hh:mm:ss".
* %Ts : Represents the travel time since the start in short format "hh:mm".
* %H : Represents the time at this point. The format is "ddd hh:mm" (ddd = abbreviated day).
* %h : Represents the time at this point. The format is "hh:mm".
* %hl : Represents the time at this point in long format "hh:mm:ss".
* %hs : Represents the time at this point in short format "hh:mm".
* %B : Represents the cut-off time at this point (time). The format is "hh:mm".
* %b : Represents the cut-off time at this point (hour). The format is "hh:mm".
* %C : Represents the specific comment on the label (Entered in the "Comment" field of the label).
* %c : Represents the comment from the main table.
* %L : Inserts a line break.
* %R : Represents refueling time this point. The format is "hh:mm".
* %Rl : Represents refueling time this point in long format "hh:mm:ss".
* %Rs : Represents refueling time this point in short format "hh:mm".
* %+ : Represents the cumulative positive climb since the start. The unit is the meter/feet.
* %- : Represents the cumulative negative climb since the start. The unit is the meter/feet. 

The "..." button, next to the "Format" field, opens a window allowing you to simplify the content of the selected label.

![Label editor dialog](./images/MRB/CG40_MRB_Label_Editor.png)

### Showing day and night times

If in the track settings the night effect has been activated, then the button ![](./images/MRB/Toolbar/night_day.png) will display the day and night times.

![Day and night times](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)  
The night times are displayed with a gray background.

## Reverse track direction

**Course Generator** can reverse the direction of any given track. To perform this action select "Tools>Reverse track".
After the operation, it is necessary to redefine the fatigue coefficients and cut-off time and then restart a calculation (button ![](./images/Toolbar/refresh.png) or [F5]).

## Set a new starting point on a looped track

**Course Generator** offers the possibility, if the track loaded in memory is a loop, to define a new starting point.

The procedure is as follows:

* Select the position that will become your new starting point.
* Select "Tools>Define a new start".
* A following dialog box will appear:  
![Confirm new start dialog](./images/CG40_Dlg_Confirm_New_Start.png)
* Confirm the order by clicking on "Yes".

After the operation it is necessary to redefine the fatigue coefficients and cut-off times and then restart a calculation (button ![](./images/Toolbar/refresh.png) or [F5]).

## The general parameters of Course Generator

Le menu "Paramètres>Paramètres de Course Generator" affiche la fenêtre de configuration du logiciel.
The "Settings>Course Generator Settings" menu displays the software configuration window.

### "General" tab  
![](./images/CG40_Settings_General.png)

The possible settings are:

* "language" : Selects the language used in the interface. "System" uses operating system settings to determine which language to use. If the system language is not managed by **Course Generator** then English is selected.
* "Units" : Chooses between "km/m" and "Miles/Feet".
* "Speed format" : Chooses the type of speed to display (speed or pace).
* "Threshold for position filter (in %) : Threshold, in%, from which the software asks if we want to apply a filter on GPS points when loading a track.
* "Check for update at startup" : Allows you to choose if you want to check for a newer version of **Course Generator** available at application startup.

### "Display" tab  
![](./images/CG40_Settings_Display.png)

The possible settings are:

* "Default font" : Allows you to choose the font that will be used for the display.
* Icons size : Specify the size of the interface icons.

## Import and export marked points

In some cases it is necessary to save only the marked points of the current track in a file. This allows, if you have a new version of the course, to import these points on the new track.

For example:  
You have the track of the UTMB. You've spent time scouting each pass, aid station, and you've entered comments on parts of the track. Unfortunately, the track of the following year is slightly different but the main points are the same. The import/export function of marked points will save you a lot of time.

The procedure is as follows:

* Take the course of the previous year
* Export marked points with "File>Export points"
* Select the types of points to export in the following dialog box:    
![Export points dialog](./images/CG40_Export_Points.png)
* Validate your selection and enter the file name (extension '.CGP')
* Open the course of the following year
* Import points with "File>Import points"
* Select the file (extension .CGP) to import.
* The following dialog box appears:    
![Import point dialog](./images/CG40_Import_Points.png)
    * The column "Dist" indicates the distance between the point found in the track and the point to be imported. The green background color indicates that the point found is less than 100m, a yellow color indicates that the point is between 100m and 1000m and a red color indicates that the point is more than 1000m. If the distance is high, this indicates that the new course does not pass through this point. 
    * The column "Line" indicates the line of the track where the found point is
    * The column "Sel." select the points to import. "X" indicates that the line is selected.
* Select the points to import and click on "Import"

## Analyzing data after a race

**Course Generator** allows you to analyze the data after completing the track. Just open the file containing the GPS data (often a GPX file). You will find in the table all your data. Reports allow you to have information that data.

The data will remain unchanged until you ask for a calculation of the travel time. A window will ask you if you want to overwrite the temporal data.

## Using the map features

**Course Generator** displaying a course on an OpenStreetMap map.  
![Map](./images/Map/CG40_Map_Area.png)

On the left, a vertical bar of buttons allows actions on this map.

* ![](./images/Map/marker.png) : Adds an start mark to the selected location
* ![](./images/Map/hide_marker.png) : Deletes the mark
* ![](./images/Map/undo.png) : Cancels the last operation
* ![](./images/Map/track_very_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very easy"
* ![](./images/Map/track_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Easy"
* ![](./images/Map/track_average.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Average"
* ![](./images/Map/track_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Hard"
* ![](./images/Map/track_very_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very hard".
* ![](./images/Map/flag.png) : Adds a mark to the current point.
* ![](./images/Map/eat.png) : Adds an aid station to the current point.
* ![](./images/Map/drink.png) : Adds a water point to the current point.
* ![](./images/Map/select_map.png) : Allows you to select the map layer to be displayed.

The mouse commands are as follows:

* Left-click on the map sets the marker on track (nearest position).
* Holding the left mouse button moves the map.
* A double click on the map allows you to zoom in.


To change the quality of the terrain for a part of the track, you must:

* Position the cursor at the beginning of the area to be modified.
* Click on the button ![](./images/Map/marker.png) to set the marker.
* Position the cursor at the end of the area to change.
* Click on the button corresponding to the required field quality (for example ![](./images/Map/track_average.png)).

In the status bar, the indicator ![](./images/Statusbar/CG40_Statusbar_Map_Size.png) indicates the disk size used by the maps. The menu "Tools>Open 'Speed/slope' folder" will open the file manager and display the contents of the directory containing the curves, the logs and the directory containing the maps. The directory "OpenStreetMapTileCache" contains the map files. If needed, you can delete its contents to save space.
