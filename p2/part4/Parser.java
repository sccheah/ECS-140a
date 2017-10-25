/* *** This file is given as part of the programming assignment. *** */
import java.util.*;

public class Parser {
    
    private Stack<ArrayList<String> > scope = new Stack<ArrayList<String> > ();
    private ArrayList<String> evaluation_list = new ArrayList<String> ();
    private Stack<ArrayList<String> > output_scope = new Stack<ArrayList<String> > ();

    private int block_ctr = 0;
    private Boolean isPrint = false;
    private Boolean isIF = false;
    private Boolean isAssign = false;
    private Boolean isDO = false;
    private Boolean checkGlobal = false;
    private Boolean checkBlock = false;
    private int checkBlockNum = 0;
    
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

    private int found_in_block()
    {
        ArrayList<String> list_temp = new ArrayList<String>();
        Stack<ArrayList<String>> stack_temp = new Stack<ArrayList<String>>();
        Boolean isfound = false; 
        String temp_str = new String();
        int temp_ctr = block_ctr;

        while (!output_scope.empty())
        {
            temp_str = "x_" + tok.string + temp_ctr;

            list_temp = output_scope.pop();
            
            for (int i = 0; i < list_temp.size(); i++)
            {
                if (list_temp.get(i).equals(temp_str))
                {
                    if (checkGlobal)
                        if (temp_ctr == 1)
                            isfound = true;

                    if (checkBlock)
                        if (temp_ctr == (block_ctr - checkBlockNum))
                            isfound = true;

                    if (!checkGlobal && !checkBlock)
                        isfound = true;
                }
            }

            stack_temp.push(list_temp);

            if(isfound)
                break;

            temp_ctr--;
        }

        while(!stack_temp.empty())
        {
            list_temp = stack_temp.pop();
            output_scope.push(list_temp);
        }

        if (isfound)
            return temp_ctr;

        return 1;
    }

    private void program()
    {
        System.out.println("#include \"stdio.h\"");
        System.out.println("int main()");
        System.out.println("{");

        block();
        
        System.out.println("return 0;");
        System.out.println("}");
    }

    private void block()
    {
        ArrayList<String> block = new ArrayList<String>();
        scope.push(block);
        block_ctr++;

        ArrayList<String> output_block = new ArrayList<String>();
        output_scope.push(output_block);
        
        declaration_list();
        statement_list();
        
        scope.pop();
        output_scope.pop();
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
        String temp_str = new String();
        Boolean isRedeclared = false;
        ArrayList<String> input_var = scope.pop();
        ArrayList<String> output_var = output_scope.pop();
        
        mustbe(TK.DECLARE);
        
        if (is(TK.ID))
        {            
            if (input_var.contains(tok.string))
            {
                print_redeclaration_var();
                isRedeclared = true;
            }

            if (!input_var.contains(tok.string))
            {
                isRedeclared = false;
                //System.out.print("int x_" + tok.string);
                System.out.print("int x_" + tok.string + block_ctr);
            }

            temp_str = "x_" + tok.string + block_ctr;
            output_var.add(temp_str);
            input_var.add(tok.string);
            mustbe(TK.ID);
        }

        while( is(TK.COMMA) )
        {
            scan();
            if (is(TK.ID))
            {

                if (input_var.contains(tok.string))
                {
                    print_redeclaration_var();
                    input_var.add(tok.string);

                    mustbe(TK.ID);
                    continue;
                }
                
                if (!isRedeclared)
                    System.out.print(", x_" + tok.string + block_ctr);

                if (isRedeclared)
                {
                    System.out.print("int x_" + tok.string + block_ctr);
                    isRedeclared = false;
                }

                temp_str = "x_" + tok.string + block_ctr;
                output_var.add(temp_str);
                input_var.add(tok.string);
                mustbe(TK.ID);
            }
        }

        System.out.println(";");

        output_scope.push(output_var);
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
        isPrint = true;

        mustbe(TK.PRINT);

        System.out.print("printf(\"");

        expr();

        if(evaluation_list.size() != 0)
        {
            System.out.print("%d\\n\", ");

            for (int i = 0; i < evaluation_list.size(); i++)
            {
                System.out.print(evaluation_list.get(i));
            }

            System.out.println(");");
        }
        else
        {
            System.out.println("\\n\");");
        }

        evaluation_list.clear();
        isPrint = false;
    }
    
