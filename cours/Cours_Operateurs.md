# Cours opérateurs

On aura aussi des opérations de préfixe (par exemple - ou !) et suffixe :

**Expression :** E → P | E [opé] E

**Préfixe :** P → **-S | !S | S** ou alors **-P | !P | S** pour prendre en compte tous les préfixes (par exemple : double !! →  `!!a;` → Les valeurs = 0 restent 0, et les valeurs ≠ 0 restent 1)

![Screenshot 2026-09-29 at 08.48.01.png](Cours%20de%20Compilation/Screenshot_2026-09-29_at_08.48.01.png)

**Suffixe :** S → A

**Atome : A →** Nombre | ( E )

**Code de la fonction P()** 

```java
Node P() {

	// Cas où l'on détectera un moins unaire
	if (check(ValidTokens.MINUS)) {
		Node p = P();
		return node_1(nd_moins_un, p);
	}
	
	// Même chose pour le token Not
	
	// Cas pour un chiffre
	Node s = S();
	
	return s;
}
```

**Attention ! Il faut penser aux priorités de calcul !**

**`a = b = c`** ne s’exécute pas dans le même ordre que `a - b - c`

![Screenshot 2026-09-29 at 09.14.43.png](Cours%20de%20Compilation/Screenshot_2026-09-29_at_09.14.43.png)

Pour un calcul avec plusieurs opérateurs comme : `1 * 2 + 3`

![Screenshot 2026-09-29 at 09.22.11.png](Cours%20de%20Compilation/Screenshot_2026-09-29_at_09.22.11.png)

Code de E ()

```java
public Node E() {
	Node a1 = P();
	
	while (estOpeBinaire(currentToken)) {
		int op = currentToken.type;
		Node a2 = P();
		a1 = node_2(op2node(op), a1, a2);
	}
	
	return a1;
}
```

En C, les opérateurs prioritaires sont : `*` `/` `%` , puis `+`  `-` 

Deuxième version du code de `E()`, où l’on va rajouter `EE()` , qui va chercher des opérateurs plus prioritaires 

```java
public Node E() {
	Node a1 = P();
	
	while (estOpeBinaire(currentToken)) {
		int op = currentToken.type;
		Node a2 = EE();
		a1 = node_2(op2node(op), a1, a2);
	}
	
	return a1;
}
```

On va associer chaque token à une priorité, et à un type de nœud :

```java
// Dictionnaires à build
tok_mul = {prio = 7, nd = nd_mult}
tok_add = {prio = 6, nd = nd_addit}

// Fonction EE, avec pmin qui est la priorité minimale
// Va chercher la plus grande expression avec les opés avec priorité >= pmin
Node EE(int pmin) {
	Node a1 = P();
	while (OP[courant.type] != null) {
		op = OP[courant.type];
		if (op.hasPrio(pmin)) {
			break;
		}
		next();
		Node a2 = EE(op.prio++); // On cherche des opérateurs plus prioritaires que l'opérateur actuel
		a1 = node_2(op.nd, a1, a2); // On crée un noeud de l'opérateur avec comme enfants a1 et a2
	}
	return a1;
}
```

Limite on pourrait écrire le code de `E()`  comme ceci : 

```java
Node E() {
	return EE(0);
}
```

Liste des opérateurs et leurs priorités :

| **Niveau priorité** | **Opérateurs** | **Associativité** |
| --- | --- | --- |
| 7 | `*` `/` `%` | 1 |
| 6 | `+` `-` | 1 |
| 5 | `<` `>` `<=` `>=` | 1 |
| 4 | `==` `!=` | 1 |
| 3 | `&&`  | 1 |
| 2 | `||` | 1 |
| 1 | `=` | 0 |

```java
OP[] = {{tok_mul, 7, 1, nd_mul},
				{tok_add, 6, 1, nd_add},
				...
				{tok_equal, 1, 0, nd_affect}}
```

```java
while (1) {
	op = null
	for (int i = 0; i < op.length(); i++) {
		if (OP[i].token == currentToken) {
			op = OP[i];
			break;
		}
	if (!op || op.prio < pmin) {
		break;
	}
}
```

# Machine des années 70

Exécution : `msm < toto.txt`

Exécution avec chaque instruction visible : `msm -d < toto.txt`

Exécution avec pile visible : `msm -b < toto.txt`

Avant chaque début de code, on met `print(".start")` et à la fin, on éteint la machine avec un `print("halt")`. 

Ordre des instructions : 

```java
print(".start")

// Notre boucle

// Pour debug
print("dbg") // Pour que Gencode soit affiché
print("halt")
```

```java
gencode() {
	Node A = AnaSem();
	gennode(A);
}

gennode(Node N) {
	switch(N.type) {
		case nd_const:
			// ...
			// On peut push tout ce qu'on veut, tant que je suis dans la partie supérieure de la pile (ou du code, je sais plus)
			print("pushing ", N.getValue());
		
		//...
		default: 
			throw new GenNodeException();
```

Chaque noeud expression doit déposer sa valeur dans la pile. (je crois)

# Génération de code

```java
SI[] = {nd_add, "", "add"}

gennode(Node N) {
	if( SI[N.type] != NULL) {
		System.out.println(SI[N.type].prefixe)
		for (i = 0; i < N.nbEnfants; i++) {
			gennode(N.enfant[i]);
		}
		print(SI[N.type].suffixe);
	}
	switch (...) {
		...
	}
```

| nd_add |  | “add” |
| --- | --- | --- |
| nd_mul |  | “mul” |
| nd_moins_u | “push 0” | “sub” |
