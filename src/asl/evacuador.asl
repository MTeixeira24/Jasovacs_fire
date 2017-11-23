// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
pos(10,10).
exit_location(15,0).
/*knowExit.
exit_direction(1).*/

/* Initial goals */

//!start.
!walkto.

/* Plans */

/*Agente esta salvo e é removido*/
+!walkto : pos(X,Y) & exit_location(EX, EY) & X = EX & Y = EY <- 
	.print("I am safe"); .my_name(N);.kill_agent(N).
/* Obter direcção para mover*/											
+!walkto : pos(X,Y) & exit_location(EX, EY) <- .wait(500);.print("Walking");jia.get_direction(X, Y, EX, EY, D); do(D); !walkto;.
//+!wander : true <- randomwalk; .wait(500); !wander.  
