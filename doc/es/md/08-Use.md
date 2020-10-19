<<<<<<< HEAD
# Utilizando Course Generator

La forma normal de proceder para utilizar **Course Generator** es:

* Subir un archivo GPS
* Establecer los parámetros globales de la ruta (Nombre, fecha y hora de comienzo)
* Escoger la curva de "Velocidad/Desnivel"
* Establecer los parámetros de la ruta (dificultad del terrno, coeficiente de fatiga, estaciones de ayuda, etc)
* Comenzar el cálculo
* Guardad la ruta en formato CGX o GPX

Los capítulos a continuación describen todas estas acciones  (y muchas más).

## Cargar una ruta

Dos tipos de archivos pueden ser abierto por **Course Generator**

* El formato GPX contine una ruta GPS de una grabación realizada con un GPS, un software de mapeado o un sitio web. Este formato no contiene datos específicos para **Course Generator**. Este formato es utilizado si no tiene un archivo CGX de su ruta.
* El formato CGX, que es el formato de **Course Generator**, almacena todos los datos específicos del software. Es el recomendado si quiere mantener sus datos.

La carga de la ruta es realizada mediante el menú "Archivo> Abrir GPX" o "Abrir> Abrir CGX" de acuerdo al formato escogido.

Si la ruta contiene una alta densidad de puntos GPS, el software le preguntará si quiere aplicar un filtro para reducir el número de puntos GPS. Una desnsidad alta de puntos GPS puede interferir en los cálculos de la distancia.

Si carga un archivo GPX que no tiene hora o fecha **Course Generator** pondrá cada punto del tiempo de la ruta a 0. Será
necesario hacer clic en el botón ![refresh button](./images/Toolbar/refresh.png) para comenzar los cálculos del tiempo para
cada punto.

## Configurar una ruta

El ajuste de la ruta es realizado mediante "Ajustes>Ajustes de la ruta..." o [F9]

Se muestra la ventana de configuración que se muestra a continuación.

![Diálogo de los parámetros de la ruta](./images/CG40_Track_Param.png)

A continuación puede:

* Introducir el nombre de la ruta (15 caracteres como máximo).
* Añadir una descripción de la ruta. Esta descripción aparecerá en el libro de ruta.
* Establecer la fecha y hora de comienzo.
* Seleccionar si quiere utilizar la funcionalidad del efecto de altitud.
* Seleccionar si quiere utilizar la funcionalidad del efecto noche. Entonces deberá introducir los tiempos de comienzo y finalización de la noche y el factor de corrección aplicado para cada posición. El botón de "Auto ajuste" abre el la caja de diálogo para calcular automáticamente la hora de amanecer y anochecer. El cálculo es realizado desde la información del primer punto GPS de la tabla (coordenadas GPS, fecha y horas).

Aparecerá la siguiente ventana:

![Diálogo solar](./images/CG40_Track_Param_Sun_Dlg.png)

Introducir la zona horaria (1 para Francia).

Una vez que el ajuste está completad, pulse sobre "Ok" para validar.
Si ha escogido tener en cuenta el efecto nocho, verá esto:

* la columna "Tiempo" muestra un fondo azul durante la noche y verde en los períodos de día.
* en el mapa las áreas recorridad durante la noche tienen una trama resaltada.

## Establecer la curva de velocidad/pendiente

Para tener un tiempo de ruta consistente con su velocidad, tiene que escoger o crear una curva de velocidad de acuerdo con la pendiente. Esta curva va desde -50% (pendiente cuesta abajo) a +50% (pendiente cuesta arriba). La elección de la curva se realiza con la elección de la velocidad que uno quiere mantener cuando la pendiente es nula (0%) en el camino. Se ha creado un juego de curvas para cubrir la mayoría de las velocidades de la carrera.

El menú "Ajustes>Curvas de velocidad/pendiente..." o el botón ![botón curva](./images/Toolbar/chart_curve.png) mostrará la caja de diálogo para seleccionar y gestionar las curvas.

![Diálogo de curvas](./images/Curve/CG40_Dlg_Curves.png)

En la izquierda aparece la listas de curvas ya creadas. Durante la creación, se asigna un nombre explícito para encontrarlas rápidamente. Intente preservar este principio si quiere crear nuevas curvas.

