import Biblioteca.BibliotecaApp;
import Console.Console;
import Menu.Menu.MainMenu;
import Menu.Option.CheckOutOption;
import Menu.Option.Option;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MenuTest {
    private Console console;
    private BibliotecaApp bibliotecaApp;
    private MainMenu mainMenu;

    @Before
    public void setUpStreams() {
        console= mock(Console.class);
        mainMenu = new MainMenu(console);
        bibliotecaApp = new BibliotecaApp(console, new MainMenu(console));
    }

    @Test
    public void testShowCurrentMenu() throws Exception {
        mainMenu.showCurrentMenu();

        verify(console).print(mainMenu.getClass().toString());
    }

    @Test
    public void testShowOptions() throws Exception {
        mainMenu.showOptions();

        verify(console).print("[ L ] List Books\t");

    }

    @Test
    public void testSelectOption() throws Exception {
        assertThat(mainMenu.selectOption("l"), isA(Option.class));

        assertEquals(mainMenu.selectOption("l").getName(), "List Books");
    }

    @Test
    public void testInvalidOption() throws Exception {

        assertThat(mainMenu.selectOption("a"), isA(Option.class));
        assertEquals("ErrorOption", mainMenu.selectOption("b").getName());
    }

    @Test
    public void testOperate() throws Exception {
        Option option = mainMenu.selectOption("l");
        option.operate(bibliotecaApp);
        verify(console).print("<<Lean Thinking>>\tJames P. Womack\t2003-06-01\t000001\n");
        verify(console).print("<<Clean Code>>\tJames P. Womack\t2003-06-01\t000002\n");
    }

    @Test
    public void testAddOption() throws Exception {
        MainMenu mainMenu = new MainMenu(console);
        mainMenu.addOption(new CheckOutOption(console));
        mainMenu.showOptions();

        verify(console).print("[ L ] List Books\t");
        verify(console).print("[ C ] Check out\t");
    }


    @Test
    public void testCheckoutBookSuccessful() throws Exception {
        MainMenu mainMenu = new MainMenu(console);
        mainMenu.addOption(new CheckOutOption(console));

        assertEquals(mainMenu.selectOption("c").getClass(), CheckOutOption.class);

        bibliotecaApp.checkout("000001");
        bibliotecaApp.checkout("000002");
        bibliotecaApp.showAllBooks();

        verify(console, times(2)).print("Thank you! Enjoy the book.\n");
        verify(console, times(0)).print("Lean Thinking\n");
        verify(console, times(0)).print("Clean Code\n");
    }

    @Test
    public void testCheckoutBookFailed() throws Exception {
        MainMenu mainMenu = new MainMenu(console);
        mainMenu.addOption(new CheckOutOption(console));

        assertNotEquals(mainMenu.selectOption("a").getClass(), CheckOutOption.class);

        bibliotecaApp.checkout("wrong");
        bibliotecaApp.checkout("98794");
        bibliotecaApp.showAllBooks();

        verify(console, times(0)).print("Thank you! Enjoy the book.\n");
        verify(console, times(2)).print("That book is not available\n");
        verify(console, times(1)).print("Lean Thinking\n");
        verify(console, times(1)).print("Clean Code\n");


    }
}
