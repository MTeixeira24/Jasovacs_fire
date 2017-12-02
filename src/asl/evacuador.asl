// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
exit_location(15,0).
/*knowExit.
exit_direction(1).*/

/* Initial goals */

//!start.
!getPosition. //Obter posição no mundo 
!see.
/* Plans */


+!getPosition : true <- agentGetPosition; !wander.

+!see_sign : cell(X, Y, exit_sign) <- .print("I spot an exit sign at ",X," ",Y);.wait(500); !see_danger.
+!see_sign : true <- !see_danger.
+!see_danger : cell(X, Y, danger) <- .print("I see danger at ",X," ",Y); .wait(500); !see.
+!see_danger : true <- !see.
+!see : true <- do(skip); .wait(500); !see_sign.

/*Agente esta salvo e é removido*/
+!walkto : pos(X,Y) & exit_location(EX, EY) & X = EX & Y = EY <- 
	.print("I am safe"); exit;.my_name(N);.kill_agent(N).
/* Obter direcção para mover*/											
+!walkto : pos(X,Y) & exit_location(EX, EY) <- .wait(500);.print("Walking");jia.get_direction(X, Y, EX, EY, D); do(D); !walkto;.

+!wander : pos(X,Y) & cell(XC,YC,danger) & X=XC & Y=YC <- .my_name(N); .print("I DEAD"); kill_agent(N).
+!wander : true <- randomwalk; .wait(500); !wander.


