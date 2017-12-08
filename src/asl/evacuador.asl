// Agente evacuador básico

/* Initial beliefs and rules */

//saude(boa).
//estado(fora_perigo).
knowExit(no).
seen_exit_sign(no).
/*
 * Percepts that are added
 * exit_location(xcoord, ycoord)
 * seen_exit_sign(yes)
 * knowExit(yes)
 */

/* Initial goals */

//!start.
!getPosition. //Obter posição no mundo 
!see.
!warn.

/* Plans */

/*COMMUNICATION */
+!warn(_,_,[]) : true <- !warn.
+!warn(X,Y,[L|Ls]) : true <- .wait(200); .print("Warning",L,"of danger");.send(L, tell, cell(X,Y,danger)); !warn(X,Y,Ls).
+!warn : cell(X,Y,danger) <- .wait(200); .findall(A, cell(_,_,agent,A),L);.drop_desire(warn);!warn(X,Y,L).
+!warn : true <- .wait(200); !warn.
/*SIGHT*/

+!see : knowExit(yes) & cell(EX,EY,exit) <- set_exit_location(EX,EY); .wait(250); !see.
+!see : seen_exit_sign(yes) & pos(AX,AY) <- .findall(cell(CX,CY,D),cell(CX,CY,exit_sign,D), L); jia.getNearestSign(AX,AY,L, ND);
								jia.get_sign_point(AX,AY,ND,EX,EY); .abolish(exit_location(_,_));set_exit_location(EX,EY);.wait(250); !see.
+!see : cell(X,Y,danger) <- .print("I don't know where the exit is! Fleeing from danger for now"); 
							jia.get_oposite_point(X, Y, EX, EY, RX, RY);set_exit_location(RX, RY);.wait(250).
+!see : true <- !see.

/*********/
/*Objectivos de fugir ao ser alertado para perigo*/
/*O agente entrou em contacto com o fogo*/
+!walkto : pos(X,Y) & cell(X,Y,danger) <- .my_name(N); .print("I died while running from danger"); register_death;.kill_agent(N).
/*Se o agente localização a porta de saída então ele sai*/
+!walkto : pos(X,Y) & exit_location(X, Y) & knowExit(yes) <-
	.print("I am safe"); exit;.my_name(N);.kill_agent(N).
/*Se o agente chegou ao local que acreditava ser a saída mas não encontra nenhuma porta de saída*/
+!walkto : pos(X,Y) & exit_location(X, Y) <- 
	.print("I can't find an exit!!'"); .abolish(exit_location(_,_)); !wander.
/*Depois do agente calcular uma possível rota de fuga irá-se dirigir para lá*/									
+!walkto : pos(X,Y) & exit_location(EX, EY) <- .wait(250);.print("Walking");jia.get_direction(X, Y, EX, EY, D); .print(D);do(D); !walkto;.

//Não sei de nada, volto a caminhar
+!walkto : true <- .drop_intention(walkto); !wander.

//+!wander : true <- randomwalk; .wait(500); !wander.  
+!wander : pos(X,Y) & exit_location(X, Y) & knowExit(yes) <- .print("I decide to leave the building"); exit;.my_name(N);.kill_agent(N).
+!wander : pos(X,Y) & cell(X,Y,danger)<- .my_name(N); .print("I am dead, did not see it coming"); register_death;.kill_agent(N).
+!wander : cell(X, Y, danger) <- .print("I saw danger, running"); !walkto.
+!wander : true <- randomwalk; .wait(250); !wander.

+!getPosition : true <- agentGetPosition; !wander.

+end_of_simulation : true <-
	.drop_desire(walkto);
	.abolish(knowExit(_));
	.abolish(pos(_,_));
	.abolish(cell(_,_,_));
	.abolish(cell(_,_,_,_));
	.abolish(exit_location(_,_));
	.abolish(seen_exit_sign(_));
	!getPosition.
