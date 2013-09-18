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


int main()
{

    // Initialize Winsock
    #if defined (WIN32)
        WSADATA wsaData;
        int error = WSAStartup(MAKEWORD(2,2), &wsaData);
     #else
        int error = 0;
    #endif

    if (error != 0)
        printf("Client: Error at WSAStartup().\n");

    // Create a SOCKET for connecting to server
    SOCKET ConnectSocket;
    ConnectSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (ConnectSocket == INVALID_SOCKET)
    {
    printf("Client: Error at socket()");
    #if defined (WIN32)
        WSACleanup();
    #endif
    return 0;
    }
    else
    printf("Client: socket() is OK.\n");
    // The sockaddr_in structure specifies the address family,
    // IP address, and port of the server to be connected to.

    struct sockaddr_in clientService;
    clientService.sin_family = AF_INET;
    clientService.sin_addr.s_addr = inet_addr("127.0.0.1");
    clientService.sin_port = htons(55555);

    // Connect to server.
    if (connect(ConnectSocket, (SOCKADDR*)&clientService, sizeof(clientService)) == SOCKET_ERROR)
    {
        printf("Client: Failed to connect.\n");
        #if defined (WIN32)
            WSACleanup();
        #endif
        return 0;

    }
    else
        printf("Client: connect() is OK.\n");

    // Declare and initialize variables.
    int bytesSent;
    int bytesRecv = SOCKET_ERROR;
    char command[100] = "";
    char recvbuf[100] = "";

    while(bytesRecv == SOCKET_ERROR )
    {
        bytesRecv = recv(ConnectSocket, recvbuf, 100, 0);

        if (bytesRecv == 0
        #if defined (WIN32)
            || bytesRecv == WSAECONNRESET
        #endif
        )
        {
            printf("Client: Connection Closed.\n");
            break;
        }
        else
        {
           printf("[SERVER]:%s\n", recvbuf);
        }
    }


    while(fgets(command, sizeof command, stdin))
    {
        //Open the file here so it send the command imediatly

        char fullcmd[300] = "server client1 ";
        int i;
        for ( i = 0; i < 50; i++ )
        {
            if (command[i] == '\n')
            {
                command[i] = '\0';
                break;
            }
        }

        if(strlen(command))
        {
            strcat(fullcmd, command);

            bytesSent = send(ConnectSocket, fullcmd, strlen(fullcmd), 0);

        }
        if(!strcmp(command,"quit"))
        {
            break;
        }
    }

    #if defined (WIN32)
        WSACleanup();
    #endif

    return 0;

}
