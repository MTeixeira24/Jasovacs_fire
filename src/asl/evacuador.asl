// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
/*knowExit.
exit_direction(1).*/

/* Initial goals */

!start.
!wander.

/* Plans */

+!wander : true <- randomwalk; .wait(500); !wander.  

+!start : true <- .print("hello world."); .wait(3000); !start.
