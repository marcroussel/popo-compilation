# Cours Blocs & variables

`gennode()` effectue un parcours en profondeur.
La distinction entre une instruction et une expression est vraiment fine.

**Conditionnelles (if else)**

**Version instruction**

```java
if (a == 3) {
	// ... 
}
else {
	// ...
}
```

**Version expression**

```java
// Ça c'est une expression, dans la mesure où ce bloc if renvoie une valeur
if (a == 3) {
	7;
}
else {
	9;
}
```

**Boucle for**

La boucle for calcule des valeurs à chaque tour de boucle, mais si elle est une expression, elle ne renvoie que la dernière valeur calculée.

**❌Mais nous, on ne va pas faire des expressions pour les boucles et conditionnelles. Ce sont des instructions.**

## **Rappel d’une instruction**

I → E + `;`

Une instruction prend la valeur qui est au sommet de la pile **et la jette** avec `drop` .

## **Debug**

Notre compilateur reconnaîtra l’instruction `debug E;` , qui dit qu’on produit le code qui calcule l’expression, puis on affiche la valeur renvoyée par cette même expression.

Ce sera un nœud reconnaissable, nommé `DEBUG` , contenant une expression.

On pourra l’implémenter ainsi :

```java
Node I() {
	// ...
	if (check(tok_debug)) {
		
	}
	else if ... {
	
	}
	else {
	
	}
```

Et en terme d’instruction msm, on aura juste a renvoyer l’expression `E` , suivi du suffixe `dbg` , sans prefixe.

## **Blocs**

On veut pouvoir définir les **blocs de code,** entre accolades { I* }. Et le bloc sera un sous-arbre, qui aura des enfants avec des **instructions**. J’ai autant d’instructions que je veux, après avoir détecté une accolade ouvrante `{` .

On peut l’implémenter ainsi :

```java
Node I() {

	// Lorsqu'on détecte une parenthèse ouvrante
	if (check(tok_lbrace)) {
		
		// On déclare un nœud de bloc, qui aura pour enfants des instructions
		Node N = new Node(nd_block);
		
		// On analyse toutes les instructions dans le bloc
		// Jusqu'à trouver une parenthèse fermante 
		while(!check(tok_lbrace)) {
		
			// On ajoute un enfant instruction
			add_child(N, I());
		}
		
		return N
	}
}
```

En instructions msm, il y aura juste à rajouter dans la pile les instructions du bloc, sans préfixe, ni suffixe.

## Variables

**Portée lexicale des variables** : La portée variable d’une variable **commence au moment de sa déclaration**, et elle se termine au moment où **son bloc où elle est déclarée** se termine.

```java
{
	int x;
	x = 3;
	
	{
		debug x; // Affiche 3
		int x;
		x = 5;
		debug x; // Affiche 5
		// Sortie du bloc, fin de portée du x = 5
	}
	
	// La valeur de x reprend celle de la portée initiale (ligne 2)
	debug x; // Affiche 3
}
```

Pour stocker les variables, on va faire une

### Table des symboles

Elle doit être accessible à l’analyse syntaxique, sémantique et à Gencode. (Nous on l’utilisera surtout pour l’analyseur sémantique)

Elle nous sert à détecter les **types des variables** et les **identificateurs**. L’analyseur sémantique doit interpréter les **déclarations de variables** et **allouer de la mémoire** pour la variable.

L’analyseur sémantique aura une fonction `declare(ident)` qui allouera de la mémoire pour stocker la variable dont l’identificateur est passé en paramètres. La table des symboles crée une nouvelle boîte, **et elle la renvoie**.

Code de **l’analyseur sémantique**

```java
// J'ai une nouvelle var à rajouter dans la table des symboles, avec cet ident
Symb declare(ident) {
	// Si la var est déjà déclarée -> Erreur fat-ass
	
}

// Cherche dans la table des symboles la var qui a l'identificateur ident
Symb find(ident) {
	// Si la var n'est pas déclarée -> Erreur fat-ass
}

// On rentre dans une portée
// On commence un nouvel environnement. 
// Se déclenche lors de la rencontre avec un nouveau bloc {}
begin() {

} 

// On sort de cette portée
end() {
	
}
```

Comment s’implémente la table des symboles ?

C’est une pile de dictionnaires, ou de table de hashage.

`begin()` → On empile une nouvelle **table de hashage**

`end()` → On dépile la dernière **table de hashage**

`declare()` → Vérifie la présence du symbole dans la hashmap du sommet

Si oui → Erreur

Si non → Nouveau symbole

`find()` → Vérifie la présence du symbole dans la hashmap du sommet

Si oui → Renvoi de ce symbole

Si non → Vérification dans la table en dessous

Si fin de la pile atteint **et aucun symbole trouvé** → Erreur

Chaque variable est un symbole. Chaque variable est dans une **hashmap** et chaque **hasmap** correspond à un **bloc de code**.

Il y aura toujours une première **HashMap** pour le code global. On peut avoir en moyenne 5 blocs, donc 6 HashMaps max dans la pile. Et il y a rarement plus de 30 variables dans chaque bloc.

Ce serait idéal d’avoir trois structures de données :

- Un tableau a avec tous les **symboles**,
- Un tableau b qui pointe tous les sommets des **HashMaps** de **symboles**, pour indiquer où se délimitent chaque bloc, dans a
- Un élément c qui pointe vers le dernier bloc de b

Cette structure permet de réduire la complexité algorithmique

`find()` aura une complexité linéaire, au lieu de constante