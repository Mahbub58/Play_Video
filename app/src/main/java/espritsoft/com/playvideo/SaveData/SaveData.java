package espritsoft.com.playvideo.SaveData;

import android.content.Context;
import android.content.SharedPreferences;

import espritsoft.com.playvideo.Audio.AudiouModel;

public class SaveData {
    static SharedPreferences Shredref;
    public SaveData(Context context){
        Shredref=context.getSharedPreferences("myRef", Context.MODE_PRIVATE);
    }
    public static void SaveData(int position){
        SharedPreferences.Editor editor=Shredref.edit();
        editor.putInt("position",position);
       // editor.putString("Password",Password);
        editor.commit();
    }
    public static int LoadData(){
        int FileContent= (int) Shredref.getInt("position", Integer.parseInt("0"));
     //   FileContent+="Password:"+Shredref.getString("Password","No Password");
        return FileContent;
    }

    public void SaveDataVideoAutoPlayOption(int position){
        SharedPreferences.Editor editor=Shredref.edit();
        editor.putInt("prev",position);
        // editor.putString("Password",Password);
        editor.commit();
    }
    public int LoadDataVideoAutoPlayOption(){
        int FileContent= (int) Shredref.getInt("prev", Integer.parseInt("0"));
        //   FileContent+="Password:"+Shredref.getString("Password","No Password");
        return FileContent;
    }


    public static void SaveHeadset(int result){
        SharedPreferences.Editor editor=Shredref.edit();
        editor.putInt("result",result);
        // editor.putString("Password",Password);
        editor.commit();
    }
    public static int LoadHeadset(){
        int FileContent= (int) Shredref.getInt("result", Integer.parseInt("1"));
        //   FileContent+="Password:"+Shredref.getString("Password","No Password");
        return FileContent;
    }
    public static void SaveHeadsetVoice(int voice){
        SharedPreferences.Editor editor=Shredref.edit();
        editor.putInt("voice",voice);
        // editor.putString("Password",Password);
        editor.commit();
    }
    public static int LoadHeadsetVoice(){
        int FileContent= (int) Shredref.getInt("voice", Integer.parseInt("1"));
        //   FileContent+="Password:"+Shredref.getString("Password","No Password");
        return FileContent;
    }
    public static void SaveHeadsetVoiceBlth(int voice){
        SharedPreferences.Editor editor=Shredref.edit();
        editor.putInt("voiceBlth",voice);
        // editor.putString("Password",Password);
        editor.commit();
    }
    public static int LoadHeadsetVoiceBlth(){
        int FileContent= (int) Shredref.getInt("voiceBlth", Integer.parseInt("1"));
        //   FileContent+="Password:"+Shredref.getString("Password","No Password");
        return FileContent;
    }

    private static final String SongName = "server_country";
    private static final String FolderName = "server_flag";
    private static final String AlbumName = "server_ovpn";
    private static final String ArtistName = "server_ovpn_user";
    private static final String Duration = "server_ovpn_password";
    private static final String AlbumCover = "server_ovpn_user";
    private static final String path = "server_ovpn_password";
    public AudiouModel getItem() {

        AudiouModel audiouModel = new AudiouModel(
                Shredref.getString(path,"vpn"),
                Shredref.getString(SongName,"Gaming Mode"),
                Shredref.getString(AlbumName,"vpngate_seongnam_tcp.ovpn"),
                Shredref.getString(ArtistName,"vpn"),
                Shredref.getString(AlbumCover,"vpn"),
                "","","",
                Shredref.getString(Duration,"vpn")
        );

        return audiouModel;
    }

}
