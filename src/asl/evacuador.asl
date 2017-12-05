// Agente evacuador básico

/* Initial beliefs and rules */

saude(boa).
estado(fora_perigo).
//exit_location(15,0).
/*knowExit.
exit_direction(1).*/

/* Initial goals */

//!start.
!getPosition. //Obter posição no mundo 
!see_danger.

/* Plans */


+!getPosition : true <- agentGetPosition; !wander.


/*Cada agente irá estar sempre atento ao que esta ao seu redor. As intenções de olhar são seguidas em simultaneo com as de mover*/
+!see_danger : cell(X, Y, danger) <- .print("I see danger at ",X," ",Y); !see_sign.
+!see_danger : true <- !see_sign.
/*Se o agente já localizou a saída não precisa de olhar para sinais de saida */
+!see_sign : cell(_, _, exit_sign, _) & knowExit <- !see_exit.
+!see_sign : cell(X, Y, exit_sign, D) <- .print("I spot an exit sign at ",X," ",Y," pointing ",D);
										 pos(AX, AY); jia.get_sign_point(AX,AY,D,EX,EY); .abolish(exit_location(_,_));
										 exit_location(EX,EY); !see_exit.
+!see_sign : true <- !see_exit.
+!see_exit : cell(X,Y,exit) <- .print("I spotted the exit!"); .abolish(exit_location(_,_));exit_location(X,Y); .wait(250).
+!see_exit : true <- .wait(250).

-!see_danger <- !see_danger.

/*Objectivos de fugir ao ser alertado para perigo*/
/*O agente entrou em contacto com o fogo*/
+!walkto : pos(X,Y) & cell(XC,YC,danger) & X=XC & Y=YC <- .my_name(N); .print("I DEAD"); .kill_agent(N).
/*Se o agente localização a porta de saída então ele sai*/
+!walkto : pos(X,Y) & exit_location(X, Y) & knowExit <-
	.print("I am safe"); exit;.my_name(N);.kill_agent(N).
/*Se o agente chegou ao local que acreditava ser a saída mas não encontra nenhuma porta de saída*/
+!walkto : pos(X,Y) & exit_location(X, Y) <- 
	.print("I can't find an exit!!'"); .abolish(exit_location(_,_)); !wander.
/*Depois do agente calcular uma possível rota de fuga irá-se dirigir para lá*/									
+!walkto : pos(X,Y) & exit_location(EX, EY) <- .wait(250);.print("Walking");jia.get_direction(X, Y, EX, EY, D); .print(D);do(D); !walkto;.
/*Em ignorancia da saída o melhor que um agente pode fazer é fugir para o lado oposto do perigo*/
+!walkto: pos(X,Y) & cell(EX, EY, danger) <- 
	.wait(250); .print("I don't know where the exit is! Fleeing from danger for now"); 
	jia.get_oposite_point(X, Y, EX, EY, RX, RY);
	exit_location(RX, RY); !walkto.

-!walkto <- !walkto.

//+!wander : true <- randomwalk; .wait(500); !wander.  
+!wander : pos(X,Y) & exit_location(X, Y) <- .print("I am safe"); exit;.my_name(N);.kill_agent(N).
+!wander : pos(X,Y) & cell(X,Y,danger)<- .my_name(N); .print("I DEAD"); .kill_agent(N).
+!wander : cell(X, Y, danger) <- .print("I saw danger, running"); !walkto.
+!wander : true <- randomwalk; .wait(250); !wander.
