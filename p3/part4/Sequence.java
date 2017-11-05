public class Sequence extends Element
{
    public Element content;
    public Sequence next;
    public Sequence() { content = null; next = null; }
    
    // returns beginning of the sequence
    public SequenceIterator begin()
    {
        // initialize the SeqIter object and reference this sequence
        SequenceIterator it = new SequenceIterator();
        it.node = this;
        return it;
    }
    
    // returns one past end of sequence
    public SequenceIterator end()
    {
        // initialize this object and reference this sequence
        SequenceIterator it = new SequenceIterator();
        Sequence node = this;
        
        // iterate to one past end of the sequence
        for (int i = 0; i < length(); i++)
        {
            node = node.next;
        }
        
        // copy the reference into the SeqIter object
        it.node = node;
        
        return it;
    }
    
    public Element first()
    {
        // returns content in this node, which is the head being called
        return content;
    }
    
    public Sequence rest()
    {
        // returns the next node in the sequence
        return next;
    }
    
    public int length()
    {
        Sequence node = this;
        int ctr = 0;
        
        // iterate through sequence and count number of elements
        while (node != null)
        {
            ctr++;
            node = node.next;
        }
        
        return ctr - 1;
    }
    
    public void add(Element elm, int pos)
    {
        Sequence node = this;
        Sequence temp_node = new Sequence();
        
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: add pos < 0 || pos > (length() - 1)");
            System.exit(1);
        }
        
        for (int i = 0; i < pos; i++)
        {
            node = node.next;
        }
        
        // let temp node take over the current object
        temp_node.content = node.content;
        temp_node.next = node.next;
        
        // change current object to take new content and point to temp, since the previous node already points to this
        node.content = elm;
        node.next = temp_node;
    }
    
    public void delete(int pos)
    {
        Sequence node = this;
        
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: delete pos < 0 || pos > (length() - 1)");
            System.exit(1);
        }
        
        // iterate to one before the position, to leave the node we want to deleted unreferenced
        for (int i = 0; i < pos; i++)
        {
            node = node.next;
        }
        
        // move to position, copy next element content and next to replace current node
        if (node.next != null)
        {
            node.content = node.next.content;
            node.next = node.next.next;
        }
        else
            node.next = null;
    }
    
    public void Print()
    {
        // set a temp node to this current object
        Sequence node = this;
        
        System.out.print("[");
        
        // iterate through the sequence
        while (node != null)
        {
            if (node.content != null)
            {
                // if there is something to print, first print a space
                System.out.print(" ");
                // if element is instance of MyChar
                if (node.content instanceof MyChar)
                    ((MyChar)node.content).Print();
                
                // if element is instance of MyInteger
                if (node.content instanceof MyInteger)
                    ((MyInteger)node.content).Print();
                
                // if element is instance of Sequence
                if (node.content instanceof Sequence)
                    ((Sequence)node.content).Print();
            }
            /*else
            {
                node = node.next;
                continue;
            }*/
    
            // iterate to the next element
            node = node.next;
        }
        
        // print a space at the end to space the end bracket
        System.out.print(" ");
        System.out.print("]");
    }
    
    public Element index(int pos)
    {
        Sequence node = this;
        
        // if pos is not in length of sequence object
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: index pos < 0 || pos > (length() - 1)");
            System.exit(1);
        }
        
        // iterate to the pos
        for (int i = 0; i < pos; i++)
        {
            node = node.next;
        }
        
        return node.content;
    }
    
    public Sequence flatten()
    {
        Sequence flat_seq = new Sequence();
        Sequence node = this;
        
        // iterate this Sequence
        while (node != null)
        {
            // if this node's content is type MyChar
            if (node.content instanceof MyChar)
            {
                // reference node.content and add it into the flatten sequence
                MyChar temp_char = (MyChar)node.content;
                flat_seq.add(temp_char, flat_seq.length());
                
                /* Test case solutions don't seem to completely create new objects inside Sequence
                MyChar temp_char = new MyChar();
                temp_char.Set(((MyChar)node.content).Get());
                new_seq.add(temp_char, new_seq.length());
                 */
            }
            
            // if this node's content is type MyInteger
            if (node.content instanceof MyInteger)
            {
                // reference node.content and add it into flatten sequence
                MyInteger temp_int = (MyInteger)node.content;
                flat_seq.add(temp_int, flat_seq.length());
                
                /* Test case solutions don't seem to completely create new objects inside Sequence
                MyInteger temp_int = new MyInteger();
                temp_int.Set(((MyInteger)node.content).Get());
                new_seq.add(temp_int, new_seq.length());
                */
            }
            
            // if this node's content is type Sequence
            if (node.content instanceof Sequence)
            {
                // reference node.content of sequence type and add it into flatten sequence
                Sequence temp_seq = ((Sequence)node.content).flatten();
                
                // iterate through the temp sequence and add the reference to node.content into the flatten sequence
                while (temp_seq != null)
                {
                    flat_seq.add(temp_seq.content, flat_seq.length());
                    temp_seq = temp_seq.next;
                }
                
                /* Test case solutions don't seem to completely create new objects inside Sequence
                Sequence temp_seq = new Sequence();
                temp_seq = ((Sequence)node.content).flatten();
                
                while (temp_seq != null)
                {
                    new_seq.add(temp_seq.content, new_seq.length());
                    temp_seq = temp_seq.next;
                }
                */
            }
            node = node.next;
        }
        
        return flat_seq;
    }
    
    // performs a deep copy
    public Sequence copy()
    {
        Sequence node = this;
        Sequence copy = new Sequence();
        
        // iterate this instance of Sequence
        while (node != null)
        {
            // if node.content is type MyChar
            if (node.content instanceof MyChar)
            {
                // create new object of MyChar and copy node.content, then add it into copy sequence
                MyChar temp_char = new MyChar();
                temp_char.Set(((MyChar)node.content).Get());
                copy.add(temp_char, copy.length());
            }
            
            // if node.content is type MyInteger
            if (node.content instanceof MyInteger)
            {
                // create new object of MyInteger and copy node.content, then add it into copy sequence
                MyInteger temp_int = new MyInteger();
                temp_int.Set(((MyInteger)node.content).Get());
                copy.add(temp_int, copy.length());
            }
            
            // if node.content is type Sequence
            if (node.content instanceof Sequence)
            {
                // create new object of Sequence and copy node.content, then add it into copy sequence
                Sequence temp_seq = new Sequence();
                temp_seq = ((Sequence)node.content).copy();
                copy.add(temp_seq, copy.length());

                /* don't want to copy each element in the nested sequence, just add the Sequence itself
                int length = temp_seq.length();
                
                for (int i = 0; i < length && temp_seq.next != null; i++)
                {
                    copy.add(temp_seq.content, copy.length());
                    temp_seq = temp_seq.next;
                }
                 */
            }
            
            // go to the next node in this sequence
            node = node.next;
        }
        return copy;
    }
    
}




















