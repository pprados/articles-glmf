package fr.prados.android.c2dm.demo;

// Les différentes clefs pour utiliser C2DM de Simulator ou de Google.
interface Config
{
	// Racine des clefs. En modifiant la base, l'application utilise l'API de Google ou de Simulator
//	static final String BASE="fr.prados.android";
	static final String BASE="com.google.android";

	static final String REGISTER=BASE+".c2dm.intent.REGISTER";
	static final String UNREGISTER=BASE+".c2dm.intent.UNREGISTER";
	static final String REGISTRATION=BASE+".c2dm.intent.REGISTRATION";
	static final String RECEIVE=BASE+".c2dm.intent.RECEIVE";
	
    static final String PREFERENCE = Config.BASE;
    static final String POST_REGISTER=
    	BASE.equals("com.google.android")
    	? "https://google.com/accounts/ClientLogin"
    	: "http://10.0.2.2:8888/ClientLogin";
    static final String POST_URL=BASE.equals("com.google.android") 
    		? "https://android.apis.google.com/c2dm/send" 
    		: "http://10.0.2.2:8888/send";
	static final String SENDER_ID="mon.compte.google.pour.envoyer.des.messages@gmail.com";
	static final String SENDER_PASSWD="le.mot.de.passe.du.compte.google";
}