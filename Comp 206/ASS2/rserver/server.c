#if defined (WIN32)
    #include <winsock2.h>
    typedef int socklen_t;
#elif defined (linux)
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #include <arpa/inet.h>
    #include <unistd.h>
    #define INVALID_SOCKET -1
    #define SOCKET_ERROR -1
    #define closesocket(s) close(s)
    typedef int SOCKET;
    typedef struct sockaddr_in SOCKADDR_IN;
    typedef struct sockaddr SOCKADDR;
#endif

#include <stdio.h>
#include <stdlib.h>

typedef struct
{
    char sender[100];
    char cmd[100];
} Command;


void writeCommand(Command command);

int splitCommand(char str[300], Command* command);
void split(char str[300], char first[100]);



int main()
{
    #if defined (WIN32)
            WORD wVersionRequested;
            WSADATA wsaData;
            int error;

            // Using MAKEWORD macro, Winsock version request 2.2
            wVersionRequested = MAKEWORD(2, 2);
            error = WSAStartup(wVersionRequested, &wsaData);
    #else
        int error = 0;
    #endif



    if (error != 0)
    {
        printf("Server: Socket dll not found!\n");
        return 0;
    }
    else
    {
        printf("Server: The   dll found!\n");
    }

    /* Confirm that the WinSock DLL supports 2.2.*/
    /* Note that if the DLL supports versions greater    */
    /* than 2.2 in addition to 2.2, it will still return */
    /* 2.2 in wVersion since that is the version we      */
    /* requested.                                        */
    #if defined (WIN32)
        if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 2 )
        {
            /* Tell the user that we could not find a usable WinSock DLL.*/
            printf("Server: The dll do not support the Winsock version %u.%u!\n", LOBYTE(wsaData.wVersion), HIBYTE(wsaData.wVersion));
            WSACleanup();
            return 0;
        }
        else
        {
               printf("Server: The dll supports the Winsock version %u.%u!\n", LOBYTE(wsaData.wVersion), HIBYTE(wsaData.wVersion));
               printf("Server: The highest version this dll can support: %u.%u\n", LOBYTE(wsaData.wHighVersion), HIBYTE(wsaData.wHighVersion));
        }
    #endif



    //////////Create a socket////////////////////////

    //Create a SOCKET object called m_socket.

    SOCKET m_socket;
    // Call the socket function and return its value to the m_socket variable.
    // For this application, use the Internet address family, streaming sockets, and
    // the TCP/IP protocol.
    // using AF_INET family, TCP socket type and protocol of the AF_INET - IPv4
    m_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    // Check for errors to ensure that the socket is a valid socket.
    if (m_socket == INVALID_SOCKET)
    {
        printf("Server: Error at socket");
        #if defined (WIN32)
            WSACleanup();
        #endif
        return 0;
    }
    else
    {
        printf("Server: socket() is OK!\n");
    }

    ////////////////bind//////////////////////////////

    // Create a sockaddr_in object and set its values.

    struct sockaddr_in service;

    // AF_INET is the Internet address family.
    service.sin_family = AF_INET;
    // "127.0.0.1" is the local IP address to which the socket will be bound.

    service.sin_addr.s_addr = inet_addr("127.0.0.1");
    // 55555 is the port number to which the socket will be bound.
    service.sin_port = htons(55555);

    // Call the bind function, passing the created socket and the sockaddr_in structure as parameters.

    // Check for general errors.
    if (bind(m_socket, (SOCKADDR*)&service, sizeof(service)) == SOCKET_ERROR)
    {
        printf("Server: bind() failed");
        closesocket(m_socket);
        return 0;
    }
    else
    {
        printf("Server: bind() is OK!\n");
    }



    // Call the listen function, passing the created socket and the maximum number of allowed

    // connections to accept as parameters. Check for general errors.

    if (listen(m_socket, 10) == SOCKET_ERROR)
       printf("Server: listen(): Error listening on socket");

    else

    {
       printf("Server: listen() is OK, I'm waiting for connections...\n");
    }

    // Create a temporary SOCKET object called AcceptSocket for accepting connections.

    SOCKET AcceptSocket;

    // Create a continuous loop that checks for connections requests. If a connection

    // request occurs, call the accept function to handle the request.

    printf("Server: Waiting for a client to connect...\n" );

    printf("***Hint: Server is ready...run your client program...***\n");

    // Do some verification...
    while (1)

    {
        AcceptSocket = SOCKET_ERROR;
        while (AcceptSocket == SOCKET_ERROR)
        {
            AcceptSocket = accept(m_socket, NULL, NULL);
        }

        // else, accept the connection...
        // When the client connection has been accepted, transfer control from the
        // temporary socket to the original socket and stop checking for new connections.
        printf("Server: Client Connected!\n");
        m_socket = AcceptSocket;
        break;

    }



    int bytesSent;
    int bytesRecv = SOCKET_ERROR;

    char sendbuf[200] = "Send some command:";
    // initialize to empty data...
    char recvbuf[300] = "";



    // Send some test string to client...
    printf("Server: Sending some test data to client...\n");
    bytesSent = send(m_socket, sendbuf, strlen(sendbuf), 0);

    if (bytesSent == SOCKET_ERROR)
    {
        printf("Server: send() error");
    }
    else
    {
        printf("Server: send() is OK.\n");
        printf("Server: Bytes Sent: %ld.\n", bytesSent);
    }

    // Receives some test string from client...and client
    // must send something lol...
    while(1)
    {
        clearArray(300, recvbuf);
        bytesRecv = recv(m_socket, recvbuf, 300, 0);
        if (bytesRecv == SOCKET_ERROR)
        {
               printf("Server: recv() error");
        }
        else
        {
            Command command;
            if(splitCommand(recvbuf, &command) != -1)
            {
                writeCommand(command);
                if(!strcmp(command.cmd,"quit"))
                {
                    break;
                }
            }

        }

    }
    #if defined (WIN32)
        WSACleanup();
    #endif
    return 0;

}

void clearArray(int n, char a[n])
{
    memset(a, 0, n);
}

int splitCommand(char message[300], Command* command)
{
    //Copy the message in a other array so we keep a trace of it
    char str[300];
    strcpy(str,message);

    char server[100];
    char sender[100];

    split(str, server);

    //Check the command is for this server
    if(!strcmp(server,"server"))
    {
        split(str, sender);

        strcat(sender,".txt");
        strcpy((*command).sender,sender);

        //Finaly add the command to the struct
        strcpy((*command).cmd,str);
    }
    else
    {
        printf("Message '%s' ignored.\n", message);
        return -1;
    }
    return 0;

}

//This function split char[] in two pieces when it find a space
void split(char str[300], char first[100])
{
    char last[300] = "\0";
    int found = 0;
    int i = 0;
    for(i = 0; i < strlen(str); i ++)
    {
        if(found == 0 && str[i] == ' ')
        {
                found = i+1;
                first[i] = '\0';
        }
        else
        {
            if(found == 0)
            {
                first[i] = str[i];
            }
            else
            {
                last[i-found] = str[i];
            }
        }
    }
    strcpy(str,last);
}



void writeCommand(Command command)
{

        FILE* file_ptr;
        file_ptr = fopen(command.sender, "a");
        if(file_ptr == NULL)
        {
            printf("[Error]: LOAD OUTPUT FILE\n");
            return;
        }

        //Print the command in the server file
        fputs(command.cmd, file_ptr);
        fputc('\n', file_ptr);
        fclose(file_ptr);



}
