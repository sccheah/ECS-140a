/* *** This file is given as part of the programming assignment. *** */
import java.util.*;

public class Parser {
    
    private Stack<ArrayList<String> > scope = new Stack<ArrayList<String> > ();
    //private Stack<Set<String> > scope = new Stack<Set<String> > ();
    private int block_ctr = 0;
    
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan()
    {
        tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner)
    {
        this.scanner = scanner;
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
    }

    private void program()
    {
        ///////////////////////////////////////////////////////
        System.out.println("#include \"stdio.h\"");
        System.out.println("int main()");
        System.out.println("{");
        ///////////////////////////////////////////////////////

        block();
        
        ///////////////////////////////////////////////////////
        System.out.println("return 0;");
        System.out.println("}");
        ////////////////////////////////////////////////////////
    }

    private void block()
    {
        ArrayList<String> block = new ArrayList<String>();
        scope.push(block);
        block_ctr++;
        
        declaration_list();
        statement_list();
        
        scope.pop();
        block_ctr--;
    }

    private void declaration_list()
    {
        while( is(TK.DECLARE) )
        {
            declaration();
        }
    }
    
    private void statement_list()
    {
        while(is(TK.TILDE) || is(TK.ID) || is(TK.PRINT) || is(TK.DO) || is(TK.IF))
        {
            statement();
        }
    }

    private void declaration()
    {
        ArrayList<String> input_var = scope.pop();
        //Set<String> input_var = scope.pop();
        
        mustbe(TK.DECLARE);
        
        if (is(TK.ID))
        {
            ////////////
            if (input_var.contains(tok.string))
                print_redeclaration_var();
            ////////////
            input_var.add(tok.string);
            mustbe(TK.ID);
        }
        while( is(TK.COMMA) )
        {
            scan();
            if (is(TK.ID))
            {
                if (input_var.contains(tok.string))
                    print_redeclaration_var();
                
                input_var.add(tok.string);
                mustbe(TK.ID);
            }
        }

        scope.push(input_var);
    }

    private void statement()
    {
        if(is(TK.TILDE) || is(TK.ID))
        {
            assignment();
        }
        else if(is(TK.PRINT))
        {
            print();
        }
        else if(is(TK.DO))
        {
            DO();
        }
        else if(is(TK.IF))
        {
            IF();
        }
    }
    
    private void print()
    {
        mustbe(TK.PRINT);

        ///////////////////////////////////////////////////////
        System.out.print("printf(\"");
        ///////////////////////////////////////////////////////

        expr();

        ///////////////////////////////////////////////////////
        System.out.println("\\n\");");
        ///////////////////////////////////////////////////////
    }
    
    private void assignment()
    {
        ref_id();
        mustbe(TK.ASSIGN);

        ///////////////////////////////////////////////////////
        System.out.print(" = ");

        expr();

        System.out.println("");
    }
    
    private Boolean stack_search()
    {
        Stack<ArrayList<String> > temp_stack = new Stack<ArrayList<String> > ();
        ArrayList<String> temp_array = new ArrayList<String>();
        Boolean isfound = false;
        
        //search entire stack if undeclared variable
        while(!scope.empty())
        {
            temp_array = scope.pop();
            
            for (int i = 0; i < temp_array.size(); i++)
            {
                if(temp_array.get(i).equals(tok.string))
                    isfound = true;
            }
            
            
            
            temp_stack.push(temp_array);
        }
        
        //put everything back into scope stack
        while (!temp_stack.empty())
        {
            temp_array = temp_stack.pop();
            scope.push(temp_array);
        }
        
        return isfound;
    }
    
    private Boolean is_in_block(int block_number)
    {
        Stack<ArrayList<String> > temp_stack = new Stack<ArrayList<String> > ();
        ArrayList<String> temp_array = new ArrayList<String>();
        Boolean isfound = false;
        
        // move to the block we want
        //for (int i = 0; i < block_number - 1; i++)
        for (int i = 0; i < block_number; i++)
        {
            temp_array = scope.pop();
            temp_stack.push(temp_array);
        }
        
        if (!scope.empty())
        {
            temp_array = scope.pop();
            
            for (int i = 0; i < temp_array.size(); i++)
            {
                if (temp_array.get(i).equals(tok.string))
                    isfound = true;
            }
            
            scope.push(temp_array);
        }
        
        
        // put everything back in scope stack
        //for (int i = 0; i < block_number - 1; i++)
        for (int i = 0; i < block_number; i++)
        {
            temp_array = temp_stack.pop();
            scope.push(temp_array);
        }

        return isfound;
    }
    