El botón ![open curve button](./images/Curve/chart_curve_open.png) carga los datos de la curva seleccionada en la lista.
El botón ![edit curve button](./images/Curve/chart_curve_edit.png) permite modificar el dato de la curva seleccionada.
El botón ![add curve button](./images/Curve/chart_curve_add.png) permite crear una nueva curva.
El botón ![duplicate curve button](./images/Curve/chart_curve_duplicate.png) duplica la curva seleccionada.
El botón ![delete curve button](./images/Curve/chart_curve_delete.png) elimina la curva seleccionada.

> **Cómo seleccionar una curva de velocidad?**
> Esto depende de usted, de sus metas... Puede basarse en un porcentaje de su vVO2max.
> Por ejemplo para una ruta larga puede escoger 60% de su vVO2max. Sobre los 10Km/h en mi caso, entonces selecciono la curva de 10Km/h.
> También existe la práctica. Al principio seguro que subestima o sobreestima su velocidad. Pero con el tiempo su elección será más y más acertada.

**Notas:**  
* Si crea nuevas curvas que le interesen, no dude en enviármelas así podré añadirlas a la página web e incluirlas en próximas versiones.
* Cada curva es un archivo cuya extensión es '.par'. Estos archivos son accesible a través del menú "Herramientas>Abrir carpeta "Velocidad/pendiente"". Esto abrirá el gestor de archivos y mostrará el contenido del directorio.

## Establecer la dificultad del terrno

La columna "Dif" es utilizada para 'cuantificar' la dificultad del terreno.

Puede introducir rápidamente la dificultad del terreno para un grupo de puntos. Simplemente seleccione la primera línea y después manteniendo pulsada la tecla Shift seleccione las líneas siguientes (con el ratón o con el teclado). El botón ![diff button](./images/Toolbar/fill_diff.png) mostrará el diálogo para auto rellenarlo

![Diálogo de dificultad del terreno](./images/CG40_Dlg_Fill_Diff.png)

El área "Comienzo" es utilizada para definir el comienzo de la línea (desde el principio o desde un número de línea específico).
El área "Fin" es utilizada para definir el final de la línea (hasta el final o hasta un número de línea específico).
La zona "Dificultad" hace posible el poder escoger entre la dificultad del terreno. También puede utilizar valore predefinidos o introducir sus propios valores de dificultad.

Esta acción también puede realizarse en el mapa de la ruta (ve a continuación).

**Nota:**  
A veces es imposible determinar o saber la calidad del terreno. Puede ser prudente establecer una calidad del terreno media para la ruta entera. Por ejemplo, "Montagn'hard 100" ha sido calificada como terreno "medi". Incluso si algunas secciones fueron muy difíciles (áreas rocosas, resbaladizas, etc) y otras zonas muy fáciles (caminos o rutas)
No intente tomar en cuenta la pendiente del terreno porque esto es tomado en cuenta mediante la curva de "Velocidad/Pendiente" y el cálculo de la pendiente es automáticamente realizada por **Course Generator**.

## Establecer el coeficiente de fatiga

La columna de "Coef" es utilizada para 'cuantificar' la fatig durante el tiempo.

Puede introducir rápidamente el coeficiente de fatiga para un conjunto de puntos (normalmente para la ruta entera). Simplemente seleccionando la primera línea y después manteniendo pulsada la tecla Shift seleccione la líneas siguientes (con el ratón o con el teclado). El botón ![coeff button](./images/Toolbar/fill_coeff.png) permite mostrar el diálogo para el auto rellenado.

![Diálogo de coeficiente de salud](./images/CG40_Dlg_Fill_Coeff.png)

El área "Comienzo" es utilizada para definir la línea de comienzo (desde el comienzo o desde una línea específica). El campo de "Valor inicial" es utilizado para introducir el valor correspondiente.

El área "Fin" es utilizada para definir el final de la línea (hasta el final o a hasta un número de línea específico). El campo del "Valor final" es utilizado para introducir el valor correspondiente.

Si el valor final no es igual al inicial entonces las líneas intermedias tendrán una variación gradual y lineal de sus valores. Las ediciones manuales realizadas mediante el editr de línea serán sobreescritas.

Los ajustes realizados en esta ventana serán globales y se guardarán en el archivo CGX.

