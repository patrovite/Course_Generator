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
