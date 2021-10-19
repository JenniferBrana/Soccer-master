package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database;

    public SoccerDatabase(){
        database = new Hashtable<String, SoccerPlayer>();
    }

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {

        String key = firstName + "##" + lastName;


        //check if player is in database
        if(database.containsKey(key)){
            return false;
        }

        SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);

        database.put(key, newPlayer);

        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String key = firstName + "##" + lastName;

        if(database.containsKey(key)){
            database.remove(key);
            return true;
        }

        return false;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String key = firstName + "##" + lastName;

        if(database.containsKey(key)){
            return database.get(key);
        }


        return null;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        if(database.containsKey(firstName + "##" + lastName)){
            SoccerPlayer bumped = getPlayer(firstName, lastName);
            bumped.bumpGoals();
            return true;
        }

        return false;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        if(database.containsKey(firstName + "##" + lastName)){
            SoccerPlayer bumped = getPlayer(firstName, lastName);
            bumped.bumpYellowCards();
            return true;
        }
        return false;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        if(database.containsKey(firstName + "##" + lastName)){
            SoccerPlayer bumped = getPlayer(firstName, lastName);
            bumped.bumpRedCards();
            return true;
        }
        return false;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if(teamName == null){
            return database.size();
        }

        int count = 0;

        Set<String> keys = database.keySet();

        for(String key: keys){
            if(database.get(key) != null){
                if(database.get(key).getTeamName().equals(teamName)){
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        int playerCount = numPlayers(teamName);

        //
        if(idx >= playerCount){
            return null;
        }

        int index = 0;



        Set<String> keys = database.keySet();
        if(teamName != null){
            for(String key: keys){
                SoccerPlayer temp = database.get(key);
                String tempTeam = temp.getTeamName();



                if(idx == 0){
                    if(tempTeam.equals(teamName)){
                        return database.get(key);
                    }
                }
                else{
                    if(tempTeam.equals(teamName)){
                        if(index == idx){
                            return database.get(key);
                        }

                        index++;
                    }
                }

            }
        }
        else{
            for(String key: keys){

                if(index == idx){
                    return database.get(key);
                }


                index++;
            }
        }

        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {

        if (!file.exists()){return false;}
        Scanner newScan= null;
        try {
            newScan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //return false;
        }
        while (true) {
            String firstName = newScan.next();
            String lastName = newScan.next();
            String teamName = newScan.next();

            int goals = Integer.parseInt(newScan.next());
            int redCards = Integer.parseInt(newScan.next());
            int uniformNum = Integer.parseInt(newScan.next());
            int yelCards = Integer.parseInt(newScan.next());

            removePlayer(firstName, lastName);

            addPlayer(firstName, lastName, uniformNum, teamName);

            for (int i=0; i<goals;i++){
                bumpGoals(firstName, lastName);
            }
            for (int i=0; i<redCards;i++){
                bumpRedCards(firstName, lastName);
            }
            for (int i=0; i<yelCards;i++){
                bumpYellowCards(firstName, lastName);
            }

            if (!newScan.hasNext()){break;}
        }
        return true;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {


        try {
            PrintWriter pw = new PrintWriter(file);
            for(SoccerPlayer p: database.values()) {
                pw.println(logString(p.getFirstName()));
                pw.println(logString(p.getLastName()));
                pw.println(logString(p.getTeamName()));
                pw.println(logString( Integer.toString((p.getGoals()))));
                pw.println(logString( Integer.toString((p.getRedCards()))));
                pw.println(logString( Integer.toString((p.getUniform()))));
                pw.println(logString( Integer.toString((p.getYellowCards()))));
                //pw.println(logString("******"));
            }
            pw.flush();
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {

        HashSet<String> hashSet = new HashSet<String>();

        for(SoccerPlayer temp: database.values()){
            if(!hashSet.contains(temp.getTeamName())){
                hashSet.add(temp.getTeamName());
            }
        }

        return hashSet;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