El área "Ayuda" permite de acuerdo con sus estimaciones darle un valor aproximado del coeficiente de fatiga. Este valor puede ser copiado en el "Valor inicial" y "Valor final" con los botones "> Comienzo" y "> Fin".

## Establecer los tiempos de abituallamiento

Para ajustarse más a la realidad, puede introducir el tiempo que planea pasar en una ubicación específica (abituallamiento, descanso, etc).

Se mosatrará el siguiente diálogo:

![Editor de línea](./images/CG40_Line_Editor.png)

Los campos de "tiempo en una estación de ayuda" son utilizados para introsducir el tiempo en esa localización (horas, minutos y segundos). El botón "0" permite establecer el tiempo en 00h00mm00ss.

> **¡FAQ!**
> El tiempo u horas mostradas en la línea que contiene el tiempo de abituallamiento es el tiempo u hora en la que planea dejar esa posición, NO el tiempo en el que planea llegar a esa posición.
> Es el diseño de **Course Generator** el que fuerza este método de cálculo.
>
> Para resumir:
> [Hora] = [Hora de la posición anterior] + [Tiempo de recorrido entre 2 posiciones] + [Tiempo de abituallamiento]
>
> [Tiempo] = [Tiempo de la posición anterior] + [Tiempo de recorrido entre 2 posiciones] + [Tiempo de abituallamiento]

## Establecer los tiempos de recuperación

Puede introducir para un punto dado el coeficiente de recuperación despues de descansar. Para hacer esto, vaya a la celda correspondiente y abra el editor de línea haciendo doble clic.

Se mostrará el siguiente diálogo:

![Editor de línea](./images/CG40_Line_Editor.png)

El campo "Recuperación" es utilizado para introducir el coeficiente de recuperación (entre 0 y 100). Este valor es relativo. Si piensa que puede recuperar un 5% del coeficiente de fatiga, debería introducir un 5 no el valor que piensa que tendrá (por ejemplo de 85% a 90%).

**Nota :**  
Después de modificar la columna de "Recuperación", es necesario realizar un cálculo global con el botón ![fill coeff](./images/Toolbar/fill_coeff.png) para que las nuevas entradas sean tenidas en cuenta.

## Establecer los tiempos de corte

Puede introducir para un punto dado el tiempo de corte programado. Este tiempo de corte está expresado en tiempo desde el comienzo no el tiempo en ese punto. Esto hace posible el tener en cuenta los retrasos en la salida (por ejemplo la UTMB 2011 fue retrasada 5 horas). Para realizar esto, vaya a la celda correspondiente y abra el editor de línea haciendo doble clic.

Se mostrará el siguiente diálogo:

![Editr de línea](./images/CG40_Line_Editor.png)

Los campos de "Tiempo de corte" son utilizados para introducir el tiempo de corte (horas, minutos y segundos). El botón "0" le permitirá establecer el tiempo en 00h00m00s.

Para que los cambios sean tenidos en cuenta es necesario realizar un cálculo mediante el botón ![refresh button](./images/Toolbar/refresh.png). Después del cálculo si el tiempo de una posición excede el tiempo de corte entonces aparecerá un indicador en rojo "Tiempo de corte" en la barra de estado. Haciendo clic sobre el indicador seleccionará la primera línea de la ruta donde el tiempo de corte ha sido excedido.

## Indicadores o etiquetas

Para cada punto tiene indicadores o etiqueta que indican una particularidad en el punto.

Los diferente indicadores son los siguientes:

