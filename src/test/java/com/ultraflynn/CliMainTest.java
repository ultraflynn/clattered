package com.ultraflynn;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CliMainTest {
    @Mock
    public BufferedReader input;

    @Mock
    public PrintStream output;

    @Mock
    public Clattered clattered;

    private CliMain cliMain;

    @Before
    public void setUp() {
        cliMain = new CliMain(input, output, clattered);
    }

    @Test
    public void shouldPresentBannerAndPromptAndThenAllowUserToQuit() throws Exception {
        doReturn("quit").when(input).readLine();

        cliMain.start();

        InOrder order = inOrder(input, output);
        order.verify(output).println(CliMain.WELCOME_BANNER);
        order.verify(output).print(CliMain.PROMPT);
        order.verify(input).readLine();
        order.verify(output).println("Quitting...");
        order.verify(input).close();
        order.verify(output).close();
        verifyNoMoreInteractions(input, output, clattered);
    }

    @Ignore
    @Test
    public void shouldAllowUserToPublishMessage() throws Exception {
        doReturn("Alice -> I love the weather today").when(input).readLine();
        doReturn("quit").when(input).readLine();

        cliMain.start();
    }
}