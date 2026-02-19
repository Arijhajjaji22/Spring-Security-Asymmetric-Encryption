INSERT INTO ROLES (id, name, created_date, last_modified_date, created_by, last_modified_by)
VALUES (RANDOM_UUID(), 'user', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO ROLES (id, name, created_date, last_modified_date, created_by, last_modified_by)
VALUES (RANDOM_UUID(), 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
