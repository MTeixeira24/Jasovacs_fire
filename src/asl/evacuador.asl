// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
knowExit.
exit_direction(1).

/* Initial goals */

!start.
//!wander.

/* Plans */

+!wander : estado(fora_perigo) <- !randomWalk; !wander.
+!wander : estado(em_perigo) <- !searchSigns.

+!randomWalk /*: belief pos */ <- moveRandom.
+!searchSigns <- getEvacDirection; !run. 

+!start : true <- .print("hello world."); .wait(3000); !start.
