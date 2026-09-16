/* Fichier de test pour l'analyse lexicale du C--
 * Contient un maximum de tokens differents :
 * mots-cles, identificateurs, litteraux, operateurs, ponctuation, commentaires
 */

// Declaration de constantes et variables globales
int compteur = 0;
float pi = 3.14159;
char lettre = 'a';
int tableau[10];

// Fonction avec plusieurs types de parametres
int addition(int a, int b) {
    return a + b;
}

float moyenne(float x, float y) {
    return (x + y) / 2.0;
}

void afficherResultat(int valeur) {
    print(valeur);
}

int main() {
    int i;
    int somme = 0;
    float ratio;

    // Boucle for classique
    for (i = 0; i < 10; i = i + 1) {
        somme = somme + i;
    }

    // Boucle while avec operateurs de comparaison
    while (compteur <= 100) {
        compteur = compteur * 2;
        if (compteur == 64) {
            break;
        } else if (compteur != 32) {
            continue;
        }
    }

    // Operateurs logiques et relationnels
    if (somme > 0 && compteur >= 10 || !(somme == 0)) {
        ratio = somme / (float) compteur;
    }

    // Operateurs arithmetiques et affectations composees
    somme += 1;
    somme -= 1;
    somme *= 2;
    somme /= 2;
    somme %= 3;

    // Ponctuation diverse et tableau
    tableau[0] = somme;
    tableau[1] = tableau[0] + 1;

    afficherResultat(addition(somme, compteur));

    return 0;
}
