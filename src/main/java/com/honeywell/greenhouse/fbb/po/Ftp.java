package com.honeywell.greenhouse.fbb.po;

import org.apache.commons.net.ftp.FTPClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ftp {
    /**
     * Set if the FTP service is enabled, default true.
     */
    private boolean enabled = true;

    /**
     * Set the FTP host
     */
    private String host;

    /**
     * Set the FTP port
     */
    private int port = 21;

    /**
     * Set the FTP username
     */
    private String username;

    /**
     * Set the FTP password
     */
    private String password;

    /**
     * Set the connect timeout for the socket. default 10000 (10 seconds)
     */
    private int connectTimeout = 10 * 1000; // 10 seconds

    /**
     * Set the (socket option) timeout on the command socket. default 120000 (120 seconds)
     */
    private int defaultTimeout = 120 * 1000; // 120 seconds

    /**
     * Set the (socket option) timeout on the data connection. default 300000 (300 seconds)
     */
    private int dataTimeout = 300 * 1000; // 300 seconds

    /**
     * Set the internal buffer size for buffered data streams. default 65536 (never set 1024, which is a known bug)
     */
    private int bufferSize = 65536;

    /**
     * File types defined by {@link FTPClient} constants:
     * <ul>
     * <li>{@link FTPClient#ASCII_FILE_TYPE}</li>
     * <li>{@link FTPClient#EBCDIC_FILE_TYPE}</li>
     * <li>{@link FTPClient#BINARY_FILE_TYPE} (DEFAULT)</li>
     * <li>{@link FTPClient#LOCAL_FILE_TYPE}</li>
     * </ul>
     */
    private int fileType = FTPClient.BINARY_FILE_TYPE;

    /**
     * ACTIVE_LOCAL_DATA_CONNECTION_MODE = 0 <br>
     * A constant indicating the FTP session is expecting all transfers
     * to occur between the client (local) and server and that the server
     * should connect to the client's data port to initiate a data transfer.
     * This is the default data connection mode when and FTPClient instance
     * is created.<br><br>
     * PASSIVE_LOCAL_DATA_CONNECTION_MODE = 2 <br>
     * A constant indicating the FTP session is expecting all transfers
     * to occur between the client (local) and server and that the server
     * is in passive mode, requiring the client to connect to the
     * server's data port to initiate a transfer.
     */
    private int clientMode = FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;

    /**
     * The maximum cache size. default 8 (recommend to be double size of cpu cores)
     */
    private int sessionPoolSize = 8;

    /**
     * Sets the limit of how long to wait for a session to become available. default 30000 (30 seconds)
     */
    private int sessionWaitTimeout = 30 * 1000;

}
