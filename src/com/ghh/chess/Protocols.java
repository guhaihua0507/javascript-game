package com.ghh.chess;

/**
 * @author haihua.gu <br>
 *         Create on May 10, 2010
 */

public interface Protocols {
    public final static String PROTOCOL_GAMESTATE       = "00";
    public final static String PROTOCOL_SYNCSTATE       = "01";
    public final static String PROTOCOL_PLAYASTEP       = "02";
    public final static String PROTOCOL_JOINGAME        = "03";
    public final static String PROTOCOL_LEAVEGAME       = "04";
    public final static String PROTOCOL_READYPLAY       = "05";

    public final static String CLIENT_PROTOCOL_WAITING  = "00";
    public final static String CLIENT_RPOTOCOL_START    = "01";
    public final static String ClIENT_PROTOCOL_SYNC     = "02";
    public final static String CLIENT_PROTOCOL_GAMEOVER = "03";
}
