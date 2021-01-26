/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper_Package;

/**
 *
 * @author Ahmed Ibrahim
 */
public class Player {
  
    // Player Variables
    private String userName,password,email;
    private int score,gameId;
    private boolean isPlaying,status;
    
    //player constructors
    public Player(){};
    
    public Player(String _userName)
    {
        userName  =_userName;
    }
    
    public Player(String _userName, String _password)
    {
        userName  =_userName;
        password  =_password;
    }
    
    public Player(String _userName,String _password,String _email)
    {
        userName  =_userName;
        password  =_password;
        email =_email;
        
    }
    
    public Player (String _userName, boolean _status,int _score)
    {
        userName  = _userName;
        status    = _status;
        score     = _score;
    };
    
    public Player (String _userName,int _score)
    {
        userName  = _userName;
        score     = _score;
    };
    
    public Player (String _userName,String _email,String _lastName,boolean _status,int _score,boolean _isPlaying,int _gameId)
    {
        userName  = _userName;
        email     = _email;
        status    = _status;
        score     = _score;
        isPlaying = _isPlaying;
        gameId    = _gameId;
    }
    
    //setters
    public void setUserName(String _userName){
          userName=_userName;
    }
    
    public void setPassword(String _password){
          password=_password;
    }
    
    public void setFirstName(String _email){
         email=_email;
    }
    

    public void setStatus(boolean _status){
           status=_status;
    }
    
    public void setScore(int _score){
            score=_score;
    }
    
    public void setIsPlaying(boolean _isPlaying){
        isPlaying=_isPlaying;
    }
    
    public void setGameId(int _gameId){
           gameId=_gameId;
    }
    
    //getters
    public String getUserName(){
      return userName;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getEmail(){
        return email;
    }
    

    
    public boolean getStatus(){
        return status;
    }
    
    public int getScore(){
        return score;
    }
    
    public boolean getIsPlaying(){
        return isPlaying;
    }
    
    public int getGameId(){
        return gameId;
    }
    
}
