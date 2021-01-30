
package server;

import Helper_Package.InsideXOGame;
import Helper_Package.Player;
import Helper_Package.RecordedMessages;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Noura Houssien
 */
class ServerThread extends Thread
{
   private final Socket socket;
   private DataInputStream dis;
   private PrintStream ps;
   private Player newPlayer;

   static Vector<ServerThread> playersVector =new Vector <>();
   static HashMap<Integer, ServerThread> onlinePlayers = new HashMap<>();
   static HashMap<String, Integer> usernameToId = new HashMap<>();
   
   public ServerThread(Socket s)
   {
     this.socket=s;
   }
    @Override
    public void run()
    {
       try {
           dis = new DataInputStream(socket.getInputStream());
           ps = new PrintStream(socket.getOutputStream(),true);
           newPlayer=new Player();
//           playersVector.add(this);
           String message;
 
            while(true) {
                message = dis.readLine();
                System.out.println("message:"+message);
                if(!message.isEmpty())
                {
                    try {
                        jsonMessageHandler(message);
                    } catch (ParseException ex) {
                        ex.getStackTrace();
                        System.out.println("error while call json message hundler ");
                    }
                }
            } 
       } catch (IOException ex) {
           ex.getStackTrace();
           System.out.println("server can not connect with client");
           try {
               socket.close();
               dis.close();
               ps.close();
//               playersVector.remove(this);
               
//               newPlayer.setStatus(false);
//               Database.updatePlayerStatus(newPlayer.getUserName(),0); //update status of player to be offline
               System.out.println("player is leaved and become offline");
           } catch (IOException e) {
               System.out.println("Error while closing socket connection from server");
               e.getStackTrace();
           }
       }
    }
   
    
    
    
    private void jsonMessageHandler(String data) throws ParseException {
         
         Gson gson=new Gson();
         InsideXOGame msgObject=gson.fromJson(data,InsideXOGame.class);
        
         String s ;
        switch (msgObject.getTypeOfOperation()) {
            case RecordedMessages.LOGIN:
                handelLogInRequest(msgObject);
                break;
            case RecordedMessages.SIGNUP:
                handelSinUpRequest(msgObject);
                break;
            case RecordedMessages.PLAYING_SINGLE_MODE:
                handelPlayingSingleModeRequest(msgObject);
                break;
            case RecordedMessages.SINGLE_MODE_GAME_FINISHED:
                handelSingleGameFinishedRequest(msgObject);
                break;
            case RecordedMessages.RETRIVE_PLAYERS:
                handelRetrivePlayersRequest(msgObject);
                break;
            case RecordedMessages.INVITE:
                handelInviteRequest(msgObject);
                break;    
            case RecordedMessages.INVITATION_ACCEPTED:
                handelInvitationAcceptedRequest(msgObject);
                break; 
            case RecordedMessages.INVITATION_REJECTED:
                handelInvitationRejectedRequest(msgObject);
                break; 
            case RecordedMessages.GAME_PLAY_MOVE:
                handelGamePlayMoveRequest(msgObject);
                break; 
            case RecordedMessages.GAME_GOT_FINISHED:
                handelGameGotFinishedRequest(msgObject);
                break; 
            case RecordedMessages.RESUME:
                handelResumeRequest(msgObject);
                break; 
            case RecordedMessages.CHAT_PLAYERS_WITH_EACH_OTHERS:
                handelChatRequest(msgObject);
                break; 
            case RecordedMessages.BACK:
                handelBackRequest(msgObject);
                break;     
        }
    }
    
    
    
    
   private void  handelLogInRequest(InsideXOGame objMsg)
   {
      
       Gson g=new Gson();
       Player player;
       String userName,password;
       int playerId=0;
       player = objMsg.getPlayer();
       userName=player.getUserName();
       password=player.getPassword();
//     playerId=Database.login(userName,password); //this function will return -1 if login faild
       if(playerId!=-1)
       {
//         Database.updatePlayerStatus(playerId,1);
           newPlayer.setStatus(1);
           newPlayer.setUserName(userName);
           newPlayer.setPassword(password);
           newPlayer.setIsPlaying(false);
           //set player id here
           ServerThread.onlinePlayers.put(playerId,this);
           ServerThread.usernameToId.put(userName,playerId);
           objMsg.setOperationResult(true);
           objMsg.setTypeOfOperation(RecordedMessages.LOG_IN_ACCEPTED);
           ps.println(g.toJson(objMsg));
          
       }
       else{
        objMsg.setTypeOfOperation(RecordedMessages.LOGIN_REJECTED);
        objMsg.setOperationResult(false);
       }
       
   }
   private void handelSinUpRequest(InsideXOGame objMsg)
   {
       Gson g=new Gson();
       Player player;
       String userName,email,password;
       int successRegister=0;
       player = objMsg.getPlayer();
       userName=player.getUserName();
       email=player.getEmail();
       password=player.getPassword();
       String[] inputData={userName,email,password};
//        successRegister=Database.register(inputData); 
       if(successRegister==1)
       {
         objMsg.setOperationResult(true);
         objMsg.setTypeOfOperation(RecordedMessages.SIGN_UP_ACCEPTED);
       }
       else
       {
          objMsg.setOperationResult(false);
          objMsg.setTypeOfOperation(RecordedMessages.SIGN_UP_REJECTED);
       }
       ps.println(g.toJson(objMsg));
   }
   private void broatCast(String msg)
   {
     for(ServerThread ch : ServerThread.playersVector)
     {
        ch.ps.println("Server: " +msg);
     }
   }


      private void handelPlayingSingleModeRequest(InsideXOGame objMsg) {
        
       Gson g=new Gson();
       Player player;
       String userName;
       player = objMsg.getPlayer();
       userName=player.getUserName();
//       Database.updatePlayerStatus(userName,2);
       objMsg.setOperationResult(true);
       objMsg.getPlayer().setIsPlaying(true);
       objMsg.getPlayer().setStatus(2);       
       objMsg.setTypeOfOperation(RecordedMessages.PLAYING_SINGLE_MODE);
       ps.println(g.toJson(objMsg));

    }

    private void handelSingleGameFinishedRequest(InsideXOGame msgObject) {
       Gson g=new Gson();
       Player player;
       String userName;
       player = msgObject.getPlayer();
       userName=player.getUserName();
//        Database.updatePlayerScore(userName,5);
       msgObject.setOperationResult(true);
       
       msgObject.getPlayer().setScore(5);
       msgObject.setTypeOfOperation(RecordedMessages.SINGLE_MODE_PLAYER_SCORE_UPDATED);
       ps.println(g.toJson(msgObject));
      
    }

    private void handelRetrivePlayersRequest(InsideXOGame msgObject) {
    }
   
    private void handelInviteRequest(InsideXOGame msgObject) {
    }

    private void handelInvitationAcceptedRequest(InsideXOGame msgObject) {
    }

    private void handelInvitationRejectedRequest(InsideXOGame msgObject) {
    }

    private void handelGamePlayMoveRequest(InsideXOGame msgObject) {
    }

    private void handelGameGotFinishedRequest(InsideXOGame msgObject) {
    }

    private void handelResumeRequest(InsideXOGame msgObject) {
    }

    private void handelChatRequest(InsideXOGame msgObject) {
    }

    private void handelBackRequest(InsideXOGame msgObject) {
    }
}

