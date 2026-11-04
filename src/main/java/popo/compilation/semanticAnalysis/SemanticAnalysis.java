package popo.compilation.semanticAnalysis;

import popo.compilation.debug.DebugLogger;
import popo.compilation.node.Node;
import popo.compilation.node.ValidNode;
import popo.compilation.symbol.Symbol;
import popo.compilation.symbol.SymbolTable;
import popo.compilation.symbol.ValidSymbol;
import popo.compilation.syntaxAnalysis.SyntaxAnalysis;

public class SemanticAnalysis {

    // TODO : À voir si c'est mieux de le convertir en non-static
    public static int nbVar;

    public SyntaxAnalysis syntaxAnalysis;

    public SemanticAnalysis(String sourceFileName) {
        this.syntaxAnalysis = new SyntaxAnalysis(sourceFileName);
    }

    /**
     * Réalise l'analyse sémantique de l'arbre, nœud par nœud
     * @param N la racine de l'arbre à analyser
     */
    public void semnode(Node N) {

        switch(N.type) {
            case 18: // BLOCK

                // On commence une nouvelle portée de variables
                SymbolTable.begin();

                // On parcourt semnode sur ses enfants
                for (Node c : N.children) {
                    semnode(c);
                }

                // On termine cette portée
                SymbolTable.end();
                break;

            case 19: // DECLARATION
                // On récupère le symbole créé lors de la déclaration de la variable
                Symbol symbolToDeclare = SymbolTable.declare(N.Ident);

                // Puisqu'on crée cette variable, on va lui allouer une case dans la Pile
                // Donc on lui crée un index
                symbolToDeclare.index = nbVar;
                nbVar++; // On incrémente le numéro de la variable
                symbolToDeclare.type = ValidSymbol.ValidTypes.get("VARIABLE");
                break;

            case 20: // REFERENCE
                // On cherche le symbole correspondant à cet identificateur
                Symbol symbolToFind = SymbolTable.find(N.Ident);

                // Si le type de symbole n'est pas une variable, on lève une erreur
                if (symbolToFind.type != ValidSymbol.ValidTypes.get("VARIABLE")) {
                    throw new SemanticException("Cannot assign value to non-reference variable, at line: " + N.lineNumber);
                }

                N.index = symbolToFind.index; // On transfère l'index de ce symbole sur la Pile, dans ce nœud
                break;

            case 12: // ASSIGN
                if (N.children.getFirst().type != ValidNode.ValidTypes.get("REFERENCE")) {
                    throw new SemanticException("Cannot assign value to non-reference variable, at line: " + N.lineNumber);
                }

                // Pour chaque enfant on fait semnode()
                for (Node c : N.children) {
                    semnode(c);
                }
                break;

            default:
                // On parcourt semnode sur tous ses enfants
                for (Node c : N.children) {
                    semnode(c);
                }
                break;
        }
    }

    public Node AnaSem() {
        Node N = this.syntaxAnalysis.AnaSyntax(); // Génération de l'arbre d'analyse syntaxique
        semnode(N);
        return N;
    }
}