* ![indicador de punto alto](./images/Tags/high_point.png) : Indica un punto alto. Este indicador es seleccionado manualmente o automáticamente por la función "Encontrar Min/Max".
* ![indicador de punto bajo](./images/Tags/low_point.png) : Indica un punto bajo. Este indicador es seleccionado manualmente o automáticamente por la función "Encontrar Min/Max".
* ![indicador de comida](./images/Tags/eat.png) : Indica un punto de abituallamiento(comida o bebida.
* ![indicador de bebida](./images/Tags/drink.png) : Indica un punto de agua.
* ![indicador de foto](./images/Tags/photo.png) : Indica una vista panorámica.
* ![indicador de marca](./images/Tags/flag.png) : Indica un punto especial. Este indicador es llamado "Marca" y permite dividir la ruta en pasos. Cada marca añade una línea en la tabla del sumario.
* ![](./images/Tags/dropbag.png) : Indica un punto para dejar la bolsa.
* ![](./images/Tags/crew.png) : Indica la presencia de público.
* ![](./images/Tags/first_aid.png) : Indica un puesto de ayuda.

Para establecer los indicadores a una posición, seleccione la celda correspondiente y abra el editor de línea haciendo doble clic.

Se mostrará el siguiente diálogo.

![Editor de línea](./images/CG40_Line_Editor.png)

Los indicadores aparecen delante de las "Etiquetas".

Para ahorrar tiempo, hay atajos de teclado disponibles:

* [F6] permite colocar o eliminar una "Marca" en la línea seleccionada.
* [F7] permite moverse rápidamente a la siguiente línea que contiene un indicador.
* [Ctrl+F7] permite moverse rápidamente a la línea previa que contiene un indicador.

## Calcular el tiempo de la ruta

Una vez que los parámetros de la ruta han sido introducidos, es necesario hacer clic en el botón ![refresh button](./images/Toolbar/refresh.png) para comenzar el cálculo del tiempo para cada punto.
Las columnas de "Tiempo" y "Horas" serán entonces actualizadas de acuerdo a los ajustes que ha realizado previamente. En la barra de estado al final de la ventana, se actualiza el tiempo total.

## Guardar la ruta

**Course Generator** ofrece la posibilidad de guardar la ruta en diversos formatos.

* "Archivo>Guardar como GPX" guarda la ruta en formato GPX que es el estándar de formatos de intercambios de rutas. El problema es que no guarda datos específicso de **Course Generator**
* "Archivo>Guardar como CGX" guarda la ruta en formato CGX que es el estándar de **Course Generator**. Este formato debería ser utilizado tan pronto como desee mantener los ajustes realizados en la ruta.
* "Archivo>Guardar como CSV" guarda la ruta en formato CSV que es el estándar para guardar datos en formato texto separados por comas. Estos archivos pueden ser abierto por programas como EXCEL, OpenOffice Calc o Libre Office Calc.Calc.
=======
# Using Course Generator

The normal procedure for using **Course Generator** is:

* Upload a GPS file
* Set the global track parameters (Name, date and start time)
* Choose the "Speed/Slope"curve
* Set the track parameters (terrain difficulty, fatigue coefficient, aid stations...)
* Start the calculation
* Save the track in CGX or GPX format

The below chapters will describe all these manipulations (And much more).

## Load a track

Two types of files can be opened by **Course Generator**.

* The GPX format contains a GPS track from a recording made with a GPS, a mapping software or a website. This format does not contain specific **Course Generator** data. This format is to use if you don't have a CGX file of your track.
* The CGX format, which is the format of **Course Generator**, stores all the specific data of the software. It is preferred if you want to keep your work.

The loading of a track is done by the menu "File> Open GPX" or "File> Open CGX" according to the chosen format.

If the track contain a too high density of GPS points, the software will ask you if you want to apply a filter to reduce the number of GPS points. A too high density of GPS points can disturb the distance calculations.

If you load a GPX file that have no time data the **Course Generator** will the time for each point of the track at 0. It will be necessary to click on the button ![refresh button](./images/Toolbar/refresh.png) to start the calculation of the time for each point.


## Set up a track

The setting of the track is done with "Settings>Track settings..." or [F9].

The configuration window below is then displayed.

![Track parameters dialog](./images/CG40_Track_Param.png)

You can then:

* Enter the name of the track (maximum 15 characters).
* Add a description of the track. This description will appear in the roadbook.
* Set the date and time of start.
* Select if you want to use the altitude effect feature.
* Select if you want to use the night effect feature. You must then enter the times for the beginning and ending of the night and the correction factor to be applied to each position. The "Auto-adjustment" button opens the dialog box to automatically calculate the sunset and sunrise time. The calculation is made from the information of the first GPS point of the table(GPS coordinates, date and hours).

The following window appears:

![Sun dialog](./images/CG40_Track_Param_Sun_Dlg.png)

Enter the time zone (1 for France).

Once the setting is completed, press "Ok" to validate.
If you have chosen to take into account the night effect, you will see that :

* the "Time" column shows a blue background during the night and green periods during the daytime periods.
* on the map the areas traveled at night have a highlighted plot.

## Set the speed/slope curve

In order to have a track time consistent with your speed, you have to choose or create a speed curve according to the slope. This curve goes from -50% slope (downhill) to + 50% slope (climb). The choice of the curve is made by the choice of the speed that one wants to "hold" when the slope is null (0%) on a road. A set of curves has been created to cover most running speeds.

The menu "Settings>Speed/Slope curves..." or the button ![curve button](./images/Toolbar/chart_curve.png) will display the dialog box for selecting and managing curves.

![Curves dialog](./images/Curve/CG40_Dlg_Curves.png)

On the left appears the list of curves already created. During the creation, an explicit name was assigned to them to find them quickly. Try to preserve this principle if you create new curves.

The button ![open curve button](./images/Curve/chart_curve_open.png) loads the data from the selected curve file into the list.  
The button ![edit curve button](./images/Curve/chart_curve_edit.png) allows you to modify the data of the selected curve.  
The button ![add curve button](./images/Curve/chart_curve_add.png) allows you to create a new curve.  
The button ![duplicate curve button](./images/Curve/chart_curve_duplicate.png) duplicates the selected curve.  
The button ![delete curve button](./images/Curve/chart_curve_delete.png) deletes the selected curve.  

> **How to choose a speed curve?**
> This will depend on you, your goals... You can base yourself on a percentage of your vVO2max.
> For example for a long trail you can take 60% of your vVO2max. About 10km/h in my case, I then select the curve of 10km/h.
> There is also the pratice. At the beginning you will surely underestimate or overestimate your speed. But over time your choice will become more and more accurate.

**Notes:**  
* If you create new curves that seem interesting to you, do not hesitate to send them to me so that I can add them on the website as well as in the next versions.
* Each curve is a file whose extension is '.par'. These files are accessible through the menu "Tools>Open "Speed/Slope" folder". This will open the file manager and display the contents of the directory.

## Set the terrain difficulty

The "Diff" column is used to 'quantify' the difficulty of the terrain.

You can quickly enter terrain difficulty for a set of points. Simply select the first line and then while holding down the SHIFT key you select the following lines (with mouse or keyboard). The button ![diff button](./images/Toolbar/fill_diff.png) will display the auto-fill dialog.

![Terrain Difficulty dialog](./images/CG40_Dlg_Fill_Diff.png)

The "Start" area is used to define the start line (from the beginning or from a specific line number).
The "End" area is used to define the end line (to the end or to a specific line number).
The zone "Difficulty" makes it possible to choose the difficulty of the terrain. You can either use the pre-determined values or enter your own difficulty value.

This action can also be done on the track map (see below).

**Note:**  
It is sometimes impossible to determine or know the quality of the terrain. It may be wise to set an average terrain quality for the entire track. For example, the "Montagn'hard 100" has been rated as "average" terrain. Even if some sections were very difficult (scree, slippery areas ...) and other very easy (roads or tracks).
Don't try to take into account the slope of the terrain because this is taken into account via the "Speed/Slope" curve and the calculation of the slope is automatically done by **Course Generator**.

## Set the fatigue coefficient

The "Coeff" column is used to 'quantify' fatigue over time.

You can quickly enter the fatigue coefficient for a set of points (usually for the entire track). Simply select the first line and then while holding down the SHIFT key select the following lines (with mouse or keyboard). The button ![coeff button](./images/Toolbar/fill_coeff.png) allows to display the auto-fill dialog.

![Health coefficient dialog](./images/CG40_Dlg_Fill_Coeff.png)

The "Start" area is used to define the start line (from the beginning or from a specific line number). The "Initial value" field is used to enter the corresponding value.

The "End" area is used to define the end line (to the end or to a specific line number). The "Ending value" field is used to enter the corresponding value.

If the ending value is not equal to the initial value then the intermediate lines will have a gradual and linear variation of the values. Manual edits made through the line editor will be overwritten.

The settings made in this window will be global and stored in the CGX file.

The area "Help" allows according to your estimated to give you an approximate value of the coefficient of fatigue. This value can be copied to in the "Initial value" and "Ending value" fields with the buttons "> Start" and "> End".

## Set the refueling times

In order to stick to reality, you can enter the time you plan to spend at a specific location (refueling, rest...).

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Aid station time" fields are used to enter the time at this location (hours, minutes and seconds). The "0" button allows you to reset the time to 00h00mm00s.

> **FAQ!**
> The time or hour displayed on the line containing a refueling time is the time or hour you plan to leave from this position, NOT the time you plan to arrive at this position.
> It's the design of **Course Generator** that forces this method of calculation.
>
> To summarize:
> [Hour] = [Hour of the previous position] + [Travel time between 2 positions] + [Refueling time]
>
> [Time] = [Time of the previous position] + [Travel time between 2 positions] + [Refueling time]

## Set the recovery times

You can enter for a given point the recovery coefficient after refueling or rest. To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Recovery" field is used to enter the recovery coefficient (between 0 and 100). This value is relative. If you think that you will recover 5% of fatigue coefficient, you must enter 5 and not the value you think you will have (eg from 85% to 90%).

**Note :**  
After modifying the "Recovery" column, it is necessary to restart a global calculation with the button ![fill coeff](./images/Toolbar/fill_coeff.png) in order to have your input taken into account.

## Set the cut-off times

You can enter for a given point the scheduled ccut-off time. This cut-off time is expressed in time since the start and not the time at the point. This makes it possible to take into account the departure delays (for example the UTMB 2011 was delayed by 5 hours). To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Cut-off time" fields are used to enter the cut-off time (hours, minutes and seconds). The "0" button allows you to reset the time to 00h00mm00s.

In order to take into account the input it is necessary to run a calculation with the button  ![refresh button](./images/Toolbar/refresh.png). After the calculation if one of the position time exceeds a cut-off time then a red indicator "Cut-off time" will appear in the status bar. A click on the indicator will select the first line of the trackthe first line where the cut-off time has been exceeded.

## Indicators or tags

For each point you can have indicators or tags that indicates a particularity of the point.

The different indicators are as follows:

* ![high point indicator](./images/Tags/high_point.png) : Indicates a high point. This indicator is selected manually or automatically by the function "Find Min/Max".
* ![low point indicator](./images/Tags/low_point.png) : Indicates a low point. This indicator is selected manually or automatically by the function "Find Min/Max".
* ![eat indicator](./images/Tags/eat.png) : Indicates a refueling point (eat or drink).
* ![drink indicator](./images/Tags/drink.png) : Indicates a water point.
* ![photo indicator](./images/Tags/photo.png) : Indicates a view point.
* ![mark indicator](./images/Tags/flag.png) : Indicates a special point. This indicator is called "Mark" and allows you to split the track into steps. Each mark adds a line in the summary table.
* ![note indicator](./images/Tags/note.png) : Indicates a note.
* ![info indicator](./images/Tags/info.png) : Indicates information.
* ![roadbook indicator](./images/Tags/roadbook.png) : Indicates the beginning of a new part of the roadbook.
* ![](./images/Tags/dropbag.png) : Indicates a drop bag
* ![](./images/Tags/crew.png) : Indicates the presence of crew
* ![](./images/Tags/first_aid.png) : Indicates a first aid

To set the indicators for a position, select the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The indicators appear in front of "Tags".

In order to save time, keyboard shortcuts are available:

* [F6] allows to put or remove a "Mark" on the selected line.
* [F7] allows you to move quickly to the next line containing an indicator.
* [Ctrl+F7] allows you to move quickly to the previous line containing an indicator.

## Calculate the track time

Once the track parameters have been entered, it is necessary to click on the button ![refresh button](./images/Toolbar/refresh.png) to start the calculation of the time for each point.
The columns 'Time' and 'Hours' are then updated according to the settings you have made previously. In the status bar at the bottom of the window, the total time is updated.

## Save the track

**Course Generator** offers the possibility to save your track in several formats.

* "File>Save as GPX" save the track in GPX format which is the standard track exchange format. The problem with this format is that it does not store specific data of **Course Generator**.
* "File>Save as CGX" saves the track in CGX format which is the standard file format of **Course Generator**. This format should be used as soon as you want to keep the settings made on a track.
* "File>Save as CSV" saves the track in CSV format which is a standard format for saving data as semicolon-separated text. These files can be opened by a spreadsheet program such as EXCEL, OpenOffice Calc or Libre Office Calc.
>>>>>>> upstream/master
