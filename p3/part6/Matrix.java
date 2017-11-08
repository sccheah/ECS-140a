class Matrix extends Sequence
{
    Sequence seq;
    int rowsize, colsize;
    
    // constructor for creating a matrix of specific number of rows and columns
    Matrix(int rowsize, int colsize)
    {
        // sets the rowsize and colsize in the object
        this.rowsize = rowsize;
        this.colsize = colsize;
        
        seq = new Sequence();
        
        // initialize the matrix to all 0's
        for (int i = 0; i < (rowsize * colsize); i++)
        {
            // create a new MyInteger object, initialize it to 0, and add it to the sequence
            MyInteger init_int = new MyInteger();
            init_int.Set(0);
            seq.add(init_int, i);
        }
    }
    
    // set the value of an element
    void Set(int rowsize, int colsize, int value)
    {
        // find the index by multiplying the desired row and the column size
        int index = rowsize * this.colsize;
        
        // take into account the column offset
        index = index + colsize;
        Sequence node = seq;
        
        // iterate to the desired index
        for (int i = 0; i < index; i++)
        {
            node = node.next;
        }
        
        // cast seqeunce to MyInteger and set the value
        ((MyInteger)node.content).Set(value);
    }
    
    // get the value of an element
    int Get(int rowsize, int colsize)
    {
        // find the index by multiplying the desired row and the column size
        int index = rowsize * this.colsize;
        
        // take into account the column offset
        index = index + colsize;
        
        Sequence node = seq;
        
        //iterate to desired index
        for (int i = 0; i < index; i++)
        {
            node = node.next;
        }
        
        // return the value at the desired index
        return ((MyInteger)node.content).Get();
    }
    
    // return the sum of two matrices: mat & this
    Matrix Sum(Matrix mat)
    {
        // create a new instance of Matrix
        Matrix sum_mat = new Matrix(rowsize, colsize);
        Sequence this_node = seq;
        Sequence other_node = mat.seq;
        int sum = 0;
        
        // check the dimensions
        if (rowsize != mat.rowsize || colsize != mat.colsize)
        {
            System.out.print("m1 + m2 = Matrix dimensions incompatible for Sum");
            System.exit(1);
        }
        
        // iterate through the entire sequence
        for (int i = 0; i < (rowsize * colsize); i++)
        {
            MyInteger temp = new MyInteger();
            sum = 0;
            
            // add this matrix sequence with the other matrix sequence
            sum = ((MyInteger)this_node.content).Get() + ((MyInteger)other_node.content).Get();
            
            // go to the next sequence
            this_node = this_node.next;
            other_node = other_node.next;

            // set the sum into temp MyInteger and add the sequence into the sum_matrix
            temp.Set(sum);
            sum_mat.seq.add(temp, i);
        }
        
        return sum_mat;
    }
    
    // return the product of two matricies: mat & this
    Matrix Product(Matrix mat)
    {
        // check matrix dimensions
        if (colsize != mat.rowsize)
        {
            System.out.println("Matrix dimensions incompatible for Product");
            System.exit(1);
        }
        
        // takes the first matrix's rowsize and the second matrix's colsize
        Matrix mult_mat = new Matrix(rowsize, mat.colsize);
        int val = 0;
        
        // iterate this rowsize
        for (int i = 0; i < rowsize; i++)
        {
            // iterate other colsize
            for (int j = 0; j < mat.colsize; j++)
            {
                val = 0;
                
                // iterate other rowsize
                for (int k = 0; k < mat.rowsize; k++)
                {
                    // performs the matrix multiplication
                    val = mult_mat.Get(i, j) + this.Get(i, k) * mat.Get(k, j);
                    
                    mult_mat.Set(i, j, val);
                }
            }
        }
        
        return mult_mat;
    }
    
    // print the elements of matrix
    public void Print()
    {
        // create an iterator sequence
        Sequence seq = this.seq;
        
        // iterate through the rows
        for (int i = 0; i < rowsize; i++)
        {
            System.out.print("[ ");
            
            // iterate through the columns
            for (int j = 0; j < colsize; j++)
            {
                System.out.print(((MyInteger)seq.content).Get());
                System.out.print(" ");
                
                seq = seq.next;
            }
            System.out.println("]");
        }
    }
}















