class MapIterator
{
    public Map node;

    public MapIterator() { node = null; }

    // if this node is equal to other MapIter node
    public boolean equal (MapIterator other)
    {
        if (node == other.node)
            return true;
        else
            return false;
    }
    
    MapIterator advance()
    {
        MapIterator next_node = new MapIterator();
        next_node.node = node.next;
        node = node.next;
        
        return next_node;
    }
    
    Pair get()
    {
        return (Pair)node.content;
    }
}
