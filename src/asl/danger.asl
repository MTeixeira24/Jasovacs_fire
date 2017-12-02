// Agent danger in project evacuation

/* Initial beliefs and rules */

/* Initial goals */

!spread.

/* Plans */

+!spread : true <- .wait(700);.print("I am spreading.");spread;!spread.
