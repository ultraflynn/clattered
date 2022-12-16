package com.ultraflynn;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CliMainTest {
    @Mock
    public BufferedReader input;

    @Mock
    public PrintStream output;

    @Mock
    public Clattered clattered;

    @Captor
    public ArgumentCaptor<String> outputCaptor;

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

    @Test
    public void shouldAllowUserToPublishMessage() throws Exception {
        doReturn("Alice -> I love the weather today")
                .doReturn("quit")
                .when(input).readLine();

        cliMain.start();

        verify(clattered).publish("Alice", "I love the weather today");
    }

    @Test
    public void shouldAllowUsersMessagesToBeRead() throws Exception {
        doReturn(ImmutableList.of("I love the weather today"))
                .when(clattered).timeline("Alice");
        doReturn("Alice -> I love the weather today")
                .doReturn("Alice")
                .doReturn("quit")
                .when(input).readLine();

        cliMain.start();

        verify(clattered).timeline("Alice");

        verify(output, times(3)).println(outputCaptor.capture());
        assertThat(outputCaptor.getAllValues(), hasItem("I love the weather today"));
    }

    @Test
    public void shouldAllowUserToFollowAnotherUser() throws Exception {
        doReturn("Alice -> I love the weather today")
                .doReturn("Bob -> Damn! We lost!")
                .doReturn("Alice follows Bob")
                .doReturn("quit")
                .when(input).readLine();

        cliMain.start();

        verify(clattered).follow("Alice", "Bob");
    }

    @Test
    public void shouldAllowUserToListTheirFollows() throws Exception {
        doReturn(ImmutableList.of("Bob"))
                .when(clattered).follow("Alice", "Bob");

        doReturn("Alice -> I love the weather today")
                .doReturn("Bob -> Damn! We lost!")
                .doReturn("Alice follows Bob")
                .doReturn("Alice follows")
                .doReturn("quit")
                .when(input).readLine();

        cliMain.start();

        verify(clattered).follow("Alice", "Bob");

        verify(output, times(3)).println(outputCaptor.capture());
        assertThat(outputCaptor.getAllValues(), hasItem("Bob"));
    }

    @Test
    public void shouldAllowUsersWallToBeRead() throws Exception {
        doReturn(ImmutableList.of(
                "Alice - I love the weather today",
                "Bob - Damn! We lost!"))
                .when(clattered).wall("Alice");

        doReturn("Alice -> I love the weather today")
                .doReturn("Bob -> Damn! We lost!")
                .doReturn("Alice follows Bob")
                .doReturn("Alice wall")
                .doReturn("quit")
                .when(input).readLine();

        cliMain.start();

        verify(clattered).wall("Alice");

        verify(output, times(4)).println(outputCaptor.capture());
        assertThat(outputCaptor.getAllValues(), hasItem("Alice - I love the weather today"));
        assertThat(outputCaptor.getAllValues(), hasItem("Bob - Damn! We lost!"));
    }

    @Test
    public void shouldHandleIOExceptionWhenReadingInput() throws Exception {
        doThrow(new IOException()).when(input).readLine();

        cliMain.start();
    }

    @Test
    public void shouldHandleIOExceptionWhenClosingInput() throws Exception {
        doThrow(new IOException()).when(input).close();
        doReturn("quit").when(input).readLine();

        cliMain.start();
    }
}