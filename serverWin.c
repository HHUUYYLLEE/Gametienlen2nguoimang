#include <stdio.h>
#include <sys/types.h> 
#include <winsock2.h>
#include <windows.h>
#include <ws2tcpip.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include<stdlib.h>
#include <sys/time.h>



const char handNumberString[] = "26";


int size;
int card[52];
int firstTurnPlayer;
bool beginMatch = true;
int sockfd, clisockfd1, clisockfd2, portno;
socklen_t client1, client2;
char buffer1[256], buffer2[256], temp[256];
struct sockaddr_in serv_addr, cli_addr1, cli_addr2;
int n;
char *endPtrStrTol = NULL;


void shuffle(size_t n)
{
    if (n > 1) 
    {
        size_t i;
        for (i = 0; i < n - 1; i++) 
        {
          size_t j = i + rand() / (RAND_MAX / (n - i) + 1);
          int t = card[j];
          card[j] = card[i];
          card[i] = t;
        }
    }
}

void checkErrorReadSocket(){
    if (n < 0)
        {
            perror("ERROR reading from socket");
            closesocket(sockfd);
            WSACleanup();
            return;
        }
}

void checkErrorWriteSocket(){
    if (n < 0)
            {
                perror("ERROR writing to socket");
                closesocket(sockfd);
        WSACleanup();
                return;
            } 
}

void decideFirstTurn(){
    memset(buffer1, 0, 256);
    memset(buffer2, 0, 256);
   n = recv(clisockfd1, buffer1, 256, 0);checkErrorReadSocket();
   n = recv(clisockfd2, buffer2, 256, 0);checkErrorReadSocket();
   if((int) strtol(buffer1, &endPtrStrTol, 10) == 70) firstTurnPlayer = 1;
   else firstTurnPlayer = 2;
  
}

void shuffleAndDealCards(){
    int i;
    memset(buffer1, 0, 256);
    memset(buffer2, 0, 256);
    memset(temp, 0, 256);
    shuffle(52);
    
    for(i = 0; i < size; ++i){
            if(card[i] >= 10) sprintf(temp, "%d", card[i]);
            else sprintf(temp,"0%d",card[i]);
            strcat(buffer1, temp);
    }
    for(i = size; i < size * 2; ++i){
            if(card[i] >= 10) sprintf(temp, "%d", card[i]);
            else sprintf(temp,"0%d",card[i]);
            strcat(buffer2, temp);
    }
    printf("send %s to 1\n", buffer1);
    printf("send %s to 2\n", buffer2);
    strcat(buffer1, "\n");
    strcat(buffer2, "\n");
    n = send(clisockfd1, buffer1, 256, 0);checkErrorWriteSocket();
    n = send(clisockfd2, buffer2, 256, 0);checkErrorWriteSocket();
}
void playMatch(){
        memset(buffer1, 0, 256);
    memset(buffer2, 0, 256);

        switch(firstTurnPlayer){
            case 1:
            while(1){
                memset(buffer1, 0, 256);
    memset(buffer2, 0, 256);
                n = recv(clisockfd1,buffer1,256, 0);checkErrorReadSocket();

                
                buffer1[strcspn(buffer1, "\n")] = '\0';

                printf("command from 1: %s\n", buffer1);
            if(strlen(buffer1) == 2 && ((int) strtol(buffer1, &endPtrStrTol, 10)) == 90) {
                n = send(clisockfd2, "90\n", 3, 0);checkErrorWriteSocket();
                break;
            }
            
                printf("send command %s to 2\n", buffer1);
                strcat(buffer1,"\n");
                n = send(clisockfd2, buffer1, 256, 0);checkErrorWriteSocket();
                n = recv(clisockfd2, buffer2, 256, 0);checkErrorReadSocket();

               
                buffer2[strcspn(buffer2, "\n")] = '\0';

                printf("command from 2: %s\n", buffer2);
            if(strlen(buffer2) == 2 && ((int) strtol(buffer2, &endPtrStrTol, 10)) == 90) {
                n = send(clisockfd1, "90\n", 3, 0);checkErrorWriteSocket();
                    break;
            }
           
                printf("send command %s to 1\n", buffer2);
                strcat(buffer2,"\n");
                n = send(clisockfd1, buffer2, 256, 0); checkErrorWriteSocket();
        }
            break;
            case 2:
            while(1){
                memset(buffer1, 0, 256);
    memset(buffer2, 0, 256);
                n = recv(clisockfd2,buffer2, 256, 0);checkErrorReadSocket();

                
                buffer2[strcspn(buffer2, "\n")] = '\0';
                
                printf("command from 2: %s\n", buffer2);
            if(strlen(buffer2) == 2 && ((int) strtol(buffer2, &endPtrStrTol, 10)) == 90) {
                n = send(clisockfd1, "90\n", 3, 0);checkErrorWriteSocket();
                break;
            }
               
                printf("send command %s to 1\n", buffer2);
                strcat(buffer2,"\n");
                n = send(clisockfd1, buffer2, 256, 0);checkErrorWriteSocket();
                n = recv(clisockfd1, buffer1, 256, 0);checkErrorReadSocket();

                 
                buffer1[strcspn(buffer1, "\n")] = '\0';

                printf("command from 1: %s\n", buffer1);
            if(strlen(buffer1) == 2 && ((int) strtol(buffer1, &endPtrStrTol, 10)) == 90) {
                n = send(clisockfd2, "90\n", 3, 0);checkErrorWriteSocket();
                    break;
            }
                
            printf("send command %s to 2\n", buffer1);
                strcat(buffer1,"\n");
                n = send(clisockfd2, buffer1, 256, 0); checkErrorWriteSocket();
            }
            break;
        }
       
}
int main( int argc, char *argv[] )
{
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2,2), &wsaData);
    size = ((int) strtol(handNumberString, &endPtrStrTol, 10));
    int i;
    srand(time(NULL));
    for(i = 0; i < 52; ++i) card[i] = i;
    //int optval;

    /* First call to socket() function */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
    {
        perror("ERROR opening socket");
        closesocket(sockfd);
        WSACleanup();
        return(1);
    }

    /* Initialize socket structure */
    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    portno = 8000;
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);


    if (bind(sockfd, (struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0)
    {
        perror("ERROR on binding");
        closesocket(sockfd);
        WSACleanup();
        return(1);
    }

    listen(sockfd,2);
    client1 = (socklen_t) sizeof(cli_addr1);
    client2 = (socklen_t) sizeof(cli_addr2);

    clisockfd1 = accept(sockfd, (struct sockaddr *)&cli_addr1, &client1);

    if (clisockfd1 < 0) 
    {
        perror("ERROR on accept");
        closesocket(sockfd);
        WSACleanup();
        return(1);
    }
    printf("Accept 1\n");

    clisockfd2 = accept(sockfd, (struct sockaddr *)&cli_addr2, &client2);

    if (clisockfd2 < 0) 
    {
        perror("ERROR on accept");
        closesocket(sockfd);
        WSACleanup();
        return(1);
    }
    printf("Accept 2\n");
    
        

    while (1)
    {
        
        /* If connection is established then start communicating */
        shuffleAndDealCards();
        decideFirstTurn();
        playMatch();
    }
    closesocket(sockfd);
    WSACleanup();
    return 0;
    

}