package popo.compilation.SyntaxAcceptor;

import java.util.ArrayList;

import popo.compilation.lexicalAnalysis.Token;

public class SyntaxAnalysis {
    
    public boolean Accept(ArrayList<Token> tokens) {
        System.out.println("\n========== LEXICAL ACCEPTANCE START ==========\n");
        SyntaxAcceptor lex = new SyntaxAcceptor(tokens);
        
        boolean accepted = lex.acceptWhile();
        System.out.println("acceptWhile result: " + accepted);
        
        accepted = accepted && lex.acceptIf();
        System.out.println("acceptIf result: " + accepted);
        
        accepted = accepted && lex.acceptFor();
        System.out.println("acceptFor result: " + accepted);
        
        accepted = accepted && lex.acceptDo();
        System.out.println("acceptDo result: " + accepted);
        
        accepted = accepted && lex.acceptReturn();
        System.out.println("acceptReturn result: " + accepted);
        
        accepted = accepted && lex.acceptInt();
        System.out.println("acceptInt result: " + accepted);
        
        accepted = accepted && lex.acceptVoid();
        System.out.println("acceptVoid result: " + accepted);
        
        // accepted = accepted && lex.acceptBreak();
        // System.out.println("acceptBreak result: " + accepted);
        
        // accepted = accepted && lex.acceptContinue();
        // System.out.println("acceptContinue result: " + accepted);
        
        System.out.println("\n========== LEXICAL ACCEPTANCE END ==========");
        System.out.println("Global result: " + accepted + "\n");
        
        return accepted;
    }
}
