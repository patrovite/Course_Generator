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
