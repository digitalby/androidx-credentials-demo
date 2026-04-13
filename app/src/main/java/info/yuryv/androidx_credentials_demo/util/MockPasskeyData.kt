package info.yuryv.androidx_credentials_demo.util

/**
 * DEMO DATA ONLY — never use these values in production.
 *
 * In a real Relying Party implementation:
 * - [registrationRequest]: fetched from your server's /auth/register/begin endpoint.
 *   The server generates a cryptographically random challenge per request, stores it
 *   server-side to prevent replay attacks, then verifies the signed response.
 * - [authenticationRequest]: fetched from your server's /auth/authenticate/begin endpoint.
 *   Same challenge requirements apply.
 *
 * The RP ID ([RP_ID]) must match a domain listed in your app's Digital Asset Links file
 * (https://<RP_ID>/.well-known/assetlinks.json). Without this, passkey creation
 * will fail with CreatePublicKeyCredentialDomException — expected in demo mode.
 */
object MockPasskeyData {
    /**
     * Relying Party ID. Must match a domain whose assetlinks.json associates this app
     * (package [info.yuryv.androidx_credentials_demo] + SHA-256 signing certificate).
     */
    const val RP_ID = "credentialsdemo.yuryv.info"

    /**
     * Fixed base64url-encoded challenge — 32 bytes of demo data.
     * A real server generates a fresh cryptographically random value per request.
     */
    private const val DEMO_CHALLENGE = "Uf68Xn4eLLA3V8tROCKgmgTkqpXIWZLM2nzFpMdJK0I"

    /**
     * WebAuthn PublicKeyCredentialCreationOptions JSON.
     *
     * Passed to [androidx.credentials.CreatePublicKeyCredentialRequest].
     * In production, your server's /register/begin endpoint returns this JSON after
     * generating a unique challenge and storing it for later verification.
     */
    val registrationRequest: String =
        """
        {
          "challenge": "$DEMO_CHALLENGE",
          "rp": {
            "name": "Credentials Demo",
            "id": "$RP_ID"
          },
          "user": {
            "id": "ZGVtby11c2VyLTAwMQ==",
            "name": "demo@example.com",
            "displayName": "Demo User"
          },
          "pubKeyCredParams": [
            { "type": "public-key", "alg": -7 },
            { "type": "public-key", "alg": -257 }
          ],
          "timeout": 60000,
          "attestation": "none",
          "authenticatorSelection": {
            "authenticatorAttachment": "platform",
            "residentKey": "required",
            "requireResidentKey": true,
            "userVerification": "required"
          },
          "excludeCredentials": []
        }
        """.trimIndent()

    /**
     * WebAuthn PublicKeyCredentialRequestOptions JSON.
     *
     * Passed to [androidx.credentials.GetPublicKeyCredentialOption].
     * In production, your server's /authenticate/begin endpoint returns this JSON.
     * [allowCredentials] is intentionally empty to trigger a discoverable-credential
     * (passkey picker) flow — the device presents all passkeys for [RP_ID].
     */
    val authenticationRequest: String =
        """
        {
          "challenge": "$DEMO_CHALLENGE",
          "rpId": "$RP_ID",
          "timeout": 60000,
          "userVerification": "required",
          "allowCredentials": []
        }
        """.trimIndent()
}
