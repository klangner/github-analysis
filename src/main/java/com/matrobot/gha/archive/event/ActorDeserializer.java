package com.matrobot.gha.archive.event;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Deserialize Actor class. 
 * Sometimes it is string and sometimes object.
 */
class ActorDeserializer implements JsonDeserializer<Actor> {
	public Actor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		Actor actor = new Actor();
		if(json.isJsonPrimitive()){
			actor.login = json.getAsJsonPrimitive().getAsString();
		}
		else{
			JsonObject jsObj = json.getAsJsonObject();
			JsonElement login = jsObj.get("login");
			if(login != null){
				actor.login = login.getAsString();
			}
		}
	    return actor;
	}
}
