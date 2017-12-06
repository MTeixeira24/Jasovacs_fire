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

/* Plans */


+!getPosition : true <- agentGetPosition; !wander.


/*SIGHT PROCESSING NEW*/

+!see : knowExit(yes) & cell(EX,EY,exit) <- set_exit_location(EX,EY); .wait(250); !see.
+!see : seen_exit_sign(yes) & pos(AX,AY) <- .findall(cell(CX,CY,D),cell(CX,CY,exit_sign,D), L); jia.getNearestSign(AX,AY,L, ND);
								jia.get_sign_point(AX,AY,ND,EX,EY); .abolish(exit_location(_,_));set_exit_location(EX,EY);.wait(250); !see.
+!see : cell(X,Y,danger) <- .print("I don't know where the exit is! Fleeing from danger for now"); 
							jia.get_oposite_point(X, Y, EX, EY, RX, RY);set_exit_location(RX, RY);.wait(250).
+!see : true <- !see.

/*********/


/*Cada agente irá estar sempre atento ao que esta ao seu redor. As intenções de olhar são seguidas em simultaneo com as de mover*/
+!see_danger : cell(X, Y, danger) <-.wait(300); .print("I see danger at ",X," ",Y); !see_sign.
+!see_danger : true <- .wait(300);!see_sign.
/*Se o agente já localizou a saída não precisa de olhar para sinais de saida */
+!see_sign : cell(_, _, exit_sign, _) & knowExit(yes) <- !see_exit.
+!see_sign : cell(X, Y, exit_sign, D) & pos(AX, AY) <- .print("I spot an exit sign at ",X," ",Y," pointing ",D);
										 jia.get_sign_point(AX,AY,D,EX,EY); .abolish(exit_location(_,_));
										 set_exit_location(EX,EY); !see_exit.
+!see_sign : true <- !see_exit.
+!see_exit : cell(X,Y,exit) <- .print("I spotted the exit!"); .abolish(knowExit(_));.abolish(exit_location(_,_));set_exit_location(X,Y); .wait(50).
+!see_exit : true <- .wait(50).

-!see_danger <- !see_danger.

/*Objectivos de fugir ao ser alertado para perigo*/
/*O agente entrou em contacto com o fogo*/
+!walkto : pos(X,Y) & cell(X,Y,danger) <- .my_name(N); .print("I died while running from danger"); .kill_agent(N).
/*Se o agente localização a porta de saída então ele sai*/
+!walkto : pos(X,Y) & exit_location(X, Y) & knowExit(yes) <-
	.print("I am safe"); exit;.my_name(N);.kill_agent(N).
/*Se o agente chegou ao local que acreditava ser a saída mas não encontra nenhuma porta de saída*/
+!walkto : pos(X,Y) & exit_location(X, Y) <- 
	.print("I can't find an exit!!'"); .abolish(exit_location(_,_)); !wander.
/*Depois do agente calcular uma possível rota de fuga irá-se dirigir para lá*/									
+!walkto : pos(X,Y) & exit_location(EX, EY) <- .wait(250);.print("Walking");jia.get_direction(X, Y, EX, EY, D); .print(D);do(D); !walkto;.
/*Se o agente ter visto um sinal de saída, usar essa informação para calcular uma rota*/
/*+!walkto: pos(X,Y) & seen_exit_sign(yes) <- .findall(dir(D,Dist), cell(CX,CY,exit_sign,D) & Dist = ((CX-X)+(CY-Y)),L);.findall(Dists,dir(_,Dists),LDs);
											.min(LDs,MinDs);dir(MinD, MinDs);jia.get_sign_point(X,Y,MinD,EX,EY); set_exit_location(EX,EY); !walkto. */
/*Em ignorancia da saída o melhor que um agente pode fazer é fugir para o lado oposto do perigo*/
/* +!walkto: pos(X,Y) & cell(EX, EY, danger) <- 
	.wait(250); .print("I don't know where the exit is! Fleeing from danger for now"); 
	jia.get_oposite_point(X, Y, EX, EY, RX, RY);
	set_exit_location(RX, RY); !walkto.*/
//Não sei de nada, volto a caminhar
+!walkto : true <- .drop_intention(walkto); !wander.

//+!wander : true <- randomwalk; .wait(500); !wander.  
+!wander : pos(X,Y) & exit_location(X, Y) & knowExit(yes) <- .print("I decide to leave the building"); exit;.my_name(N);.kill_agent(N).
+!wander : pos(X,Y) & cell(X,Y,danger)<- .my_name(N); .print("I am dead, did not see it coming"); .kill_agent(N).
+!wander : cell(X, Y, danger) <- .print("I saw danger, running"); !walkto.
+!wander : true <- randomwalk; .wait(250); !wander.
