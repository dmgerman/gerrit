-- Upgrade: schema_version 10 to 11
--

ALTER TABLE accounts ADD use_flash_clipboard CHAR(1);
UPDATE accounts SET use_flash_clipboard = 'Y';
ALTER TABLE accounts ALTER COLUMN use_flash_clipboard SET DEFAULT 'N';
ALTER TABLE accounts ALTER COLUMN use_flash_clipboard SET NOT NULL;

UPDATE schema_version SET version_nbr = 11;
