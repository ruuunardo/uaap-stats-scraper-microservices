package com.teamr.runardo.uaapstatsdata.configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.teamr.runardo.uaapstatsdata.entity.BballPlayerStat;
import com.teamr.runardo.uaapstatsdata.entity.PlayerStat;
import com.teamr.runardo.uaapstatsdata.entity.VballPlayerStat;

import java.io.IOException;
import java.util.regex.Pattern;

public class PlayerStatDeserializer extends JsonDeserializer<PlayerStat> {

    @Override
    public PlayerStat deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String type = node.get("gameResultId").asText();

        // Regular expressions to match different types
        Pattern patternBB = Pattern.compile(".*BB.*"); // Matches any type with 'A'
        Pattern patternVB = Pattern.compile(".*VB.*"); // Matches any type with 'B'

        // Determine the concrete type based on the 'type' field and regex matching
        if (patternBB.matcher(type).matches()) {
            return jsonParser.getCodec().treeToValue(node, BballPlayerStat.class);
        } else if (patternVB.matcher(type).matches()) {
            return jsonParser.getCodec().treeToValue(node, VballPlayerStat.class);
        } else {
            throw new IOException("Unknown type: " + type);
        }
    }
}
