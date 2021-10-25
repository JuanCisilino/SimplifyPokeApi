Proyecto pokeApi simplificada :

Introducción:
PokeApi es una Api gratuita descentralizada que contiene la base de datos responsable de los juegos de Pokémon, 
lugares, personajes, características, por ende para obtener un espectro completo de los personajes(Pokémons)
es necesario realizar varias consultas a la api de forma simultanea.
Esta propuesta sugiere realizar una api que centralice estas llamadas de forma que al ser consumida brinde 
la mayor cantidad posible de características de cada personaje. Así como modificar su mote, apodo.

Desarrollo:
Se propone ejecutar inicialmente una llamada al endPoint responsable de la lista de personajes:
https://pokeapi.co/api/v2/pokemon?offset=0&limit=1200
la cantidad actual de personajes es: 1118 y suelen agregar entre 20/30 por temporada.
De cada personaje de la lista se ejecutan dos nuevas llamadas:
https://pokeapi.co/api/v2/pokemon/{id}/
https://pokeapi.co/api/v2/pokemon-species/{id}/
de estos endpoints podemos empezar a armar cada objeto y trayendo los nuevos endpoints a consultar:
https://pokeapi.co/api/v2/move/{moveId}/
https://pokeapi.co/api/v2/type/{typeId}/
https://pokeapi.co/api/v2/evolution-chain/{chainId}/
estos datos quedaran en la base de datos de la Api para una consulta rápida y eficiente.

Conclusión:
La Api será capaz de devolver listas paginadas de Pokémons con características completas, 
directamente para su consumo en el front, brindando la posibilidad de una implementación simple y escalable, 
capaz de modificar los objetos y persistirlos. (edited) 
