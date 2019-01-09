# Introducción

**Course Generator** te permite procesar tus archivos GPS para poder :

* Calcular la duración de tu trayecto habiendo ajustado previamente los parámetros correspondientes a la naturaleza del terreno o tus habilidades,
* Calcular tus tiempos en cada punto de la ruta,
* Añadir durante el trayecto la naturaleza del terreno, tu coeficiente de fatiga en el paso del tiempo, los periodos de avituallamiento o descanso y comentarios,
* Definir tiempos límite (se mostrará un indicador el sobrepaso de ese tiempo),
* Definir coeficientes de recuperación,
* Definir los periodos nocturnos y la disminución de la eficiencia asociada,
* Seleccionar la disminución de rendimiento de acuerdo con la altitud,
* Ver tu ruta en un mapa (OpenStreetMap, OpenTopoMap, etc...),
* Generar un mini libro de ruta con el perfil de tu ruta y anotaciones en puntos clave (nombre, tiempo, altitud, desnivel ...),
* Generar un informe en formato de texto (CSV),
* Generar estadísticas sobre la ruta (En formato HTML),
* Invertir la dirección de la ruta,
* Establecer un nuevo punto de partida en una ruta en bucle,
* Insertar una ruta al principio o al final de otra ruta,
* Extraer parte de una ruta,
* Guardar la ruta modificada, conteniendo los tiempos calculados de la ruta, en formato GPX. Esto te permitirá, por ejemplo, utilizar la función "Virtual Partner" de los GPS GARMIN,
* Guardar la ruta en formato CGX que es el formato de salvaguarda de **Course Generator** para poder ser capaz de intercambiar las rutas manteniendo los datos del suelo, marcadores, los comentarios, ...

Y muchas otras cosas...

Las convenciones de escritura que se han adoptado en este manual, está detalladas a continuación.

* *"Archivo> Abrir GPX"* indica que es necesario seleccionar el menú "Archivo" y después seleccionar en ese menú, "Abrir GPX". Esto permite simplificar la descripción de una secuencia de acciones que se deben hacer con el ratón.
* *"[CTRL + O]"* indica una o una serie de teclas que se deben pulsar para poder realizar una acción. Por ejemplo [CTRL + O] indica que es necesario presionar la tecla CTRL mientras que al mismo tiempo se presiona la tecla O.

## Nota del autor

Desarrollé  **Course Generator** en 2008 como ayuda para prepararme para mi primera carrera de ultramaratón de 100km. Mejoré el software basándome en mis necesidades. Un pequeño artículo en Ultrafondu (una revista francesa) me permitió empezar a darla a conocer y después fui complementándola con los comentarios enviados por las personas que lo utilizaban.
Ha evolucionado enormemente y muchas veces, la he reescrito de acuerdo a mis necesidades. Cada vez fue un reto y me gustan las carreras que corro, fue una aventura que me hizo crecer (conocimiento, cuestionarse cosas, apertura de la mente ...).

**Course Generator** no pretende darte resultados totalmente precisos. Mucho depende de ti mismo/a y de las condiciones externas. Considera este software como una ayuda para prepararte para futuras aventuras.

Quizás has notado que no he utilizado el término "carrera" ("race" en el original) porque para mí el enfoque de "aventura" de una carrera, por más difícil que sea, siempre me ha permitido seguir adelante con su enfoque positivo (por no mencionar el crono de todos modos :) ).

El desarrollo de **Course Generator**  ha sido y continua siendo una aventura.

Si te gusta y encuentras útil este software puedes contribuir de maneras diferentes:

* Con una donación, visitando el sitio web de **Course Generator**. Esto me permitirá pagar el "hosting" de la web, las herramientas y libros que me permiten continuar esta aventura. El desarrollo de este aplicación la realiza en mi tiempo libre.
* Difundiéndolo. por elección propia, no soy muy activo en foros o redes sociales y empleo mi tiempo libre en mejorar el software. Si tienes la oportunidad, no dudes en hablar sobre **Course Generator**. Twitter, Facebook, foros y también Reddit o las redes sociales que frecuentes, son buenas plataformas para difundir esta herramienta.
* Con comentarios sobre el software. Errores, correcciones en la documentación y peticiones de mejoras son siempre bienvenidas.
* Participando en la traducción del software en otro idioma. Es simple, te enviaré un archivo de texto con textos en inglés y los traducirás en el idioma que sea de tu interés siguiendo unas pocas y simples reglas.
* Participando en el desarrollo del software. Nada muy complicado, tienes que tener conocimientos del lenguaje Java, git y GitHub. Desde la versión 4, **Course Generator** es de Código Abierto y está alojado en Github (github.com/patrovite/Course_Generator) así otras personas pueden mejorar el software junto a mí. El tema es amplio, todavía queda mucho por hacer.

Prepárate para una aventura con **Course Generator**.

Pierre DELORE

## Protección de datos personales

El software recopila información sobre la configuración de tu hardware y software en el archivo de registros (logs). Estos datos están en la carpeta 'logs' a la que puede accederse mediante el menú "Herramientas> Abrir la carpeta "Velocidad/Desnivel"". Ninguno de esos datos sale de tu equipo. Se almacenan en caso de algún problema, si contactas conmigo te pediré que me envíes los archivos de registros ('logs').
