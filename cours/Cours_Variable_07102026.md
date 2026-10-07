7 octobre 2026 

## Construire des variables

Pour le moment, on ne va se focus que sur des variables int. 

**On doit essentiellement pouvoir faire ça :**

```java
int a, b, c; // Déclaration de variable

a = 5; // Attribution de valeur

c = a + 3; // Affectation et incrémentation

a = b = 0; // Double affectation
```

Il y aura un nœud “déclaration” et un nœud “affectation”. 

Pour le moment, on ne va pas rendre en compte les **initialisations** de type : **`int** a = 0;` 

Si on veut vraiment le faire, dans **l’analyseur syntaxique**, on doit générer un arbre de cette forme :

![Screenshot 2026-10-07 at 08.47.04.png](Cours%20de%20Compilation/Screenshot_2026-10-07_at_08.47.04.png)

En gros, on rajoute un nœud d’affectation de valeur après la déclaration.

Voici les outils qu’on va devoir utiliser :

**Tas**

**C’est la mémoire dynamique du programme.** C’est là où on aura toutes nos variables locale, c’est la mémoire qui peut survivre à l’appel de fonction (*pas sûr de cette phrase*). 

**Pile**

C’est là où on stocke les valeurs pour les calculs. On empile nos variables au fur et à mesure qu’on fait tourner le code. Mais en dessous du sommet de la pile, c’est difficile d’accéder aux valeurs. C’est pour ça que les compilateurs, ils ont un **Stack Pointer**, qui pointe sur le sommet de la pile, et il est possible d’accéder à la valeur de **Stack Pointer - *i*,** avec i la i-ème valeur en dessous du **Stack Pointer**.

On a encore un autre outil : le **Base Pointer**, qui pointe sur le bas de la pile, tout simplement. Et c’est probablement ce qu’on va utiliser parce que c’est beaucoup plus simple. **Base Pointer** est fixe, contrairement à **Stack Pointer**. 

**Et dans le code ?**

La déclaration d’une variable, revient à traduire ça : `int ident (',' ident)*`

On va rajouter un nouveau **nœud** : la déclaration de la variable. 

Et pour les stocker dans un **bloc**, sans avoir la notion de **portée de variable**, on va créer le nœud **séquence**. Il permet d’unifier les déclarations d’une ou plusieurs variables dans **un seul nœud**. 

**Dans l’analyse syntaxique**

```java
// Code de détection d'une déclaration de variable 
if (check(tok_int)) {

	Node N = new Node(nd_seq); // C'est là qu'on utilise le nouveau nœud séquence
	N.addChild(new Node(nd_ident, ident)); // L'idée est de rajouter un nouveau identificateur fils

	accept(tok_ident);
	
	// Dans le cas où on a plusieurs variables à déclarer
	while(!check(tok_semicolumn)) {
		accept(tok_virgule);
		accept(tok_ident);
		
		N.addChild(new Node(nd_ident, ident)); // L'idée est de rajouter un nouveau identificateur fils
	}
}
```

Dans le code généré, **seq** va avoir le même comportement qu’un bloc. (*pas sûr de cette phrase*)

On aura aussi un **noeud référence**, qui va accéder à la valeur de l’identificateur concerné. 

Quand on fait `b = a + 3`, le nœud référence de `a` va récupérer sa valeur avec un `get _` dans la pile, avec `_` la position de la valeur de `a` dans la pile, et y placer une **copie** de cette valeur au sommet de la pile. → ***Tout ça, c’est le travail de l’analyse sémantique.*** 

Dans **l’analyse sémantique** 

```java
void semnode(Node N) {
	switch(N.type) {
		default: 
			// Pour chaque enfant e de N :
				semnode(e); // On passe l'analyseur sémantique sur les enfants de ce nœud
				
		case nd_block:
			// On commence une nouvelle portée de variables
			symbolTable.begin()
			
			// Pour chaque ...
				semnode(e); // On parcourt semnode sur ses enfants
				
			// On termine cette portée
			symbolTable.end() 
			
		case nd_declare:
			
			// On récupère le symbole crée lors de la déclaration de la variable
			S = symbolTable.declare(N.ident); 
			
			// Puisqu'on crée cette variable, on va lui allouer une case dans la Pile
			// Donc on lui crée un index
			S.index = nbvar; // nbvar est une var globale
			nbvar++;
			S.type = SymVariable;
			
		case nd_ref:
			// On cherche le symbole correspondant à cet identificateur
			S = symbolTable.find(N.ident);
			
			// Déjà si le type de symbole n'est pas une variable, on tej une errreur
			if (S.type != SymVariable) {
				throw new Exception(); 
			}
			
			N.index = S.index // On transfère l'index de ce symbole sur la Pile, dans ce nœud
			
	
	Node AnaSem() {
		Node N = this.syntaxAnalysis.AnaSyntax(); // Déjà fait
		nbvar = 0; // -> Ce sera à déclarer en tant que var globale statique 
		semnode(N); // On effectue l'analyse nœud par nœud
		return N;
	}
```

Exemple concret : 

```java
{
	int a, b;
	a = 3;
	b = a + 1;
}

```

Arbre produit par l’analyseur sémantique :

![IMG_3577.HEIC](Cours%20de%20Compilation/IMG_3577.heic)

Si on avait remplacé `seq` par un `block` , on aurait un premier `begin()` (celui de la racine), puis un deuxième `begin()` suivie des déclarations de a et b, puis un `end()` , rendant les symboles de a et b inaccessibles, donc **turbo erreur.** 

**Nœud affectation**

En plus des nœuds **séquence, déclaration, référence(index)**, on a un **quatrième nœud : affect.**

Le nœud a deux enfants : une **ref** et une **expression**. 

**Code à rajouter dans `semnode` :**

```java
void semnode(Node N) {
	switch(N.type) {
	
		// ...
	
		case nd_affect:
			if (N.enfant[0].type != nd_ref) {
				throw new Exception();
			}
			
			// Pour chaque enfant on fait semnode()
	}
}	
```

**Dans la machine,** a=3 → Fait `push 3`, puis `dup`, puis `set N.enfant[0].index`

**Et il y a un détail à ne pas oublier**, c’est qu’après l’affectation, il faut ajouter `drop` pour enlever la merde laissée par l’affectation dans la pile, afin de transformer l’affectation en véritable instruction. Voici le même arbre actualisé : 

![IMG_3578.HEIC](Cours%20de%20Compilation/IMG_3578.heic)

**Code à rajouter dans** `gencode()` :

```java
gencode() {
	N = anasem();
	print("res n", nbvar); // Réserver le nombre de case, 
												// ajoute nbvar au pointeur de pile
	gennode(N);
}
```
