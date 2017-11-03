public class Sequence extends Element
{
    public Element content;
    public Sequence next;
    public Sequence() { content = null; next = null; }
    
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
            System.out.print(" ");
            if (node.content != null)
            {
                // if element is instance of MyChar
                if (node.content instanceof MyChar)
                {
                    ((MyChar)node.content).Print();
                }
                
                // if element is instance of MyInteger
                if (node.content instanceof MyInteger)
                    ((MyInteger)node.content).Print();
                
                // if element is instance of Sequence
                if (node.content instanceof Sequence)
                    ((Sequence)node.content).Print();
            }
    
            // iterate to the next element
            node = node.next;
        }
        
        //System.out.print(" ");
        System.out.print("]");
    }
    
    
    
}




















