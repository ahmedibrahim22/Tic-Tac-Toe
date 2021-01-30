
package server;

import Helper_Package.InsideXOGame;
import Helper_Package.Player;
import Helper_Package.RecordedMessages;
import Database.Database;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
   Gson g = new Gson();
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

    public Player getNewPlayer() {
        return newPlayer;
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
             {
                 try {
                     handelInvitationAcceptedRequest(msgObject);
                 } catch (SQLException ex) {
                     Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
                break; 
 
            case RecordedMessages.INVITATION_REJECTED:
                handelInvitationRejectedRequest(msgObject);
                break; 
            case RecordedMessages.GAME_PLAY_MOVE:
             {
                 try {
                     handelGamePlayMoveRequest(msgObject);
                 } catch (Exception ex) {
                     Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
                break; 
 
            case RecordedMessages.GAME_GOT_FINISHED:
             {
                 try {
                     handelGameGotFinishedRequest(msgObject);
                 } catch (SQLException ex) {
                     Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
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
           newPlayer.setStatus(true);
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
       objMsg.getPlayer().setStatus(true);///////////////       
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
        //System.out.println("ok");
        Vector<Player> players = new Vector<>();
        for(Map.Entry<Integer, ServerThread> handler : onlinePlayers.entrySet()){
            Player player = handler.getValue().getNewPlayer(); 
            if(player.getStatus() && !player.getIsPlaying()){// add he is not busy
                players.add(player);
            }
        }
        msgObject.setOperationResult(true);
        msgObject.setTypeOfOperation(RecordedMessages.RETREVING_PLAYERS_LIST);
        msgObject.players = players;
        ps.println(g.toJson(msgObject));
        //System.out.println(g.toJson(msgObject));
    }
   
    private void handelInviteRequest(InsideXOGame msgObject) {
        int opponentUserId = usernameToId.get(msgObject.getGame().getAwayPlayer());
        if(onlinePlayers.containsKey(opponentUserId)){//add he is not busy
            Player opponentPlayer = onlinePlayers.get(opponentUserId).getNewPlayer(); 
            if(opponentPlayer.getStatus() && !opponentPlayer.getIsPlaying()){
                msgObject.setOperationResult(true);
                msgObject.setTypeOfOperation(RecordedMessages.RECEIVING_INVITATION);
                onlinePlayers.get(opponentUserId).getPs().println(g.toJson(msgObject));//print in json format
                return;
            }
        }
        msgObject.setOperationResult(false);
        msgObject.setTypeOfOperation(RecordedMessages.INVITATION_REJECTED);
        ps.println(g.toJson(msgObject));
    }

    private void handelInvitationAcceptedRequest(InsideXOGame msgObject) throws SQLException {
        int opponentUserId = usernameToId.get(msgObject.getGame().getAwayPlayer());
        if(onlinePlayers.containsKey(opponentUserId)){
            Player opponentPlayer = onlinePlayers.get(opponentUserId).getNewPlayer(); 
            if(opponentPlayer.getStatus() && !opponentPlayer.getIsPlaying()){
                int gameId = Database.addPlayersGame(newPlayer.getPlayerId(), opponentUserId, 0);
                msgObject.setOperationResult(true);
                msgObject.setTypeOfOperation(RecordedMessages.INVITATION_ACCEPTED_FROM_SERVER);
                msgObject.getGame().setGameId(gameId);
                newPlayer.setOpponentId(opponentUserId);
                newPlayer.setIsPlaying(true);
                onlinePlayers.get(opponentUserId).getNewPlayer().setIsPlaying(true);
                onlinePlayers.get(opponentUserId).getNewPlayer().setOpponentId(newPlayer.getPlayerId());
                onlinePlayers.get(opponentUserId).getPs().println(g.toJson(msgObject));// json
                return;
            }
        }
    }

    private void handelInvitationRejectedRequest(InsideXOGame msgObject) {
        int opponentUserId = usernameToId.get(msgObject.getGame().getAwayPlayer());
        if(onlinePlayers.containsKey(opponentUserId) && onlinePlayers.get(opponentUserId).getNewPlayer().getStatus()){
            msgObject.setOperationResult(true);
            msgObject.setTypeOfOperation(RecordedMessages.INVITATION_REJECTED_FROM_SERVER);
            onlinePlayers.get(opponentUserId).getPs().println(g.toJson(msgObject));// json
        }else{//what if the inviter went off
            
        }
    }

    private void handelGamePlayMoveRequest(InsideXOGame msgObject) throws SQLException, IndexOutOfBoundsException, IllegalAccessException {
        char[] maz = msgObject.getGame().getSavedGame();
        maz[msgObject.getFieldPosition()] = msgObject.getSignPlayed();
        msgObject.getGame().setSavedGame(maz);
        if(onlinePlayers.containsKey(newPlayer.getOpponentId()) && onlinePlayers.get(newPlayer.getOpponentId()).getNewPlayer().getStatus()){
            msgObject.setOperationResult(true);
            msgObject.setTypeOfOperation(RecordedMessages.INCOMING_MOVE);
            onlinePlayers.get(newPlayer.getOpponentId()).getPs().println(g.toJson(msgObject));// json
        }else{// other player went off line
            Database.saveGame(msgObject.getGame().getGameId(), newPlayer.getGameId(), newPlayer.getOpponentId(), msgObject.getGame().getSavedGame());
            /*int myMoves, himMoves;
            myMoves = himMoves = 0;
            for(int i = 0; i < 9; i++){
                if(maz[i] == msgObject.getSignPlayed()){
                    myMoves++;
                }else if(maz[i] == 'X' || maz[i] == 'O'){
                    himMoves++;
                }
            }
            if(myMoves > himMoves){//handle to meet o
                Database.saveGame(msgObject.getGame().getGameId(), newPlayer.getGameId(), newPlayer.getOpponentId(), msgObject.getGame().getSavedGame());                
            }else{
                Database.saveGame(msgObject.getGame().getGameId(), newPlayer.getOpponentId(), newPlayer.getPlayerId(), msgObject.getGame().getSavedGame());                                
            }*/
        }
    }

    private void handelGameGotFinishedRequest(InsideXOGame msgObject) throws SQLException {
        msgObject.setOperationResult(true);
        msgObject.setTypeOfOperation(RecordedMessages.GAME_GOT_FINISHED_SECCUSSFULLY);
        newPlayer.setIsPlaying(false);
        onlinePlayers.get(newPlayer.getOpponentId()).getNewPlayer().setIsPlaying(false);
        onlinePlayers.get(newPlayer.getOpponentId()).getNewPlayer().setOpponentId(0);
        newPlayer.setOpponentId(0);
        Database.updatePlayerScore(newPlayer.getPlayerId(), 10);
        ps.println(g.toJson(msgObject));
    }

    private void handelResumeRequest(InsideXOGame msgObject) {
        
    }

    private void handelChatRequest(InsideXOGame msgObject) {
        if(onlinePlayers.containsKey(newPlayer.getOpponentId()) && onlinePlayers.get(newPlayer.getOpponentId()).getNewPlayer().getStatus()){
            msgObject.setOperationResult(true);
            msgObject.setTypeOfOperation(RecordedMessages.CHAT_PLAYERS_WITH_EACH_OTHERS_FROM_SERVER);
            onlinePlayers.get(newPlayer.getOpponentId()).getPs().println(g.toJson(msgObject));//json
        }
    }

    private void handelBackRequest(InsideXOGame msgObject) {
        newPlayer.setIsPlaying(false);
        msgObject.setOperationResult(true);
        msgObject.setTypeOfOperation(RecordedMessages.BACK_FROM_SERVER);
        ps.println(g.toJson(msgObject));//json
    }

    public PrintStream getPs() {
        return ps;
    }

    public void setPs(PrintStream ps) {
        this.ps = ps;
    }
    
    
}

