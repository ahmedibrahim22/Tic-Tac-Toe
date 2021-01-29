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
    private String userName,password,email,status;
    private int score,gameId;
    private boolean isPlaying;
    
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
    
    public Player (String _userName, String _status,int _score)
    {
        userName  = _userName;
        status    = _status;
        score     = _score;
    }
    
    public Player (String _userName,int _score)
    {
        userName  = _userName;
        score     = _score;
    };
    
    public Player (String _userName,String _email,String _lastName,String _status,int _score,boolean _isPlaying,int _gameId)
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
    

  public void setStatus(Integer _status){
        if(_status==0){
            status="Offline";
       }
       else if(_status==1){
            status="Online";
       }
       else if(_status==2){
            status="Busy";
       }
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
    

    
    public String getStatus(){
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