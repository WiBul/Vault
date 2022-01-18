// Created by E.S. Olthof
// 20210712

package com.example.thevault.support.authorization;

import com.example.thevault.domain.model.Klant;


import java.util.Optional;
import java.util.UUID;

public interface TokenKlantCombinatieDao {

    public TokenKlantCombinatie slaTokenKlantPairOp(TokenKlantCombinatie tokenKlantCombinatie);

    public Optional<TokenKlantCombinatie> vindTokenKlantPairMetKey (UUID key);

    public Optional<TokenKlantCombinatie> vindTokenKlantCombinatieMetKlant(Klant klant);

    public UUID delete(UUID uuid);
}