    private void assignment()
    {
        isAssign = true;

        ref_id();
        mustbe(TK.ASSIGN);
        System.out.print(" = ");

        expr();
        System.out.println(";");
        isAssign = false;
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

            checkGlobal = true;
        }
        
        // check for at specific block
        if ((hasTilde == true) && (hasNum == true))
        {
            if (is_in_block(blockNum) == false)
                print_not_in_scope(hasNum, blockNum);

            checkBlock = true;
            checkBlockNum = blockNum;
        }

        if (isIF && isPrint == false && isDO == false)
            System.out.print("x_" + tok.string + found_in_block());

        if(isAssign && isPrint == false && isDO == false && isIF == false)
            System.out.print("x_" + tok.string + found_in_block());

        if(isDO && isPrint == false)
            System.out.print("x_" + tok.string + found_in_block());

        if(isPrint)
            evaluation_list.add("x_" + tok.string + found_in_block());
                
        checkBlock = false;
        checkGlobal = false;
        mustbe(TK.ID);
    }
    
    private void DO()
    {
        isDO = true;

        System.out.print("while(");

        mustbe(TK.DO);
        guarded_command();
        mustbe(TK.ENDDO);

        System.out.println("}");
        isDO = false;
    }
    
    private void IF()
    {
        isIF = true;

        System.out.print("if(");
        mustbe(TK.IF);
        guarded_command();

        System.out.println("}");

        while(is(TK.ELSEIF))
        {
            System.out.print("else if(");

            scan();
            guarded_command();
            System.out.println("}");
        }
        
        if(is(TK.ELSE))
        {
            System.out.println("else{");
            scan();
            block();
            System.out.println("}");

        }
        mustbe(TK.ENDIF);

        isIF = false;
    }

    private void guarded_command()
    {
        expr();

        if(isIF && isDO == false)
            System.out.println(" <= 0){");

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

        if(isDO && isPrint == false && isAssign == false)
            System.out.println(" <= 0){");
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
            if (isPrint)
                evaluation_list.add("(");

            scan();
            expr();
            mustbe(TK.RPAREN);

            if (isPrint)
                evaluation_list.add(")");
        }
        else if(is(TK.TILDE) || is(TK.ID))
        {
            ref_id();
        }
        else if(is(TK.NUM))
        {
            if (isPrint)
                evaluation_list.add(tok.string);

            if (isIF && (isAssign == false) && (isPrint == false) && (isDO == false))
                System.out.print(tok.string);
            
            if (isAssign || isDO)
                System.out.print(tok.string);

            mustbe(TK.NUM);
        }
    }
    
    private void addop()
    {
        if(is(TK.PLUS))
        {
            if (isPrint)
                evaluation_list.add(" + ");

            if ((isAssign || isDO || isIF) && isPrint == false)
                System.out.print(" + ");

            mustbe(TK.PLUS);
        }
        
        if(is(TK.MINUS))
        {
            if (isPrint)
                evaluation_list.add(" - ");

            if ((isAssign || isDO || isIF) && isPrint == false)
                System.out.print(" - ");

            mustbe(TK.MINUS);
        }
    }
    
    private void multop()
    {
        if(is(TK.TIMES))
        {
            if (isPrint)
                evaluation_list.add(" * ");

            if ((isAssign || isDO || isIF) && isPrint == false)
                System.out.print(" * ");

            mustbe(TK.TIMES);
        }
        
        if(is(TK.DIVIDE))
        {
            if (isPrint)
                evaluation_list.add(" / ");

            if ((isAssign || isDO || isIF) && isPrint == false)
                System.out.print(" / ");

            mustbe(TK.DIVIDE);
        }
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
