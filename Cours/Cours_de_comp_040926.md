# Cours de Compilation

Dommage on ne fera pas de switch, mais faudra se préparer à manipuler des **pointeurs !**

**Bootstraping :** écriture de compilateur d’un langage dans ce même langage. Genre le compilateur Rust est écrit initialement en C, puis a été écrit en Rust.

On ne cherche pas l’efficacité, ni la propreté du code, on veut juste que du binaire fonctionnel soit produit. **En revanche !!!** On cherche la **fiabilité !**

Et apparemment, on ne va pas utiliser les types. On aura besoin que d’entiers dans notre langage. On enlèvera les opérateurs + + et - - (parce qu’apparemment c’est chiant à programmer).

Et en compilation, on a que le projet à faire ! Pas d’examen (_woahuuu_ 🥳)

On va compiler pour une machine des années 70 💀

# Architecture d’un compilateur

Code source (en C - - ), une chaîne de caractère géante

⬇️

**Analyse lexicale** (_transformation de chaînes en tokens_)

```c
if (a == 3)
```

Liste de tokens : `if` , `(` , variable `a` , etc…

⬇️

**Analyse syntaxique** → Construction d’un arbre

```
				conditions (if)
					/           \
		comparateur (==)	 ...
		/              \
	ref (a)		      const (3)
```

**Exemple d’erreur :**

```c
int main() {
	int x;
	3 = 5 + x; // Erreur de syntaxe (*à vérifier*)
```

⬇️

**Analyse sémantique** → Ensemble de vérification de validité des opérations

**Exemple d’erreur :**

```c
int main() {
	int x, y, x; // Erreur de sémantique : deux mêmes variables avec le même nom
```

⬇️

**Gencode**

⬇️

**Binaire de sortie**

✅ Terminado

# Analyse lexicale

On peut stocker tous les tokens du langage dans un `enum` …

```c
enum {
	tok_if;
	tok_plus;
	tok_const;
	tok_ident;
	// ...
}
```

… definis comme ceci :

```c
struct Token {
	int type;
	int valeur;
	string ident;
	int ligne; // OPT : pour le dev quand il aura une erreur
	int colonne; // OPT
}
```

Il y a un token spécial : `EOS` . Chaque code source se termine par une infinité de `EOS` , donc facile pour se repérer.

**On a donc comme éléments à reconnaître :**

`EOS`

constantes, identificateurs…

Les **Opérateurs :** +, -, _, / et % pour le modulo, _ et & pour les pointeurs

Les **Comparateurs :** <, >, ≤, ≥, ==, !, etc… et même = pour les affectations

Les **portes logiques** : &&, ||, !

Les **“repérages*”* :** (, ), [, ], {, }, ; et même la virgule ,

Les **mots-clés :** `if`, `else`, `for`, `while`, `do`, `int`, `void`, `continue`, `break`, `return`

// _Il y a une partie que j’ai raté, il faudra compléter_

On début ce code avec deux tokens (_un peu comme si c’était des vars glob_):

- Token courant
- Token last

```c
void init(code); // Je veux débuter l'analyse lexicale de ce code.

// Normalement, check va être gigantesque
void next() {
	last = courant;
	sauter_les_espaces(); // ouais tkt elle est définie
	c = prochain

	switch(c) {
		case '+' :
			courant.type = tok_plus; // tok_plus qui fait partie de l'enum plus haut ↑

		//... la ^m pour tous les chars
	}

	if (estChiffre(c)) {
		tok_const
	}

	if (estLettre(c)) {
		id = lineId(c)
		...
	}
};

int check(int type) {
	if (courant.type == type) {
		next();
		return true;
	}
	return false;
}

void accept(int type);
```
