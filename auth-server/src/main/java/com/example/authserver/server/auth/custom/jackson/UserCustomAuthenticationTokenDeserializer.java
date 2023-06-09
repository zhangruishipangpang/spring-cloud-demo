package com.example.authserver.server.auth.custom.jackson;

import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;

/**
 * @author: 长安
 * 自定义 UserCustomAuthenticationToken 反序列化
 */
public class UserCustomAuthenticationTokenDeserializer extends JsonDeserializer<UserCustomAuthenticationToken> {

    private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<List<GrantedAuthority>>() {};
    private static final TypeReference<Object> OBJECT = new TypeReference<Object>() {};

        @Override
    public UserCustomAuthenticationToken deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
        JsonNode principalNode = readJsonNode(jsonNode, "principal");
        Object principal = getPrincipal(mapper, principalNode);
        JsonNode credentialsNode = readJsonNode(jsonNode, "credentials");
        Object credentials = getCredentials(credentialsNode);
        List<GrantedAuthority> authorities = mapper.readValue(readJsonNode(jsonNode, "authorities").traverse(mapper),
            GRANTED_AUTHORITY_LIST);
        UserCustomAuthenticationToken token = (!authenticated)
            ? UserCustomAuthenticationToken.of(principal, credentials)
            : UserCustomAuthenticationToken.of(principal, credentials, authorities);
        JsonNode detailsNode = readJsonNode(jsonNode, "details");
        if (detailsNode.isNull() || detailsNode.isMissingNode()) {
            token.setDetails(null);
        }
        else {
            Object details = mapper.readValue(detailsNode.toString(), OBJECT);
            token.setDetails(details);
        }
        return token;
    }

    private Object getCredentials(JsonNode credentialsNode) {
        if (credentialsNode.isNull() || credentialsNode.isMissingNode()) {
            return null;
        }
        return credentialsNode.asText();
    }

    private Object getPrincipal(ObjectMapper mapper, JsonNode principalNode)
        throws IOException, JsonParseException, JsonMappingException {
        if (principalNode.isObject()) {
            return mapper.readValue(principalNode.traverse(mapper), Object.class);
        }
        return principalNode.asText();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
