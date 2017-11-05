public class SequenceIterator
{
    // node to reference the node in the Sequence
    public Sequence node;
    public SequenceIterator() { node = null; }
    
    // if this node is equal to the other SeqIter node reference
    public boolean equal (SequenceIterator other)
    {
        if (node == other.node)
            return true;
        else
            return false;
    }
    
    // go to the next node and also return an object that references that node
    public SequenceIterator advance()
    {
        SequenceIterator next_node = new SequenceIterator();
        next_node.node = node.next;
        node = node.next;

        return next_node;
    }
    
    // return the element that this node is pointing to 
    public Element get()
    {
        return node.content;
    }
    
}
