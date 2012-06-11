package com.renault.c2dm.demo;

// Les différentes clefs pour utiliser C2DM de Renault ou de Google.
interface Config
{
	// Racine des clefs. En modifiant la base, l'application utilise l'API de Google ou de Renault
	static final String BASE="com.renault.android";
//	static final String BASE="com.google.android";
	static final String REGISTER=BASE+".c2dm.intent.REGISTER";
	static final String UNREGISTER=BASE+".c2dm.intent.UNREGISTER";
	static final String REGISTRATION=BASE+".c2dm.intent.REGISTRATION";
	static final String RECEIVE=BASE+".c2dm.intent.RECEIVE";
	
}