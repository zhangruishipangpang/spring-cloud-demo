package com.example.authserver.server.auth.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 长安
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtKeyProperties {

    private String keyId = "123-213-123-123";

}
