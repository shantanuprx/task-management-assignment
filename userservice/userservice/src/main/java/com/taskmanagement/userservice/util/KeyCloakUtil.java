package com.taskmanagement.userservice.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.userservice.dto.AuthRequestDto;
import com.taskmanagement.userservice.dto.KeyCloakUserVerficationDto;
import com.taskmanagement.userservice.dto.ProfileDetailDto;

@Component
public class KeyCloakUtil {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private KeyCloakUserVerficationDto keyCloakUserVerficationDto;

	@Autowired
	private Keycloak keycloakAdmin;

	@Value("${keycloak.realm}")
	private String realm;

	public void addUserToRealm(ProfileDetailDto dto) throws IllegalAccessException, JSONException {
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(dto.getUserId());
		user.setEmail(dto.getEmailId());
		user.setEmailVerified(true);
		user.setAttributes(Collections.singletonMap("origin", Collections.singletonList("userservice")));

		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setTemporary(false);
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(dto.getPassword());

		user.setCredentials(Collections.singletonList(credential));

		Response response = keycloakAdmin.realm(realm).users().create(user);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
		}
		assignClientRole(dto.getUserId(), keyCloakUserVerficationDto.getClientId(),
				dto.getUserRole().toUpperCase() + "_ROLE");
	}

	private String assignClientRole(String userId, String clientId, String roleName) {

		ClientRepresentation clientRepresentation = keycloakAdmin.realm(realm).clients().findAll().stream()
				.filter(client -> client.getClientId().equals(clientId)).collect(Collectors.toList()).get(0);

		UserRepresentation addedUser = keycloakAdmin.realm(realm).users().list().stream()
				.filter(a -> a.getUsername().equals(userId)).findFirst().get();
		
		List<RoleRepresentation> clientRoles = keycloakAdmin.realm(realm).clients().get(clientRepresentation.getId())
				.roles().list();
		
		RoleRepresentation roleToAssign = clientRoles.stream().filter(role -> role.getName().equals(roleName))
				.findFirst().orElse(null);
		if (roleToAssign == null) {
			throw new RuntimeException("Invalid role");
		}
		keycloakAdmin.realm(realm).users().get(addedUser.getId()).roles().clientLevel(clientRepresentation.getId())
				.add(Collections.singletonList(roleToAssign));
		return "Role assigned successfully.";
	}

	public String verifyUserFromKeyCloak(AuthRequestDto authRequestDto) throws IllegalAccessException, JSONException {
		StringBuilder payload = new StringBuilder();
		payload.append("client_id=").append(keyCloakUserVerficationDto.getClientId()).append("&").append("grant_type=")
				.append(keyCloakUserVerficationDto.getGrantType()).append("&").append("username=")
				.append(authRequestDto.getUserName()).append("&").append("password=")
				.append(authRequestDto.getPassword()).append("&").append("scope=")
				.append(keyCloakUserVerficationDto.getScope()).append("&")
				.append("client_secret=").append(keyCloakUserVerficationDto.getClientSecret());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<String>(payload.toString(), headers);
		String response = restTemplate.postForObject(keyCloakUserVerficationDto.getUrl(), request, String.class);
		JSONObject responseJSON = new JSONObject(response);
		if (responseJSON.has("error")) {
			throw new IllegalAccessException(
					responseJSON.getString("error") + " - " + responseJSON.getString("error_description"));
		} else {
			return responseJSON.optString("access_token");
		}
	}

}
