package com.arij.project.auth.request;

import lombok.Data;

/**
 * Représente la requête envoyée par le front pour l'authentification Google.
 */
@Data
public class GoogleAuthRequest {
    private String idToken; // obligatoire
}
