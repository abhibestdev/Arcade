package me.abhi.arcade.enums;

import org.bukkit.ChatColor;

public enum Messages {

    NO_PERMISSION("&cNo permission."),
    GAME_WON_VOTE("&a%game% &2has won the vote with a total of &a%votes%&2!"),
    GAME_STARTING_IN("%prefix% &eThe game will start in &6%seconds% &esecond(s)!"),
    GAME_STARTS_NOW("%prefix% &eThe game starts now!"),
    PLAYER_WON_GAME("%prefix% &ePlayer &6%player% &ehas won the game!"),
    CANT_CHAT_DURING_VOTING("&cYou cannot talk in chat during the voting period!"),
    NOT_VOTING_PERIOD("&cYou can only vote during the voting period!"),
    ALREADY_VOTED("&cYou have already voted!"),
    VOTED_FOR_GAME("&2You have voted for &a%game%&2!"),
    CANT_VOTE_FOR_GAME("&cYou cannot vote for that game at this time."),
    SPAWN_SET("&eThe spawn has been set for &6%spawn%&e!"),
    GAME_DOESNT_EXIST("&cThat game doesn't exist!"),
    SELECTING_REGION_FOR("&2Selecting region for &a%game%&2!"),
    EXITED_REGION_SELECTION("&cExited region select mode."),
    SET_POINT("&aSet point %point%."),
    PLAYER_JOINED("&a%player% &ehas joined the server!"),
    PLAYER_LEFT("&a%player% &ehas left the server!"),
    NOT_ENOUGH_PLAYERS_TO_START("&cThere are not enough players to start the game!"),
    PLAYER_ELIMINATED("%prefix% &ePlayer &6%player% &ehas been eliminated!"),
    ANVIL_DODGE_PERCENT("%prefix% &eAnvils starting to fall (&6%percent%&e)!"),
    GRACE_PERIOD("&aGrace period has started!"),
    GRACE_PERIOD_ENDED("&cGrace period has now ended!");

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
