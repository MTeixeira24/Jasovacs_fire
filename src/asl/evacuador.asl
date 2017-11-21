// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
pos(10,10).
/*knowExit.
exit_direction(1).*/

/* Initial goals */

!start.
!walkto.

/* Plans */
											/* Obter direcção para mover para XY */
+!walkto : pos(X,Y) <- .wait(500);.print("Walking");jia.get_direction(X, Y, 15, 0, D); do(D); !walkto;.
//+!wander : true <- randomwalk; .wait(500); !wander.  

+!start : true <- .print("hello world."); .wait(3000); !start.
