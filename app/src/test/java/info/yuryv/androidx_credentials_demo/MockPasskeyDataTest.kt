package info.yuryv.androidx_credentials_demo

import info.yuryv.androidx_credentials_demo.util.MockPasskeyData
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Validates that [MockPasskeyData] contains well-formed WebAuthn JSON with the required fields.
 *
 * These are unit tests — no Android context required (JSONObject comes from the JDK/Android stubs
 * and works in Robolectric-free unit tests via the org.json artifact on the test classpath).
 */
class MockPasskeyDataTest {

    @Test
    fun `RP ID is not blank`() {
        assertFalse(
            "RP_ID must not be blank",
            MockPasskeyData.RP_ID.isBlank(),
        )
    }

    @Test
    fun `registration request contains required WebAuthn fields`() {
        val json = JSONObject(MockPasskeyData.registrationRequest)

        assertNotNull("challenge is required", json.opt("challenge"))
        assertFalse("challenge must not be blank", json.getString("challenge").isBlank())

        val rp = json.getJSONObject("rp")
        assertNotNull("rp.id is required", rp.opt("id"))
        assertEquals("rp.id must match RP_ID", MockPasskeyData.RP_ID, rp.getString("id"))

        val user = json.getJSONObject("user")
        assertNotNull("user.id is required", user.opt("id"))
        assertNotNull("user.name is required", user.opt("name"))
        assertNotNull("user.displayName is required", user.opt("displayName"))

        val params = json.getJSONArray("pubKeyCredParams")
        assertFalse("pubKeyCredParams must not be empty", params.length() == 0)

        val authenticatorSelection = json.getJSONObject("authenticatorSelection")
        assertEquals(
            "residentKey must be required for discoverable credentials",
            "required",
            authenticatorSelection.getString("residentKey"),
        )
    }

    @Test
    fun `authentication request contains required WebAuthn fields`() {
        val json = JSONObject(MockPasskeyData.authenticationRequest)

        assertNotNull("challenge is required", json.opt("challenge"))
        assertFalse("challenge must not be blank", json.getString("challenge").isBlank())

        assertNotNull("rpId is required", json.opt("rpId"))
        assertEquals("rpId must match RP_ID", MockPasskeyData.RP_ID, json.getString("rpId"))

        // Empty allowCredentials triggers discoverable-credential (passkey picker) flow
        val allowCredentials = json.getJSONArray("allowCredentials")
        assertEquals(
            "allowCredentials must be empty to trigger passkey picker",
            0,
            allowCredentials.length(),
        )
    }

    @Test
    fun `registration and authentication use the same challenge in demo mode`() {
        val regChallenge = JSONObject(MockPasskeyData.registrationRequest).getString("challenge")
        val authChallenge = JSONObject(MockPasskeyData.authenticationRequest).getString("challenge")
        // In demo mode both use the same hardcoded challenge. In production each must be unique.
        assertEquals(
            "Both demo requests use the same hardcoded challenge",
            regChallenge,
            authChallenge,
        )
    }
}
