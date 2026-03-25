package com.yourcompany.bitcoinpayment.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Atomically allocates the next BIP32 derivation index for receive addresses.
 */
@Repository
public class AddressIndexRepository {

    private final JdbcTemplate jdbcTemplate;

    public AddressIndexRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long allocateNextIndex() {
        Long idx = jdbcTemplate.query(
                """
                        UPDATE address_index
                        SET next_index = next_index + 1
                        WHERE id = 1
                        RETURNING next_index - 1
                        """,
                rs -> rs.next() ? rs.getLong(1) : null);
        if (idx == null) {
            throw new IllegalStateException("address_index row missing; run Flyway migrations");
        }
        return idx;
    }
}
