#include <stdio.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
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


void shuffle(size_t n) {    
    if (n > 1) {
        size_t i;
        for (i = n - 1; i > 0; i--) {
            size_t j = (unsigned int) (drand48()*(i+1));
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
            return;
        }
}

void checkErrorWriteSocket(){
    if (n < 0)
            {
                perror("ERROR writing to socket");
                return;
            } 
}

void decideFirstTurn(){
    bzero(buffer1,256);
    bzero(buffer2,256);
   n = read(clisockfd1, buffer1, 256);checkErrorReadSocket();
   n = read(clisockfd2, buffer2, 256);checkErrorReadSocket();
   if((int) strtol(buffer1, &endPtrStrTol, 10) == 70) firstTurnPlayer = 1;
   else firstTurnPlayer = 2;
  
}

void shuffleAndDealCards(){
    int i;
    bzero(buffer1,256);
    bzero(buffer2,256);
    bzero(temp,256);
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
    n = write(clisockfd1, buffer1, 256);checkErrorWriteSocket();
    n = write(clisockfd2, buffer2, 256);checkErrorWriteSocket();

}
void playMatch(){
        bzero(buffer1,256);
        bzero(buffer2,256);

        switch(firstTurnPlayer){
            case 1:
            while(1){
                bzero(buffer1,256);
                bzero(buffer2,256);
                n = read(clisockfd1,buffer1,256);checkErrorReadSocket();

                
                buffer1[strcspn(buffer1, "\n")] = '\0';

                printf("command from 1: %s\n", buffer1);
            if(strlen(buffer1) == 2 && ((int) strtol(buffer1, &endPtrStrTol, 10)) == 90) {
                n = write(clisockfd2, "90\n", 3);checkErrorWriteSocket();
                break;
            }
            
                printf("send command %s to 2\n", buffer1);
                strcat(buffer1,"\n");
                n = write(clisockfd2, buffer1, 256);checkErrorWriteSocket();
                n = read(clisockfd2, buffer2, 256);checkErrorReadSocket();

               
                buffer2[strcspn(buffer2, "\n")] = '\0';

                printf("command from 2: %s\n", buffer2);
            if(strlen(buffer2) == 2 && ((int) strtol(buffer2, &endPtrStrTol, 10)) == 90) {
                n = write(clisockfd1, "90\n", 3);checkErrorWriteSocket();
                    break;
            }
           
                printf("send command %s to 1\n", buffer2);
                strcat(buffer2,"\n");
                n = write(clisockfd1, buffer2, 256); checkErrorWriteSocket();
        }
            break;
            case 2:
            while(1){
                bzero(buffer1,256);
                bzero(buffer2,256);
                n = read(clisockfd2,buffer2,256);checkErrorReadSocket();

                
                buffer2[strcspn(buffer2, "\n")] = '\0';
                
                printf("command from 2: %s\n", buffer2);
            if(strlen(buffer2) == 2 && ((int) strtol(buffer2, &endPtrStrTol, 10)) == 90) {
                n = write(clisockfd1, "90\n", 3);checkErrorWriteSocket();
                break;
            }
               
                printf("send command %s to 1\n", buffer2);
                strcat(buffer2,"\n");
                n = write(clisockfd1, buffer2, 256);checkErrorWriteSocket();
                n = read(clisockfd1, buffer1, 256);checkErrorReadSocket();

                 
                buffer1[strcspn(buffer1, "\n")] = '\0';

                printf("command from 1: %s\n", buffer1);
            if(strlen(buffer1) == 2 && ((int) strtol(buffer1, &endPtrStrTol, 10)) == 90) {
                n = write(clisockfd2, "90\n", 3);checkErrorWriteSocket();
                    break;
            }
                
            printf("send command %s to 2\n", buffer1);
                strcat(buffer1,"\n");
                n = write(clisockfd2, buffer1, 256); checkErrorWriteSocket();
            }
            break;
        }
       
}
int main( int argc, char *argv[] )
{
    size = ((int) strtol(handNumberString, &endPtrStrTol, 10));
    int i;
    struct timeval tv;
    gettimeofday(&tv, NULL);
    int usec = tv.tv_usec;
    srand48(usec);
    for(i = 0; i < 52; ++i) card[i] = i;
    //int optval;

    /* First call to socket() function */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
    {
        perror("ERROR opening socket");
        return(1);
    }

    /* Initialize socket structure */
    bzero((char *) &serv_addr, sizeof(serv_addr));
    portno = 8000;
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);


    if (bind(sockfd, (struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0)
    {
        perror("ERROR on binding");
        return(1);
    }

    listen(sockfd,2);
    client1 = (socklen_t) sizeof(cli_addr1);
    client2 = (socklen_t) sizeof(cli_addr2);

    clisockfd1 = accept(sockfd, (struct sockaddr *)&cli_addr1, &client1);

    if (clisockfd1 < 0) 
    {
        perror("ERROR on accept");
        return(1);
    }
    printf("Accept 1\n");

    clisockfd2 = accept(sockfd, (struct sockaddr *)&cli_addr2, &client2);

    if (clisockfd2 < 0) 
    {
        perror("ERROR on accept");
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
    close(sockfd);
    return 0;


}