package com.ultraflynn;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.PrintStream;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CliMainTest {
    @Mock
    public BufferedReader input;

    @Mock
    public PrintStream output;

    private CliMain cliMain;

    @Before
    public void setUp() {
        cliMain = new CliMain(input, output);
    }

    @Test
    public void shouldAllowUserToQuit() throws Exception {
        doReturn("quit").when(input).readLine();

        cliMain.start();
    }
//        doReturn("Alice -> I love the weather today").when(input).readLine();
}