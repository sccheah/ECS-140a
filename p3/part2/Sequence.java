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
        
        while (temp_next != null)
        {
            temp_next = temp_next.next;
            counter++;
        }
        
        return counter;
    }
    
    // could be wrong iteration to pos and inputting elm0
    public void add(Element elm, int pos)
    {
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: pos < 0 || pos > length()");
            System.exit(1);
        }
        
        if (pos == 0)
        {
            Sequence new_node = new Sequence();
            new_node.content = content;
            new_node.next = next;
            content = elm;
            next = new_node;
        
            return;
        }
         
        Sequence node = this;
        
        for (int i = 0; i < pos; i++)
        {
            if (node.next != null)
                node = node.next;
        }
        
        Sequence new_node = new Sequence();
        new_node.content = node.content;
        new_node.next = node.next;
        node.content = elm;
        node.next = new_node;
            
    }
    
    public void delete(int pos)
    {
        if (pos < 0 || pos > length())
        {
            System.err.println("Error: pos < 0 || pos > length()");
            System.exit(1);
        }
        
        if (pos == 0)
        {
            content = next.content;
            next = next.next;
            
            return;
        }
        
        Sequence node = new Sequence();
        node.next = next;
    
        for (int i = 0; i < pos; i++)
        {
            if (node.next != null)
                node = node.next;
        }
        
        if (node.next.next != null)
        {
            node.content = node.next.content;
            node.next = node.next.next;
        }
     }
    
    public void Print()
    {
        System.out.print("[ ");
        
        if (content != null)
            content.Print();
        
        Sequence temp_next = next;
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









