package fxioc;

import fxioc.exception.DuplicateBeanException;
import fxioc.fixtures.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class IoCContainerTest {


    private IoCContainer container;

    @Before
    public void setUp() {
        container = new IoCContainer();
        container.register(Foo.class);
    }

    @Test
    public void shouldGetNewBeanByClass() {
        Foo foo = container.getBean(Foo.class);

        assertThat(foo, notNullValue(Foo.class));
    }

    @Test
    public void shouldGetNewBeanByInterface() {
        IFoo foo = container.getBean(IFoo.class);

        assertThat(foo, notNullValue(IFoo.class));
        assertThat(foo, instanceOf(Foo.class));
    }

    @Test
    public void shouldGetNewBeanByParent() {
        PFoo pFoo = container.getBean(PFoo.class);

        assertThat(pFoo, notNullValue(PFoo.class));
        assertThat(pFoo, instanceOf(Foo.class));
    }

    @Test
    public void shouldGetNewBarByConstructorInject() {
        container.register(BarConstructor.class);

        BarConstructor bar = container.getBean(BarConstructor.class);

        assertThat(bar, notNullValue(BarConstructor.class));
        assertThat(bar.getFoo(), notNullValue(Foo.class));
    }

    @Test
    public void shouldGetNewBarBySetterInject() {
        container.register(BarSetter.class);

        BarSetter barSetter = container.getBean(BarSetter.class);

        assertThat(barSetter, notNullValue(BarSetter.class));
        assertThat(barSetter.getFoo(), notNullValue(Foo.class));
    }

    @Test(expected = DuplicateBeanException.class)
    public void shouldRaiseDuplicateServiceExceptionWhenRegister2DifferentImplClassWithSameInterface() {

    }
}