    private Boolean is_in_global()
    {
        Stack<ArrayList<String> > temp_stack = new Stack<ArrayList<String> > ();
        ArrayList<String> temp_array = new ArrayList<String>();
        Boolean isfound = false;
        
        // move to the block we want
        for (int i = 0; i < block_ctr - 1; i++)
        {
            temp_array = scope.pop();
            temp_stack.push(temp_array);
        }
        
        if (!scope.empty())
        {
            temp_array = scope.pop();
            
            for (int i = 0; i < temp_array.size(); i++)
            {
                if (temp_array.get(i).equals(tok.string))
                    isfound = true;
            }
            
            scope.push(temp_array);
        }
        
        
        // put everything back in scope stack
        for (int i = 0; i < block_ctr - 1; i++)
        {
            temp_array = temp_stack.pop();
            scope.push(temp_array);
        }
        
        return isfound;
    }
    
    private void ref_id()
    {
        Boolean hasTilde = false;
        Boolean hasNum = false;
        int blockNum = 0;
        
       if(is(TK.TILDE))
       {
           hasTilde = true;
           mustbe(TK.TILDE);
           
           if(is(TK.NUM))
           {
               hasNum = true;
               blockNum = Integer.parseInt(tok.string);
               mustbe(TK.NUM);
           }
       }
        
        // if specified blockNum is greater than blocks currently in the program
        if (hasNum == true)
        {
            if ((blockNum > block_ctr) || (blockNum < 0))
                print_not_in_scope(hasNum, blockNum);
        }
    
        // check to see if variable is declared in any scope
        if ((hasTilde == false) && (hasNum == false))
        {
            if (stack_search() == false)
                print_undeclared_var();
        }
        
        // check for global level
        if ((hasTilde == true) && (hasNum == false))
        {
            if (is_in_global() == false)
                print_not_in_scope(hasNum, block_ctr);
        }
        
        // check for at specific block
        if ((hasTilde == true) && (hasNum == true))
        {
            if (is_in_block(blockNum) == false)
                print_not_in_scope(hasNum, blockNum);
        }
        
        mustbe(TK.ID);
    }
    
    private void DO()
    {
        mustbe(TK.DO);
        guarded_command();
        mustbe(TK.ENDDO);
    }
    
    private void IF()
    {
        ///////////////////////////////////////
        System.out.print("if(");
        ///////////////////////////////////////
        mustbe(TK.IF);
        guarded_command();

        System.out.println("}");

        //guarded_command();

        while(is(TK.ELSEIF))
        {
            System.out.print("else if(");

            scan();
            guarded_command();

            System.out.println("}");

        }
        
        if(is(TK.ELSE))
        {
            System.out.println("else {");

            scan();
            block();

            System.out.println("}");
        }
        mustbe(TK.ENDIF);

        //System.out.println("}");
    }

    private void guarded_command()
    {
        expr();
        System.out.println(" <= 0)");
        System.out.println("{");
        mustbe(TK.THEN);
        block();
    }
    
    private void expr()
    {
        term();
        while(is(TK.PLUS) || is(TK.MINUS))
        {
            addop();
            term();
        }
    }
    
    private void term()
    {
        factor();
        while(is(TK.TIMES) || is(TK.DIVIDE))
        {
            multop();
            factor();
        }
    }
    
    private void factor()
    {
        if(is(TK.LPAREN))
        {
            scan();
            expr();
            mustbe(TK.RPAREN);
        }
        else if(is(TK.TILDE) || is(TK.ID))
        {
            ref_id();
        }
        else if(is(TK.NUM))
        {
            ///////////////////////////////////////////////////////
            System.out.print(tok.string);
            ///////////////////////////////////////////////////////

            mustbe(TK.NUM);
        }
    }
    
    private void addop()
    {
        if(is(TK.PLUS))
            mustbe(TK.PLUS);
        
        if(is(TK.MINUS))
            mustbe(TK.MINUS);
    }
    
    private void multop()
    {
        if(is(TK.TIMES))
            mustbe(TK.TIMES);
        
        if(is(TK.DIVIDE))
            mustbe(TK.DIVIDE);
    }
    
    // is current token what we want?
    private boolean is(TK tk)
    {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk)
    {
        if( tok.kind != tk )
        {
            System.err.println( "mustbe: want " + tk + ", got " + tok);
            parse_error( "missing token (mustbe)" );
        }
        scan();
    }

    private void parse_error(String msg) {
        System.err.println( "can't parse: line " + tok.lineNumber + " " + msg );
        System.exit(1);
    }
    
    private void print_undeclared_var()
    {
        System.err.println(tok.string + " is an undeclared variable on line " + tok.lineNumber);
        System.exit(1);
    }
    
    private void print_redeclaration_var()
    {
        System.err.println("redeclaration of variable " + tok.string);
        //System.exit(1);
    }
    
    private void print_not_in_scope(Boolean hasNum, int blockNum)
    {
        if (hasNum == false)
            System.err.println("no such variable ~" + tok.string + " on line " + tok.lineNumber);
        
        if (hasNum == true)
            System.err.println("no such variable ~" + blockNum + tok.string + " on line " + tok.lineNumber);
        
        System.exit(1);
    }
}
