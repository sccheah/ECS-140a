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
    
    public void add(Element elm, int pos)
    {
        if (pos < 0 || pos > length())
        {
            System.err.println("Add Error: pos < 0 || pos > length()");
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
            System.err.println("Delete Error: pos < 0 || pos > length()");
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
    
    public Element index(int pos)
    {
        if (pos < 0 || pos > length())
        {
            System.err.println("Index Error: pos < 0 || pos > length()");
            System.exit(1);
        }
        
        if (pos == 0)
            return this.content;
        
        Sequence node = this;
        
        for (int i = 0; i < pos; i++)
        {
            if (node.next != null)
                node = node.next;
        }
        
        return node.content;
    }

    public Sequence flatten()
    {
        Sequence new_sequence = new Sequence();
        Sequence node = this;
        
        while (node.next != null)
        {
            Sequence nested_sequence = new Sequence();
            
            if (node.content instanceof Sequence)
            {
                nested_sequence = ((Sequence)node.content).flatten();
                
                while (nested_sequence.next != null)
                {
                    new_sequence.add(nested_sequence.content, new_sequence.length());
                    nested_sequence = nested_sequence.next;
                }
            }
            else
            {
                new_sequence.add(node.content, new_sequence.length());
            }
            
            node = node.next;
        }
        
        return new_sequence;
    }
    
    public Sequence copy()
    {
        Sequence copy = new Sequence();
        Sequence node = this;
        
        while (node.next != null)
        {
            if (node.content instanceof MyChar)
            {
                MyChar temp_char = new MyChar();
                temp_char.Set(((MyChar)node.content).Get());
                copy.add(temp_char, copy.length());
            }
            
            if (node.content instanceof MyInteger)
            {
                MyInteger temp_int = new MyInteger();
                temp_int.Set(((MyInteger)node.content).Get());
                copy.add(temp_int, copy.length());
            }
            
            if (node.content instanceof Sequence)
            {
                Sequence temp_seqence = new Sequence();
                temp_seqence = (Sequence)node.content;
                copy.add(temp_seqence, copy.length());
            }
            
            node = node.next;
        }
        
        return copy;
    }
}






















