class Pair extends Element
{
    public MyChar key;
    public Element value;
    
    // Pair constructors
    Pair() { key.Set('0'); value = null; }
    Pair(MyChar key, Element value)
    {
        this.key = key;
        this.value = value;
    }
    
    public void Print()
    {
        System.out.print("(");
        key.Print();
        System.out.print(" ");
        if (value != null)
            value.Print();
        
        System.out.print(")");
    }
}
