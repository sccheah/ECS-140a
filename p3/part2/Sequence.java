public class Sequence extends Element
{
    Element content;
    Sequence next;
    public Sequence() { content = null; next = null; }
    
    public Element first()
    {
        return content;
    }
    
    public Sequence rest()
    {
        return next;
    }
    
    public int length()
    {
        int counter = 0;
        Sequence temp_next = next;
        
        // keep iterating until next is null and increase counter
        while (temp_next != null)
        {
            temp_next = temp_next.next;
            counter++;
        }
        
        return counter;
    }
    
    public void add(Element elm, int pos)
    {
        // if not between 0 and length of Sequence object
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: pos < 0 || pos > length()");
            System.exit(1);
        }
        
        Sequence node = this;
        
        // iterate to the position we want to add Element at
        for (int i = 0; i < pos; i++)
        {
            if (node.next != null)
                node = node.next;
        }
        
        // copy over existing content into new node and set next to next node in sequence
        Sequence new_node = new Sequence();
        new_node.content = node.content;
        new_node.next = node.next;
        
        // copy element into existing node in pos, and point to new node
        node.content = elm;
        node.next = new_node;
            
    }
    
    public void delete(int pos)
    {
        // if not between 0 and length of Sequence object
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: pos < 0 || pos > length()");
            System.exit(1);
        }
        
        Sequence node = new Sequence();
        node = this;
    
        // iterate to the position we want to delete
        for (int i = 0; i < pos; i++)
        {
            if (node.next != null)
                node = node.next;
        }
        
        // if there is a node two nodes ahead of position, copy one node ahead and change next ptr
        if (node.next.next != null)
        {
            node.content = node.next.content;
            node.next = node.next.next;
        }
        else
        {
            node = null;
        }
     }
    
    public void Print()
    {
        System.out.print("[ ");
        
        // print first content in node
        if (content != null)
            content.Print();
        
        // temp sequence to iterate squence
        Sequence temp_next = next;
        
        // iterate sequence and print content 
        while (temp_next != null)
        {
            if (temp_next.content != null)
            {
                System.out.print(" ");
                temp_next.content.Print();
            }
            
            temp_next = temp_next.next;
        }
         
        System.out.print(" ]");
    }
}









