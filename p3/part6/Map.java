class Map //extends Sequence
{
    Element content;
    Map next;
    
    // Map constructors
    public Map() { content = null; next = null; }
    public Map(Element content, Map next)
    {
        this.content = content;
        this.next = next;
    }
    
    public MapIterator begin()
    {
        MapIterator it = new MapIterator();
        it.node = this;
        return it;
    }
    
    public MapIterator end()
    {
        MapIterator it = new MapIterator();
        Map node = this;
        
        while (node.next != null)
        {
            node = node.next;
        }
        
        it.node = node;
        
        return it;
    }
    
    public void Print()
    {
        Map node = this;
        
        System.out.print("[");
        
        while (node != null)
        {
            System.out.print(" ");

            if (node.content != null)
            {
                if (node.content instanceof Pair)
                {
                    System.out.print("(");
                    
                    ((Pair)node.content).key.Print();
                    
                    System.out.print(" ");
                    
                    if (((Pair)node.content).value instanceof MyChar)
                        ((MyChar)(((Pair)node.content).value)).Print();
                    
                    if (((Pair)node.content).value instanceof MyInteger)
                        ((MyInteger)(((Pair)node.content).value)).Print();
                    
                    if (((Pair)node.content).value instanceof Sequence)
                        ((Sequence)(((Pair)node.content).value)).Print();
                    
                    System.out.print(")");
                }
            }


            // iterate to the next element
            node = node.next;
        }
        
        // print a space at the end to space the end bracket
        //System.out.print(" ");
        System.out.print("]");
    }
    
    public void add(Pair inval)
    {
        Map insert_map = new Map();
        Map node = this;
        
       while (node.next != null)
       {
           if (inval.key.Get() < ((Pair)node.content).key.Get())
               break;
           
           node = node.next;
       }

        
        insert_map.content = node.content;
        insert_map.next = node.next;
            
        node.content = inval;
        node.next = insert_map;
    }
    
    public MapIterator find(MyChar key)
    {
        MapIterator index_itr = new MapIterator();
        index_itr.node = new Map();
        Map node = this;
        
        while(node.next != null)
        {
            if (((Pair)node.content).key.Get() == key.Get())
                break;
            
            node = node.next;
        }
        
        index_itr.node = node;
        return index_itr;
    }
}











