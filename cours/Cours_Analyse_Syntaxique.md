# Analyse syntaxique

On va représenter tous les tokens en arbres

```java
class Node {
	int type, // -> À voir si on en fait un enum ou une classe ou autre chose 
	int valeur,
	String ident, 
	int line, // Tout comme token
	int nbEnfants,
	ArrayList<Node> enfants[],
}
```

On va rajouter les types de noeuds, au fur et à mesure qu’on en aura besoin. Ils ne seront pas déjà prédéfinis, contrairement à ValidToken.

⚠️ Attention, c’est pas pour autant qu’il faille utiliser une enum pour définir les types de Node.

**Fonctions pour se simplifier la vie**

```java
// Constructeurs de Node
// Tous ces constructeurs sont sans enfants
public Node(int type) {
	//...
	// On peut prendre la ligne du token courant
}

public Node(int type, int ligne) {
	//...
}

// Si c'est une constante avec valeur
public Node(int type, int valeur) {
	//...
}

// Si c'est un identificateur
public Node(int type, String ident) {
	//...
}

// Constructeurs avec enfants
public Node(int type, Node e1) {
	//...
}
public Node(int type, Node e1, Node e2) {
	//...
}

// Fonctions utilitaires 

public void ajouter_enfant(Node p, Node e) {
	//...
}
```

Les noeuds peuvent avoir types suivants :

nd_const

nd_neg

nd_add → Aura deux enfants : les deux valeurs à additionner

etc…

**Comment implémenter ça ?**

```java
main() {
	...
	init() // Askip c'est pas l'analyse lexicale, c'est la conversion de 
				// ton fichier source en string, mais pas sûr
	
	// ... Affichages
	
	while(courant.type != tok_eos) {
		gencode(); // Normalement on devrait avoir l'analyse lexicale et syntaxique dedans
							// Mange les tokens, produit du code et le renvoit
	}
}
```

Voici, à terme, le Gencode, qui produit du code à partir d’un arbre de token.

```java

gencode() {
	Node A = AnaSem(); // Génération de l'arbre, à partir de l'analyseur sémantique 
	// ... À faire @Aujourd'hui (28/09/2026)
}

// Fonction d'analyse sémantique 
Node AnaSem() {
	Node A = AnaSyntax(); // Génération de l'analyse syntaxique
	return A; // (pour le moment)
}

// Fabricant de l'arbre syntaxique
Node AnaSyntax() {
	// ... Grosse machine de fabrication À faire @Aujourd'hui (28/09/2026)
	
	return A;
}
```

C’est le générateur de code qui tire tout vers la fin. C’est la manière la plus simple

**Syntaxes des langages** → Des grammaires : des règles pour écrire des expressions

Exemple de grammaires :

Si j’ai “A”, elle peut être écrite en “aB”

Si j’ai “B”, elle peut être écrite en “bA” ou “ε”.

Avec ces deux règles de grammaire, on peut avoir :

A → aB → abA → abaB → jusqu’à l’inifini… OU aba. On finit la chaîne si on a choisi B = ε

A et B sont des symboles **non-terminaux** car on peut réécrire dessus une autre chaîne.

a et b sont des symboles **terminaux**, car une fois qu’ils sont écrits, ils sont définitivement dans la chaîne.

**Tout autre exemple plus appliqué :**

A → 0 | 1 | 2 ….. | 9

B → A + A | A - A | A → Bref, les opérations sur les chiffres définis dans A

mais si on a :

B → A + B | A - B | A → B peut très bien être un A, et il est donc possible de faire plusieurs opérations genre A + A2 + B, ou A + A2 - A3, etc….

**Expression ≠ Instruction**

Expression a une valeur

Instruction n’a pas de valeur. C’est “fait ça, je m’en fous de la valeur que tu produis”.

En C, les boucles, for, while, sont des **instructions**, mais les **opérateurs** (tout ce qui est mathématique) ****sont des **expressions.**

Break, continue → instruction

Déclaration de variable (`int value;`) → instruction

Return → instruction

`1 + 2` est **une expression** car c’est une valeur que l’on peut affecter quelque part.

`a = 1 + 2`  est aussi **une expression** car l’attribution de valeur stocke une valeur **ET renvoie cette même valeur**

→ Donc `b = a = 1 + 2`  **est autorisé !** Et b vaudra 3.

**EN REVANCHE !** Si tu mets un **point-virgule** après cette attribution de valeur (`a = 1 + 2**;**`) alors ça devient une **instruction**.

**Descente récursive**

Fonctions (Fonction F) → Instructions (Fonction I)

Instructions (I) → Expressions (E) + ‘;’ *(Token point-virgule)*

Expressions (E) → Atomes (A)

Atomes (A) → *Constantes (Token constante)* | ‘(’ E ‘)’  → Est utile si tu as 1 + (2 - 3)

→ On aura A (Atome), qui s’appellera elle-même, ou qui appellera E (Expression)

Chaque règle va se transformer en une fonction ! Par exemple :

```java
Node F() {
	return I(); // Pour le moment, on s'arrête là, on la complètera plus tard
}
```

Pour A, on est sûrs que, quand elle sera appelée, elle devra s’attendre à une constante ou un token ‘(’, une Expression et un token ‘)’. La fonction A, elle me garantit que ces tokens seront mangés, puis qu’elle renverra l’arbre correspondant. Sinon → **Exception fatale**

Pour I :

```java
Node I() {
	Node e = E(); // On doit avoir un arbre avec les expressions consommées
	accept(token_point_virgule) // On doit s'assurer que le point-virgule existe, sinon erreur
	return e;
}
```

Pour A (qui peut avoir deux possibilités) :

```java
Node A() {

	// On consomme token si le token vérifié est bien une constante ou une parenthèse ouvrante
	// Mais si ça renvoie false, on consomme pas 
	
	// Constante (A)
	if (check(tok_const)) {
		return node_v(nd_const, lastToken.getValue()); // On doit dans tous les cas renvoyer un arbre
	}
	
	// ( E )
	else if (check(tok_parenthese_gauche)) {
		Node e = E();
		accept(tok_parenthese_droite); // On vérifie qu'après récup de l'expression, on a bien une paranthèse fermante
		return e;
	}
	
	else {
		throw new CompilationException();
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