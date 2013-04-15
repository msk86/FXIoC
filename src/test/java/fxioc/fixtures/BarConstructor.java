package fxioc.fixtures;

public class BarConstructor {
    private Foo foo;

    public BarConstructor(Foo foo) {
        this.foo = foo;
    }

    public Foo getFoo() {
        return foo;
    }
}
