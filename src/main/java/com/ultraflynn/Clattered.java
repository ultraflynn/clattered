package com.ultraflynn;

import java.util.List;

interface Clattered {
    /**
     * Publishes a message on a users timeline.
     *
     * @param user The user on whose timeline the message is posted
     * @param text The message to be posted
     */
    void publish(String user, String text);

    /**
     * Gets the timeline for a user. Note that this will not include
     * any messages from the users that the specified user follows.
     *
     * @param user The user for whom you want the timeline
     * @return A list of messages with the newest first
     */
    List<String> timeline(String user);

    /**
     * Adds a follow between two users.
     *
     * @param user   The user doing the following
     * @param follow The user being followed
     * @return If follow is specified as a blank ("") then the list of
     * the users follows is return. Otherwise a null.
     */
    List<String> follow(String user, String follow);

    /**
     * Gets the wall for a user. This will include a messages for that user
     * plus all messages published by any users that this user follows.
     *
     * @param user The user who whom you want the wall
     * @return A list of messages with the newest first
     */
    List<String> wall(String user);
}
